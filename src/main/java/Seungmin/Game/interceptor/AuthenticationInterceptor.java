package Seungmin.Game.interceptor;

import Seungmin.Game.common.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {


    private final ObjectMapper mapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRemoteUser() != null)
            return true;
        else {
            MessageDto messageDto = MessageDto.builder()
                    .message("로그인 후 이용해 주세요")
                    .method(RequestMethod.GET)
                    .redirectUri("/")
                    .data(null).build();

//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().print(mapper.writeValueAsString(messageDto));
//            response.getWriter().flush();
            HttpSession session = request.getSession();
            session.setAttribute("params", messageDto);
            response.sendRedirect("/common/messageRedirect");

            return false;
        }


//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
