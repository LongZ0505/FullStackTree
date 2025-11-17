package com.longsocial.chat.mapper;

import com.longsocial.chat.dto.response.ConversationResponse;
import com.longsocial.chat.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper
public interface ConversationMapper {
    ConversationResponse toResponse(Conversation conversation);
}
