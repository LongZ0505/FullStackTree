package com.group.post.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document(value = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @MongoId
    String postId;
    String postIdentifier;
    String imageUrl;
    String userId;
    Instant postDate;
    String title;
    String content;
//    String postAlt;
//    int postComments;
//    int postLikes;
//    boolean allowComment;
//    boolean allowLike;
//    public void increaseLikes(){
//        this.postLikes+=1;
//    }
//    public void increaseComments(){
//        this.postComments+=1;
//    }
//    public void decreaseLikes(){
//        this.postLikes-=1;
//    }
}
