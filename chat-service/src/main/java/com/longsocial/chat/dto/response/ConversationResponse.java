package com.longsocial.chat.dto.response;

import com.longsocial.chat.entity.ParticipantInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    List<ParticipantInfo> participants;
    String messageDigestion;
    Instant updateTime;
}
