package Seungmin.Game.interceptor;

import Seungmin.Game.common.dto.NotificationDto;
import Seungmin.Game.common.enums.Role;
import Seungmin.Game.common.exceptions.ApiUserNotLoggedInException;
import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    private static final String LOGIN_REQUIRED_MESSAGE = "로그인 후 이용해 주세요";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isLoggedIn(request)) {
            if(isNotRegisteredUser()) {                         // oauth 추가 정보 미인증 사용자
                String requestURI = request.getRequestURI();
                if(requestURI.startsWith("/member/oauth"))
                    return true;
                handleNotRegisteredUser(request, response);
                return false;
            }
            return true;
        } else {
            handleUnauthenticatedUser(request, response);
            return false;
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return request.getRemoteUser() != null;
    }

    private boolean isNotRegisteredUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            for(GrantedAuthority authority : authentication.getAuthorities()) {
                if(authority.getAuthority().equals(Role.Not_registered.name()))
                    return true;
            }
        }
        return false;
    }

    private void handleUnauthenticatedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/comment"))
            throw new ApiUserNotLoggedInException(LOGIN_REQUIRED_MESSAGE);

        NotificationDto notificationDto = NotificationDto.builder()
                .message(LOGIN_REQUIRED_MESSAGE)
                .method(RequestMethod.GET)
                .redirectUri("/")
                .data(null).build();

        HttpSession session = request.getSession();
        session.setAttribute("params", notificationDto);
        response.sendRedirect("/common/messageRedirect");
    }

    private void handleNotRegisteredUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberService.getMemberByAuthentication(authentication);
        String identifier = member.getIdentifier();

        NotificationDto notificationDto = NotificationDto.builder()
                .message("추가 사용자 정보를 입력해주세요")
                .method(RequestMethod.GET)
                .redirectUri("/member/oauth/" + identifier)
                .data(null).build();

        HttpSession session = request.getSession();
        session.setAttribute("params", notificationDto);
        response.sendRedirect("/common/messageRedirect");
    }

}
