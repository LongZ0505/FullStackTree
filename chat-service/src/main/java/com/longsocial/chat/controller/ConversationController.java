package com.longsocial.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longsocial.chat.dto.request.CreationConversationRequest;
import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.dto.response.ConversationResponse;
import com.longsocial.chat.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequestMapping("/conversations")
public class ConversationController {
    ConversationService conversationService;
    @GetMapping("/userId/{userId}")
    public ApiResponse<List<ConversationResponse>> fetchSessionsByUserId(@PathVariable("userId") String userId){
        return  ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationService.fetchSessionsByUserId(userId))
                .build();
    }

    @GetMapping("/conversationId/{conversationId}")
    public ApiResponse<ConversationResponse> getConversationById(@PathVariable("conversationId") String conversationId){
        log.info(conversationId);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.getConversationById(conversationId))
                .build();
    }


    @PostMapping("/conversation")
    public ApiResponse<?> createSession(@RequestBody CreationConversationRequest request) throws JsonProcessingException {
        log.info("request: {}",request);
            return ApiResponse.builder()
              .result(conversationService.create(request))
              .build();
    }
}
