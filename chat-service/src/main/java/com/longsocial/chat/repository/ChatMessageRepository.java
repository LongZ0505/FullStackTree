package com.longsocial.chat.repository;

import com.longsocial.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    @Query(value = "{ 'conversationId' : ?0 }", sort = "{ 'chatTimestamp' : 1 }")
    List<ChatMessage> findByConversationIdOrderByChatTimestampDesc(String conversationId);
    @Query(value = "{ 'conversationId' : ?0 }", sort = "{ 'chatTimestamp' : -1 }")
    List<ChatMessage> findLatestMessage(String conversationId);
}
