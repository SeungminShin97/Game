package Seungmin.Game.common.enums;

import Seungmin.Game.config.handlers.CustomTextWebSocketHandler;
import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessageDto;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public enum MessageType {
    ENTER {
        @Override
        public void process(WebSocketSession session, ChatMessageDto chatMessageDto, CustomTextWebSocketHandler customTextWebSocketHandler) throws IOException {
            customTextWebSocketHandler.handlePublicSessionEnter(session);
        }
    },
//    LEAVE,
    MESSAGE {
        @Override
        public void process(WebSocketSession session, ChatMessageDto chatMessageDto, CustomTextWebSocketHandler customTextWebSocketHandler) {
            customTextWebSocketHandler.handlePublicSessionMessage(session, chatMessageDto);
        }
    },
//    FILE
    ;

    public abstract void process(WebSocketSession session, ChatMessageDto chatMessageDto, CustomTextWebSocketHandler customTextWebSocketHandler) throws IOException;
}
