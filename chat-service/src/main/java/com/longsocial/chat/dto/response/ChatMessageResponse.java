package com.longsocial.chat.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String chatId;
    String conversationId;
    Integer userId;
    String message;
    Instant chatTimestamp;
}
