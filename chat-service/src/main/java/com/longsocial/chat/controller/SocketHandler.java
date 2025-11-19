package com.longsocial.chat.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.longsocial.chat.dto.request.IntrospectRequest;
import com.longsocial.chat.entity.WebSocketSession;
import com.longsocial.chat.service.IdentityService;
import com.longsocial.chat.service.WebSocketService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class SocketHandler {
    SocketIOServer server;
    IdentityService identityService;
    WebSocketService webSocketService;
    @OnConnect
    public void onSocketIOServer(SocketIOClient client){
        log.info("pppppp");
        var token= client.getHandshakeData().getSingleUrlParam("token");
        log.info("token:{} ",token);
        var check=identityService.introspect(IntrospectRequest.builder().token(token).build());
        log.info("check:{}",check);
        if(check.isValid()) {
            log.info("userID: {}",check.getUserId());
            WebSocketSession webSocketSession= WebSocketSession.builder()
                    .socketSessionId(client.getSessionId().toString())
                    .userId(check.getUserId())
                    .createdAt(new Date(System.currentTimeMillis()).toInstant())
                    .build();
            webSocketSession=webSocketService.create(webSocketSession);
            log.info("Session ID:{}",webSocketSession.getSocketSessionId());
        }else{
            log.info("UNAUTHENTICATED");
            client.disconnect();
        }
    }

    @OnEvent("candidate")
    public void onCandidate(SocketIOClient client, Object data) {
        log.info("onCandidate!!!");
        //send all candidate in room exclusiveSelf
        client.sendEvent("candidate",data);
    }

    @OnDisconnect
    public void disconectServer(SocketIOClient client){
        log.info("Disconnect Server: {}",client.getSessionId());
        webSocketService.delete(client.getSessionId().toString());
    }

    @PostConstruct
    public void startServer(){
        server.start();
        server.addListeners(this);
        log.info("Starting sever....");
    }
    @PreDestroy
    public void stopServer(){
        server.stop();
       log.info("Sever stopped");
    }
}
