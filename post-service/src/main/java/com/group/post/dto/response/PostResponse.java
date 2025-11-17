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
    String postId;
    String postIdentifier;
    String imageUrl;
    int userId;
    String postDate;
    String postLocation;
    String postCaption;
    String postAlt;
    int postComments;
    int postLikes;
    boolean allowComment;
    boolean allowLike;
    String avatar;
    String userName;
}
