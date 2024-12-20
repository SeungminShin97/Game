package Seungmin.Game.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null)
            attributes.put("userInfo", authentication);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if(exception != null) log.error("WebSocket handshake failed: ", exception.getMessage());
        else {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String socketInfo = servletRequest.getSession().getId();
            log.info("new user connected socket");
            log.info("socketId : " + socketInfo);
            log.info("sessionInfo : " + servletRequest.getSession().toString());
        }
    }

}
