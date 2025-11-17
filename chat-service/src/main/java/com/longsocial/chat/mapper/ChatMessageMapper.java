package com.longsocial.chat.mapper;

import com.longsocial.chat.dto.response.ChatMessageResponse;
import com.longsocial.chat.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper
public interface ChatMessageMapper {
    ChatMessageResponse toResponse(ChatMessage chatMessage);
}
