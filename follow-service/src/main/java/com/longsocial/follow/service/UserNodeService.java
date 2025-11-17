package com.longsocial.follow.service;

import com.longsocial.follow.dto.request.RelationDeleteRequest;
import com.longsocial.follow.dto.request.RelationRequest;
import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.*;
import com.longsocial.follow.entity.UserNode;
import com.longsocial.follow.exception.AppException;
import com.longsocial.follow.exception.ErrorCode;
import com.longsocial.follow.mapper.UserNodeMapper;
import com.longsocial.follow.repository.UserNodeRepository;
import com.longsocial.follow.repository.client.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserNodeService {
    UserNodeRepository userNodeRepository;
    UserNodeMapper userNodeMapper;
    IdentityClient identityClient;

    public UserNodeResponse createUserNode(UserNodeRequest request) {
        log.info(request.toString());
        var userNode = userNodeMapper.toUserNode(request);
        userNode = userNodeRepository.save(userNode);
        log.info(userNode.toString());
        return userNodeMapper.toUserNodeResponse(userNode);
    }

    public UserNodeResponse getNode(String userId) {
        return userNodeMapper.toUserNodeResponse(userNodeRepository.findByUserId(userId));
    }



    // testing follow
//
//        // Chuyển đổi Map trả về từ query sang DTO


//    public String unfollow(RelationRequest request) {
//        userNodeRepository.deleteRelationship(request.getFollowerId(), request.getFolloweeId());
//        return "User " + request.getFollowerId() + " has just unFollowed " +
//                request.getFolloweeId() + " at " + request.getFollowTimestamp();
//    }

    public void createNode() {

    }

    //Update Generation in case
    public RelationResponse creationRelation(RelationRequest request) {
        var fromId = request.getFromNodeId();
        var toId = request.getToNodeId();
        var type = request.getType();
        RelationProjection  result = null;
        switch (request.getType()) {
            case "FATHER_SON":
                result = userNodeRepository.createFatherSonRelationship(fromId, toId);
                break;
            case "MOTHER_SON":
                result = userNodeRepository.createMotherSonRelationship(fromId, toId);
                break;
            case "SOUSE":
                result = userNodeRepository.createSouseRelationship(fromId, toId);
                break;
            case "BROTHER_SISTER":
                result = userNodeRepository.createBrotherSisterRelationship(fromId, toId);
                break;
            default:
                throw new IllegalArgumentException("Loại quan hệ không hợp lệ: " + type);

        }

        return RelationResponse.builder()
                .toNodeId(toId)
                .fromNodeId(fromId)
                .type(type)
                .id(result.getId())
                .build();
    }

    public TreeResponse getAllUserNode() {
        var nodes = userNodeRepository.findAllNodes();
        var relations=userNodeRepository.findAllRelationships();
        return TreeResponse.builder()
                .nodes(nodes.stream().map(userNodeMapper::toUserNodeResponse).toList())
                .relations(relations)
                .build();

    }
    public void deleteRelation(long relationId){
        userNodeRepository.deleteRelation(relationId);
    }

    public String updateUserNode(String id,UserNodeRequest request) {
        var userNode= userNodeRepository.findById(id);
        if(Objects.isNull(userNode))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        userNodeRepository.update(id,request.getName(),request.
                getDob(),request.getDod(), request.isSex());
        return "You have just updated userNode: ";
    }

    public void deleteNode(String id) {
        userNodeRepository.deleteById(id);
    }
}
