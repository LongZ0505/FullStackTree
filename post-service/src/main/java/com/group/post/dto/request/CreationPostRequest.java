package com.group.post.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationPostRequest {
    String postIdentifier;
    String imageUrl;
    String userId;
    Instant postDate;
    String content;
    String title;
}
