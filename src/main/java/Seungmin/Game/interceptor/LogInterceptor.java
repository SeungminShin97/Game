package Seungmin.Game.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("=========================================");
        log.debug("==================BEGIN==================");
        log.debug("Request URI -> {}", request.getRequestURI());
        log.debug(String.valueOf(request.getParameterNames()));
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("===================END===================");
        log.debug("=========================================");
        log.debug("");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
