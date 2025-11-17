package com.longsocial.follow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RelationDeleteRequest {
    String fromNodeId;
    String toNodeId;
    String type; // SOUSE, FATHER_SON, ...
}
