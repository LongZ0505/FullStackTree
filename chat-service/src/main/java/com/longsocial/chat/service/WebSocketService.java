package com.longsocial.chat.service;

import com.longsocial.chat.entity.WebSocketSession;
import com.longsocial.chat.repository.ConversationRepository;
import com.longsocial.chat.repository.WebSocketSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketService {
    WebSocketSessionRepository webSocketSessionRepository;
    ConversationRepository conversationRepository;
    public WebSocketSession create(WebSocketSession webSocketSession){
        return webSocketSessionRepository.save(webSocketSession);

    }
    public void delete(String webSocketSessionId){
        webSocketSessionRepository.deleteBySocketSessionId(webSocketSessionId);
    }
    public void sendEvent(WebSocketSession session, Object data){

    }
}
