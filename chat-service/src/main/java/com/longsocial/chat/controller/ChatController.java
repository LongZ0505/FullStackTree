package com.longsocial.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longsocial.chat.dto.request.CreationChatMessageRequest;
import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.dto.response.ChatMessageResponse;
import com.longsocial.chat.service.ChatMessageService;
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
@RequestMapping("/chats")
public class ChatController {
    ChatMessageService chatMessageService;
    @GetMapping("/allChats/{conversationId}")
    public ApiResponse<List<ChatMessageResponse>> getChatsBySessionId(@PathVariable("conversationId") String conversationId){
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .result(chatMessageService.getByConversationId(conversationId))
                .build();
    }
    @GetMapping("/conversationId/{conversationId}")
    public ApiResponse<ChatMessageResponse> getLatestSession(@PathVariable("conversationId") String conversationId){
        return ApiResponse.<ChatMessageResponse>builder()
                .result(chatMessageService.getLatest(conversationId))
                .build();
    }

//    @PostMapping("/new")
//    public ApiResponse<?> newChat(@RequestBody CreationChatMessageRequest request) throws JsonProcessingException {
//       return ApiResponse.builder()
//               .result(chatMessageService.create(request))
//               .build();
//    }
}
