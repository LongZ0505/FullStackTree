package com.group.post.controller;

import com.group.post.dto.request.CreationPostRequest;
import com.group.post.dto.request.UpdatePostRequest;
import com.group.post.dto.response.ApiResponse;
import com.group.post.dto.response.PostResponse;
import com.group.post.dto.response.GetPostResponse;
import com.group.post.service.PostService;
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
@RequestMapping("/posts")
public class PostController {
    PostService postService;

    @GetMapping("/getByUserID/{userId}")
    public ApiResponse<GetPostResponse> getPostsById(@PathVariable("userId") Integer userId) {

        return ApiResponse.<GetPostResponse>builder()
                .result(postService.getPosByUserId(userId))
                .build();
    }

    @GetMapping("/getByUserName/{userName}")
    public ApiResponse<GetPostResponse> getPostsByUserName(@PathVariable("userName") String userName) {
        return ApiResponse.<GetPostResponse>builder()
                .result(postService.getPosByUserName(userName))
                .build();
    }

    @GetMapping("/getPostByIdentifier/{identifier}")
    public ApiResponse<PostResponse> getPostByIdentifier(@PathVariable("identifier") String identifier) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostByIdentifier(identifier))
                .build();
    }

    @GetMapping("/getPostByPostId/{postId}")
    public ApiResponse<PostResponse> getPostByPostId(@PathVariable("postId") String postId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostByPostId(postId))
                .build();
    }

    @PostMapping("/newPost")
    public ApiResponse<PostResponse> newPost(
            @RequestBody CreationPostRequest request) {
        log.info(request.toString());
        return ApiResponse.<PostResponse>builder()
                .result(postService.newPost(request))
                .build();
    }

    @PutMapping("/updatePost")
    public ApiResponse<?> updatePost(@RequestBody UpdatePostRequest request) {
        return ApiResponse.builder()
                .message(postService.updatePost(request))
                .build();
    }

    @DeleteMapping("/deletePost/{postId}")
    public ApiResponse<?> deletePost(@PathVariable("postId") String postId) {
        return ApiResponse.builder()
                .message(postService.deletePost(postId))
                .build();
    }


    @GetMapping("/friendPosts")
    public ApiResponse<List<PostResponse>>
    getFriendPosts(@RequestParam("userId") Integer userId,
                   @RequestParam("startIndex") Integer startIndex,
                   @RequestParam("limit") Integer limit) {

        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostFollowees(userId, startIndex, limit))
                .build();
    }

    //  use pageable
    @GetMapping("/randomPost")
    public ApiResponse<List<PostResponse>> getSamplePostsExcludingSelf(@RequestParam("userId") Integer userId, @RequestParam("limit") Integer limit) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPostExcludingSelf(limit, userId))
                .build();
    }


    @GetMapping("/random/{limit}")
    public ApiResponse<List<PostResponse>> getRandomPosts(@PathVariable("limit") Integer limit){
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getRandomPosts(limit))
                .build();
    }

}
