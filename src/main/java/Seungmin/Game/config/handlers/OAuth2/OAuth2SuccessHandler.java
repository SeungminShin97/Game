package Seungmin.Game.config.handlers.OAuth2;

import Seungmin.Game.common.enums.Role;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.member.memberDto.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String identifier = oAuth2User.getName();

        Member member = memberRepository.findByIdentifier(identifier).orElseThrow(() -> new UsernameNotFoundException("회원 없음"));

        String redirectUrl = getRedierctUrlByRole(member.getRole(), identifier);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    private String getRedierctUrlByRole(Role role, String identifier) {
        if(role == Role.ROLE_NOT_REGISTERED) {
            return "/member/oauth/" + identifier;
        }
        return "/";
    }
}
