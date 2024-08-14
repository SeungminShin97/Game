package Seungmin.Game.domain.member;

import Seungmin.Game.common.dto.ApiResponseDto;
import Seungmin.Game.common.enums.Provider;
import Seungmin.Game.domain.member.memberDto.MemberRequest;
import Seungmin.Game.domain.member.memberDto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    // oauth 회원가입
    @PostMapping("/member/oauth")
    @ResponseBody
    public ApiResponseDto oauthSignup(@RequestBody MemberRequest memberRequest, HttpServletRequest httpServletRequest) {
        String tempPassword = UUID.randomUUID().toString();
        memberRequest.setPassword(tempPassword);
        memberRequest.setProvider(Provider.Kakao);
        try{
            Long id = memberService.saveMember(memberRequest);
            MemberResponse kakaoMember = memberService.getMemberById(id);
            memberService.autoLogin(kakaoMember);
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return ApiResponseDto.builder()
                    .successStatus(true).build();
        } catch (Exception e) {
            return ApiResponseDto.builder()
                    .successStatus(false)
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
