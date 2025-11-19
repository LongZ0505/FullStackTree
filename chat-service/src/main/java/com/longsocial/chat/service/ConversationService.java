package com.longsocial.chat.service;

import com.longsocial.chat.dto.request.CreationConversationRequest;
import com.longsocial.chat.dto.response.ChatPageResponse;
import com.longsocial.chat.dto.response.ConversationResponse;
import com.longsocial.chat.entity.Conversation;
import com.longsocial.chat.entity.ParticipantInfo;
import com.longsocial.chat.exception.AppException;
import com.longsocial.chat.exception.ErrorCode;
import com.longsocial.chat.mapper.ConversationMapper;
import com.longsocial.chat.repository.ConversationRepository;
import com.longsocial.chat.repository.httpClient.IdentityClient;
import com.longsocial.chat.repository.httpClient.UserNodeClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ConversationService {
    ConversationRepository conversationRepository;
    UserNodeClient userNodeClient;
    ConversationMapper conversationMapper;
    ChatMessageService chatMessageService;
    public String create(CreationConversationRequest request) {
        var userA=userNodeClient.getNode(request.getParticipantIds().getFirst());
        var userB=userNodeClient.getNode(request.getParticipantIds().get(1));
        if(Objects.isNull(userB)||Objects.isNull(userA))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var userAInfor=userA.getResult();
        var userBInfor=userB.getResult();
        log.info("userA: {}",userA);
        log.info("userB: {}",userB);
        List<String> userIds = new ArrayList<>();
        userIds.add(userAInfor.getUserId());
        userIds.add(userBInfor.getUserId());

        var sortedIds = userIds.stream().sorted().toList();
        String userIdHash = generateParticipantHash(sortedIds);
       var con=conversationRepository.findByParticipantsHash(userIdHash)
                .orElseGet(()->{ List<ParticipantInfo> participantInfos = List.of(
                        ParticipantInfo.builder()
                                .userId(userAInfor.getUserId())
                                .name(userAInfor.getName())
                                .build(),
                        ParticipantInfo.builder()
                                .userId(userBInfor.getUserId())
                                .name(userBInfor.getName())
                                .build()
                );
        Conversation newConversation = Conversation.builder()
                .participantsHash(userIdHash)
                .createdDate(Instant.now())
                .participants(participantInfos)
                .build();
        return conversationRepository.save(newConversation);});
       return con.getId();
    }
    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        // SHA 256
        return stringJoiner.toString();
    }

    public ConversationResponse getConversationById(String  conversationId) {
        var conversation=conversationRepository.findById(conversationId).
                orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(conversation.getParticipants())
                .build();
    }

    public List<ConversationResponse> fetchSessionsByUserId(String userId) {
        var check = userNodeClient.getNode(userId);
        if(Objects.isNull(check)) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        var lstConversation=conversationRepository.findByParticipantsUserId(userId);
        return lstConversation.stream().map(conversation -> {
            var response= conversationMapper.toResponse(conversation);
            log.info(conversation.getId());
            var latest =chatMessageService.getLatest(conversation.getId());
           if(!Objects.isNull(latest)){
               response.setMessageDigestion(latest.getMessage());
               response.setUpdateTime(latest.getChatTimestamp());
           }
           return response;
        }).toList();
    }
}
