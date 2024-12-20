package Seungmin.Game.config;

import Seungmin.Game.config.handlers.CustomTextWebSocketHandler;
import Seungmin.Game.interceptor.CustomHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CustomTextWebSocketHandler customTextWebSocketHandler;
    private final CustomHandshakeInterceptor customHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customTextWebSocketHandler, "/ws/chat")
                .addInterceptors(customHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
