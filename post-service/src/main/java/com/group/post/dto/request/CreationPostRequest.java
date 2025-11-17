package com.group.post.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationPostRequest {
    String postIdentifier;
    String imageUrl;
    int userId;
    Instant postDate;
    String postLocation;
    String postCaption;
    String postAlt;
    int postComments;
    int postLikes;
    boolean allowComment;
    boolean allowLike;

}
