package Seungmin.Game.domain.chat.chatRoomMember;

import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessage;
import Seungmin.Game.domain.chat.chatRoom.ChatRoom;
import Seungmin.Game.domain.member.memberDto.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              // 발신자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;          // 채팅방

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_message_id")
    private ChatMessage chatMessage;    // 마지막으로 읽은 채팅

    @Column
    private final LocalDateTime joinTime = LocalDateTime.now();
}
