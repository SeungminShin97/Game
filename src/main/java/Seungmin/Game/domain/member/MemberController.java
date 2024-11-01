package Seungmin.Game.domain.member;

import Seungmin.Game.common.dto.ApiResponseDto;
import Seungmin.Game.common.dto.NotificationDto;
import Seungmin.Game.domain.member.memberDto.MemberRequest;
import Seungmin.Game.domain.member.memberDto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public String goLoginPage() { return "member/login"; }

    @PostMapping("/member")
    public String loginFailure() { return "member/login"; }



    // 아이디 찾기
    @PostMapping("/member/id")
    @ResponseBody
    public void findId() {
        // todo.java 아이디 찾기 구현
    };

    // 비밀번호 찾기
    @PostMapping("/member/password")
    @ResponseBody
    public void findPassword() {
        // todo.java 비밀번호 찾기 구현 (비밀번호 변경)
    }

    // 회원가입
    @PostMapping("/member/signup")
    @ResponseBody
    public ApiResponseDto signup(@Validated @RequestBody MemberRequest memberRequest) {
        try{
            memberService.saveMember(memberRequest);
            return ApiResponseDto.builder()
                    .successStatus(true).build();
        } catch (Exception e) {
            return ApiResponseDto.builder()
                    .successStatus(false)
                    .errorMessage(e.toString()).build();
        }
    }

    // oauth 추가정보 매핑
    @GetMapping("/member/oauth/{identifier}")
    public String addMemberInfo(@PathVariable String identifier, Model model) {
        try{
            MemberResponse memberResponse = memberService.getMemberByIdentifier(identifier);
            ApiResponseDto apiResponseDto = ApiResponseDto.builder()
                            .data(memberResponse).successStatus(true).build();
            model.addAttribute("apiResponseDto", apiResponseDto);
            return "member/addMemberInfo";
        } catch(Exception e) {
            NotificationDto params = NotificationDto.builder().message(e.getMessage()).redirectUri("/").build();
            model.addAttribute("params", params);
            return "common/messageRedirect";
        }
    }


    // oauth 추가정보 저장
    @PostMapping("/member/oauth")
    @ResponseBody
    public ApiResponseDto oauthSignup(@RequestBody MemberRequest memberRequest, HttpServletRequest httpServletRequest) {
        try{
            Long id = memberService.updateOAuthMemberByIdentifier(memberRequest);
            MemberResponse memberResponse = memberService.getMemberById(id);
            memberService.autoLogin(memberResponse);
            return ApiResponseDto.builder()
                    .successStatus(true).redirectUri("/").build();
        } catch (Exception e) {
            return ApiResponseDto.builder()
                    .successStatus(false)
                    .redirectUri("/member")
                    .errorMessage(e.toString()).build();
        }
    }

    // 회원가입 아이디 중복 검사
    @PostMapping("/member/duplicateLoginId")
    @ResponseBody
    public boolean duplicateLoginId(@RequestBody final String loginId) {
        return memberService.existLoginId(loginId);
    }
}
