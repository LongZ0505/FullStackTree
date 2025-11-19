package com.longsocial.chat.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.longsocial.chat.dto.request.CreationChatMessageRequest;
import com.longsocial.chat.dto.response.ChatMessageResponse;
import com.longsocial.chat.entity.ChatMessage;
import com.longsocial.chat.entity.ParticipantInfo;
import com.longsocial.chat.entity.WebSocketSession;
import com.longsocial.chat.exception.AppException;
import com.longsocial.chat.exception.ErrorCode;
import com.longsocial.chat.mapper.ChatMessageMapper;
import com.longsocial.chat.repository.ChatMessageRepository;
import com.longsocial.chat.repository.ConversationRepository;
import com.longsocial.chat.repository.WebSocketSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import com.fasterxml.jackson.datatype:jackson-datatype-jsr310
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ChatMessageService {
    SocketIOServer socketIOServer;
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ChatMessageMapper chatMessageMapper;
    ObjectMapper objectMapper;
    WebSocketSessionRepository webSocketSessionRepository;
    public String create(CreationChatMessageRequest request) {
        var userId= SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userId.equals(request.getSenderId()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var conversation=conversationRepository.findById(request.getConversationId())
                .orElseThrow(()->new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        if(conversation.getParticipants().stream().noneMatch(participantInfo
                -> participantInfo.getUserId().equals(request.getSenderId())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var chat= ChatMessage.builder()
                .userId(request.getSenderId())
                .message(request.getMessage())
                .chatTimestamp(request.getChatTimestamp())
                .conversationId(request.getConversationId())
                .build();
        var message=chatMessageRepository.save(chat);
        var participantsId=conversation.getParticipants().stream().map(ParticipantInfo::getUserId).toList();
        Map<String, WebSocketSession> userIds=webSocketSessionRepository.findAllByUserId(participantsId)
                .stream().collect(Collectors.
                        toMap(WebSocketSession::getSocketSessionId,Function.identity()));
        socketIOServer.getAllClients().forEach(client -> {
            log.info("Connecting");
                var response=userIds.get(client.getSessionId().toString());
                if(!Objects.isNull(response)){
                    try {
                        var beSend= objectMapper.writeValueAsString(message);
                        client.sendEvent("message",beSend);
                        log.info(beSend);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    };
                }

    });

        return "done";
    }

    public List<ChatMessageResponse> getByConversationId(String conversationId) {
       conversationRepository.findById(conversationId)
                .orElseThrow(()->new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
       log.info(conversationId);
        var lstChat=chatMessageRepository.findByConversationIdOrderByChatTimestampDesc(conversationId);
        return lstChat.stream().map(chatMessageMapper::toResponse).toList();
    }

    public ChatMessageResponse getLatest(String conversationId) {
       conversationRepository.findById(conversationId)
                .orElseThrow(()->new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        var lastest=chatMessageRepository.findLatestMessage(conversationId);
        if(lastest.size()<1)
            throw new AppException(ErrorCode.CHAT_NOT_EXISTED);
        log.info("latest: {}",lastest.getFirst().toString());
        return chatMessageMapper.toResponse(lastest.getFirst());
    }
}
