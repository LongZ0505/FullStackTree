package com.longsocial.follow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TreeResponse {
    List<UserNodeResponse> nodes;
    List<RelationResponse> relations;
}
