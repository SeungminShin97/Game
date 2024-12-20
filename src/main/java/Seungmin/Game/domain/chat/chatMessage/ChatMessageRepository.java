package Seungmin.Game.domain.chat.chatMessage;

import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository <ChatMessage, Long> {
}
