package com.longsocial.chat.repository;

import com.longsocial.chat.configuration.WebIOConfig;
import com.longsocial.chat.entity.WebSocketSession;
import com.longsocial.chat.service.WebSocketService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession,String> {
    void deleteBySocketSessionId(String SessionId);
    @Query("{'userId' : {$in:?0}}")
    List<WebSocketSession> findAllByUserId(List<Integer> userIds);
}
