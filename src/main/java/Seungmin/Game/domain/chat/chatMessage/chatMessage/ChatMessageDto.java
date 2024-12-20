package Seungmin.Game.domain.chat.chatMessage.chatMessage;

import Seungmin.Game.common.enums.MessageType;
import Seungmin.Game.domain.member.memberDto.MemberChatDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessageDto {

    private Long id;
    private MemberChatDto member;
    private Long chatRoomId;
    private String content;
    private String anonymousId;
    private String ipAddress;
    private LocalDateTime createdDate;
    private MessageType messageType;

    public void maskIpAddress() {
        if(ipAddress.contains(":")) {                   // IPv6
            if(ipAddress.startsWith("::ffff:"))         // IPv4 호환 IPv6 처리
                ipAddress = ipAddress.substring(7);

            int index = ipAddress.indexOf('%');
            if(index != -1)                             // 네트워크 정보 제거
                ipAddress = ipAddress.substring(0, index);

            int lastIndex = ipAddress.lastIndexOf(":");
            if(lastIndex != -1) {
                int secondLastIndex = ipAddress.lastIndexOf(":", lastIndex - 1);
                if(secondLastIndex != -1)
                    ipAddress = ipAddress.substring(0, secondLastIndex) + ":xxxx:xxxx";
                else
                    ipAddress = ipAddress.substring(0, lastIndex) + ":xxxx";
            } else
                ipAddress = "not found IpAddress";

        } else {                        // IPv4
            int index = ipAddress.lastIndexOf(".");
            if(index != -1)
                ipAddress = ipAddress.substring(0, index) + ".xxx";
            else
                ipAddress = "not found IpAddress";
        }
    }
}
