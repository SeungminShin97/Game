package Seungmin.Game.domain.member;

import Seungmin.Game.domain.member.memberDto.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
        // todo 아이디 찾기 구현
    };

    // 비밀번호 찾기
    @PostMapping("/member/password")
    @ResponseBody
    public void findPassword() {
        // todo 비밀번호 찾기 구현 (비밀번호 변경)
    }

    // 회원가입
    @PostMapping("/member/signup")
    @ResponseBody
    public boolean signup(@RequestBody MemberRequest memberRequest) {
        return memberService.saveMember(memberRequest);
    }

    // 회원가입 아이디 중복 검사
    @PostMapping("/member/duplicateLoginId")
    @ResponseBody
    public boolean duplicateLoginId(@RequestBody final String loginId) {
        return memberService.findLoginId(loginId);
    }
}
