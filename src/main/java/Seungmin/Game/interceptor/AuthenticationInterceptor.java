package Seungmin.Game.interceptor;

import Seungmin.Game.common.dto.NotificationDto;
import Seungmin.Game.common.exceptions.ApiUserNotLoggedInException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String LOGIN_REQUIRED_MESSAGE = "로그인 후 이용해 주세요";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isLoggedIn(request))
            return true;
        else {
            handleUnauthenticatedUser(request, response);
            return false;
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return request.getRemoteUser() != null;
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

}
