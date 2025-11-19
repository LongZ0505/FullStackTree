package com.longsocial.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationChatMessageRequest {
    String conversationId;
    String senderId;
    String message;
    Instant chatTimestamp;
}
