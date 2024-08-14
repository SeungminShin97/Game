package Seungmin.Game.domain.OAuth;

import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.MemberResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final OAuthService oAuthService;
    private final MemberService memberService;

    @GetMapping(value="/kakao")
    public String kakaoConnect() {
        String url = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
        return "redirect:" + url;
    }

    @GetMapping(value = "/kakao/callback")
    public String kakaoCallback(String code, HttpServletRequest httpServletRequest, Model model) {

        // 인가코드 받기
        // -> 카카오 인증 서버가 Redirect URI로 인가 코드 전달해줌

        // 인가코드 기반으로 Access Token 발급
        String accessToken = null;
        try {
            accessToken = oAuthService.getKakaoAccessToken(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Access Token을 통해 사용자 정보 조회
        OAuthMember oAuthMember = null;
        try {
            oAuthMember = oAuthService.getKakaoOAuthMemberInfo(accessToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 카카오 사용자 정보 확인
        MemberResponse kakaoMember = memberService.getMemberByEmail(oAuthMember.getEmail());

        if(kakaoMember != null ){
            // 강제 로그인
            memberService.autoLogin(kakaoMember);
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return "redirect:/";
        } else {
            model.addAttribute("oauthMember", oAuthMember);
            return "member/addMemberInfo";
        }
    }

}
