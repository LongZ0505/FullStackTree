package com.group.post.dto.response;

import com.group.post.entity.Post;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GetPostResponse {
    List<Post> listPost;
}
