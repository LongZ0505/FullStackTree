package com.group.post.service;

import com.group.post.dto.request.CreationPostRequest;
import com.group.post.dto.request.UpdatePostRequest;
import com.group.post.dto.response.*;
import com.group.post.entity.Post;
import com.group.post.exception.AppException;
import com.group.post.exception.ErrorCode;
import com.group.post.mapper.PostMapper;
import com.group.post.repository.PostRepository;
import com.group.post.repository.httpClient.FollowClient;
import com.group.post.repository.httpClient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserClient userClient;
    FollowClient followClient;
    DateTimeFormatter dateTimeFormatter;
   // SaveService saveService;

    public GetPostResponse getPosByUserId(Integer userId) {
        return GetPostResponse.builder()
                .listPost(postRepository.findALlByUserId(userId))
                .build();
    }

    public GetPostResponse getPosByUserName(String userName) {
        var userResponse = userClient.getUserByUserName(userName);
        var user = userResponse.getResult();
        if (Objects.isNull(user))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        return GetPostResponse.builder()
                .listPost(postRepository.findALlByUserId(user.getUserId()))
                .build();
    }

    public PostResponse newPost(CreationPostRequest request) {
        var userId=Integer.parseInt(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var check=userClient.getUserById(userId);
        if(Objects.isNull(check))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        if(request.getUserId()!=userId)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var post = postMapper.toPost(request);
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PostResponse getPostByIdentifier(String identifier) {
        return postMapper.toPostResponse(postRepository.findByPostIdentifier(identifier));
    }

    public PostResponse getPostByPostId(@PathVariable("postId") String postId) {
        var post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_EXISTED));
        var userResponse = userClient.getUserById(post.getUserId());
        var user = userResponse.getResult();
        if (Objects.isNull(user))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        var postResponse = postMapper.toPostResponse(post);
        postResponse.setAvatar(user.getAvatar());
        postResponse.setUserName(user.getUserName());
        return postResponse;

    }

    public Post updateLike(String postId) {
        var post = postRepository.findById(postId).orElseThrow
                (() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        post.increaseLikes();
        return postRepository.save(post);
    }

    public Post updateComment(String postId) {
        var post = postRepository.findById(postId).orElseThrow
                (() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        post.increaseComments();
        return postRepository.save(post);
    }

    public Post updateUnLike(String postId) {
        var post = postRepository.findById(postId).orElseThrow
                (() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        post.decreaseLikes();
        return postRepository.save(post);
    }

    public String updatePost(UpdatePostRequest request) {
        var userId=Integer.parseInt(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        var check=userClient.getUserById(userId);
        if(Objects.isNull(check))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        var post = postRepository.findById(request.getPostId()).orElseThrow
                (() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        if(post.getUserId()!=userId)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        postMapper.toPostFromUpdatePostRequest(post, request);
        postRepository.save(post);
        return "You have just completed to update your post";
    }

    public String deletePost(String postId) {
        var post = postRepository.findById(postId).orElseThrow
                (() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postRepository.delete(post);
        return "You have just completed to delete your post";
    }

    public List<PostResponse> getPostFollowees(Integer userId,
                                               Integer startIndex, Integer limit) {
        var lstFollowees = followClient.getFollowees(userId);
        var lstIdFollowees = lstFollowees.getResult().stream().map(UserNodeResponse::getUserId).toList();
        log.info(lstIdFollowees.toString());
        Pageable pageable = PageRequest.of(startIndex, limit, Sort.by(Sort.Direction.DESC, "postDate"));
        var lstPagePost = postRepository.findPostPageable(lstIdFollowees, pageable);
        log.info("list Post of Followees: {}", lstPagePost.getContent().toString());
        return lstPagePost.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setPostDate(dateTimeFormatter.format(post.getPostDate()));
            postResponse.setAvatar(userClient.getUserById(post.
                    getUserId()).getResult().getAvatar());
            postResponse.setUserName(userClient.getUserById(post.
                    getUserId()).getResult().getUserName());
            return postResponse;
        }).toList();
    }

    public List<PostResponse> getAllPostExcludingSelf(Integer limit, Integer userId) {
        var lstUser = userClient.getAllExcludingSelf(10, userId).getResult();
        var lstId = lstUser.stream().map(UserResponse::getUserId).toList();
        Pageable pageable = PageRequest.of(0, limit, Sort.Direction.DESC,
                "postDate");
        var lstPagePost = postRepository.
                findPostPageable(lstId, pageable);
        return lstPagePost.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setPostDate(dateTimeFormatter.format(post.getPostDate()));
            postResponse.setAvatar(userClient.getUserById(post.
                    getUserId()).getResult().getAvatar());
            postResponse.setUserName(userClient.getUserById(post.
                    getUserId()).getResult().getUserName());
            return postResponse;
        }).toList();
    }

//    public List<PostResponse> getFavoritePosts(Integer userId, Integer startIndex, Integer limit) {
//        var lstSaved = saveService.getSavedPosts(userId);
//        log.info(lstSaved.toString());
//        var lstPostIds = lstSaved.stream().map(saveResponse -> saveResponse.getPostId()).toList();
//        Pageable pageable = PageRequest.of(startIndex, limit, Sort.Direction.DESC, "postDate");
//        var lstPostPage = postRepository.findAllById(lstPostIds, pageable);
//
//        return lstPostPage.getContent().stream().map(post -> {
//            var postResponse = postMapper.toPostResponse(post);
//            postResponse.setPostDate(dateTimeFormatter.format(post.getPostDate()));
//            postResponse.setAvatar(userClient.getUserById(post.
//                    getUserId()).getResult().getAvatar());
//            postResponse.setUserName(userClient.getUserById(post.
//                    getUserId()).getResult().getUserName());
//            return postResponse;
//        }).toList();

 //   }

    public List<PostResponse> getRandomPosts(Integer limit) {
        var lstUser = userClient.getRandomUsers(limit).getResult();
        var lstUserId = lstUser.stream().map(UserResponse::getUserId).toList();
        Pageable pageable = PageRequest.of(0, limit, Sort.Direction.DESC, "postDate");
        var lstPostPageable = postRepository.findPostPageable(lstUserId, pageable);
        return lstPostPageable.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setAvatar(userClient.getUserById(post.getUserId()).getResult().getAvatar());
            postResponse.setUserName(userClient.getUserById(post.getUserId()).getResult().getUserName());
            return postResponse;
        }).toList();
    }

//    public List<PostResponse> getSavedPosts(Integer userId) {
//        var lstsaved = saveService.getSavedPosts(userId);
//        log.info(lstsaved.toString());
//        var lstPostId = lstsaved.stream().map(SaveResponse::getPostId).toList();
//        var result= postRepository.findByListId(lstPostId).stream()
//                .map(postMapper::toPostResponse).toList();
//        log.info(result.toString());
//        return result;
//    }
//
//    ;
}
