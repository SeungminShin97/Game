package Seungmin.Game.domain.chat.chatMessage.chatMessage;

import Seungmin.Game.common.enums.MessageType;
import Seungmin.Game.domain.chat.chatRoom.ChatRoom;
import Seungmin.Game.domain.member.memberDto.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              // 발신자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;          // 채팅방

    @Column(nullable = false)
    private String content;     // 메세지
    @Column
    private String anonymousId;        // 익명 사용자 UUID
    @Column
    private String ipAddress;   // 발신자 ip 주소, 익명 사용자만 적용
    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private MessageType messageType;


    @Builder
    public ChatMessage(Member member, ChatRoom chatRoom, String content, String anonymousId, String ipAddress, MessageType messageType) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.anonymousId = anonymousId;
        this.ipAddress = ipAddress;
        this.content = content;
        this.messageType = messageType;
    }

    public ChatMessageDto toDto() {
        return ChatMessageDto.builder()
                .id(id)
                .member(member.toChatDto())
                .chatRoomId(chatRoom.getId())
                .anonymousId(anonymousId)
                .content(content)
                .ipAddress(ipAddress)
                .createdDate(createdDate)
                .messageType(messageType).build();

    }

}
