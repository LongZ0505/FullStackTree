package com.group.identity_service.mapper;

import com.group.identity_service.dto.request.CreationRequest;
import com.group.identity_service.dto.request.UserNodeRequest;
import com.group.identity_service.dto.response.CreationResponse;
import com.group.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdentityMapper {
    User toUser(CreationRequest request);
    CreationResponse toUserResponse(User user);
}
