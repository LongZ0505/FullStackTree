package com.group.post.mapper;

import com.group.post.dto.request.CreationPostRequest;
import com.group.post.dto.request.UpdatePostRequest;
import com.group.post.dto.response.PostResponse;
import com.group.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(CreationPostRequest request);
    PostResponse toPostResponse(Post post);

    @Mapping(target = "postId", ignore = true)
    void toPostFromUpdatePostRequest(@MappingTarget Post post, UpdatePostRequest request);
}
