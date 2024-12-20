package Seungmin.Game.config.handlers;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.config.handlers.Authentication.CustomAnonymousAuthenticationToken;
import Seungmin.Game.domain.chat.chatMessage.ChatMessageService;
import Seungmin.Game.domain.chat.chatMessage.chatMessage.ChatMessageDto;
import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.member.memberDto.MemberChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class CustomTextWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageService chatMessageService;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    private final Set<WebSocketSession> publicSessions = new HashSet<>();    // 공용 채팅
//    private final Set<WebSocketSession> sessions = new HashSet<>();         // 현재 연결된 세션들
//    private final Map<Long, Set<WebSocketSession>> sessionMap = new HashMap<>();

    private static final int sessionRemoveSize = 3;     // 공용 채팅 세션 최대 수
    private static final Long AnonymousUserId = 1L;     // 익명 유저 아이디

    private final MemberChatDto anonymousMember;        // 익명 유저 객체

    @Autowired
    public CustomTextWebSocketHandler(ChatMessageService chatMessageService, MemberService memberService, ObjectMapper objectMapper) {
        this.chatMessageService = chatMessageService;
        this.memberService = memberService;
        this.objectMapper = objectMapper;
        this.anonymousMember = memberService.getMemberChatDtoById(AnonymousUserId);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String ipAddress = Objects.requireNonNull(session.getRemoteAddress()).getAddress().getHostAddress();

        Authentication authentication = (Authentication) session.getAttributes().get("userInfo");

        MemberChatDto userInfo = getUserInfo(authentication).orElseGet(() -> {
            String anonymousId = getAnonymousId(authentication);
            session.getAttributes().put("anonymousId", anonymousId);
            return anonymousMember;
        });

        session.getAttributes().put("ipAddress", ipAddress);
        session.getAttributes().put("userInfo", userInfo);

        log.info("user : {} // room : {} // ipAddress : {}", userInfo, session.getId(), ipAddress);

//        sessions.add(session);
//        log.info("total session : {}", sessions.size());
        publicSessions.add(session);     // 공용 채팅 자동 입장
        log.info("publicSession user : {}", publicSessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        log.info("session {}", chatMessageDto.toString());

        Long chatRoomId = chatMessageDto.getChatRoomId();         // 세션 가져오기

        if(chatRoomId == 1L) {                                // 공용 세션일 경우
            // 공용 세션의 수가 일정 수 이상일 경우 종료된 세션 삭제
            if(publicSessions.size() >= sessionRemoveSize) {
                log.info("remove closed publicSessions. publicSessionSize {}", publicSessions.size());
                removeClosedSession(publicSessions);
            }

            // 메세지 타입에 따른 처리
            chatMessageDto.getMessageType().process(session, chatMessageDto, this);
        }
    }

    /**
     *공용 채팅 입장 처리
     */
    public void handlePublicSessionEnter(WebSocketSession session) throws IOException {
        String anonymousId = (String) session.getAttributes().get("anonymousId");
        Map<String, String> response = new HashMap<>();
        response.put("messageType", "ENTER");
        response.put("anonymousId", anonymousId);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    /**
     * 공용 채팅 메세지 처리 (익명, 인증 사용자)
     */
    public void handlePublicSessionMessage(WebSocketSession session, ChatMessageDto chatMessageDto) {
        MemberChatDto userInfo = (MemberChatDto) session.getAttributes().get("userInfo");
        chatMessageDto.setMember(userInfo);

        if(userInfo.equals(anonymousMember)) {
            chatMessageDto.setIpAddress((String) session.getAttributes().get("ipAddress"));
            chatMessageDto.setAnonymousId((String) session.getAttributes().get("anonymousId"));
        }

        chatMessageService.saveChat(chatMessageDto);

        // IP주소 마스킹
        if(userInfo.equals(anonymousMember)) chatMessageDto.maskIpAddress();

        sendPublicMessage(chatMessageDto);
    }

    /**
     * 닫힌 세션 제거
     */
    private void removeClosedSession(Set<WebSocketSession> session) {
        session.removeIf(ses -> !publicSessions.contains(ses) || !ses.isOpen());
    }

    /** 공용 채팅 발송, 병렬 처리*/
    private <T> void sendPublicMessage(T message){
        publicSessions.parallelStream().forEach(publicSession -> {
            try{
                publicSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (IOException e) {
                log.warn("메세지 전송 실패 : {}", e.getMessage());
            }
        });
    }

    /**
     * 사용자 인증 정보 조회 <br>
     * 조회 : {@link MemberService#getMemberByAuthentication}
     * @return Optional
     */
    private Optional<MemberChatDto> getUserInfo(Authentication authentication) {
        try{
            Member member = memberService.getMemberByAuthentication(authentication);
            return Optional.of(member.toChatDto());
        } catch (CustomException e) {
            return Optional.empty();
        }
    }

    /**
     * 익명 사용자 랜덤 uuid 조회
     * @return 랜덤 uuid
     * @throws IllegalStateException 인증 사용자가 랜덤 uuid를 받으려고 할 때
     */
    private String getAnonymousId(Authentication authentication) {
        if (authentication instanceof CustomAnonymousAuthenticationToken) {
            return ((CustomAnonymousAuthenticationToken) authentication).getAnonymousId();
        }
        throw new IllegalStateException("anonymousId cannot be generated for non-anonymous users.");
    }

}
