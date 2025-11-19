package com.longsocial.follow.mapper;

import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.UserNodeResponse;
import com.longsocial.follow.entity.UserNode;
import org.mapstruct.Mapper;

@Mapper
public interface UserNodeMapper {
    UserNode toUserNode(UserNodeRequest request);
    UserNodeResponse toUserNodeResponse(UserNode userNode);

}

