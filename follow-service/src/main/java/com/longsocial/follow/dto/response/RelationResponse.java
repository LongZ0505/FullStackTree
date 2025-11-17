package com.longsocial.follow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RelationResponse {
     String id;
     String fromNodeId;
     String toNodeId;
     String type;
}
