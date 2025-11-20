package com.group.post.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String postIdentifier;
    String imageUrl;
    String userId;
    Instant postDate;
    String content;
    String title;
    String name;
}
