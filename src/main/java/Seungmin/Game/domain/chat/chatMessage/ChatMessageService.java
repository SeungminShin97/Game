package Seungmin.Game.domain.chat.chatMessage;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessage;
import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessageDto;
import Seungmin.Game.domain.chat.chatRoom.ChatRoom;
import Seungmin.Game.domain.chat.chatRoom.ChatRoomRepository;
import Seungmin.Game.domain.member.MemberRepository;
import Seungmin.Game.domain.member.memberDto.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void saveChat(final ChatMessageDto chatMessageDto) {
        try{
            ChatMessage chatMessage = chatMessageConverter(chatMessageDto);
            chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.ChatSaveFailedException);
        }
    }

    private ChatMessage chatMessageConverter(ChatMessageDto chatMessageDto) {
        Long memberId = chatMessageDto.getMember().getId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException("유저 검색 실패"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId()).orElseThrow(() -> new CustomException(CustomExceptionCode.ChatRoomNotFoundException));

        return ChatMessage.builder()
                .member(member)
                .chatRoom(chatRoom)
                .content(chatMessageDto.getContent())
                .ipAddress(chatMessageDto.getIpAddress())
                .messageType(chatMessageDto.getMessageType())
                .build();
    }
}
