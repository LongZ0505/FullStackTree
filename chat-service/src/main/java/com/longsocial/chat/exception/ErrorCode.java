package com.longsocial.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1000,"User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1001,"User not existed", HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(1007,"Post not existed", HttpStatus.NOT_FOUND),
    LIKE_NOT_EXISTED(1008,"You've not liked this post", HttpStatus.NOT_FOUND),
    SAVE_NOT_EXISTED(1009,"You've not saved this post", HttpStatus.NOT_FOUND),
    TAG_NOT_EXISTED(1010,"You've not tag this post", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(8888,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1002,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_EXISTED(1003,"Role not existed", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(1004,"Password at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1005, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1006,"Email is required", HttpStatus.BAD_REQUEST),
    CONVERSATION_NOT_EXISTED(1011,"Conversation not existed", HttpStatus.NOT_FOUND),
    CHAT_NOT_EXISTED(1012,"Still have no chat", HttpStatus.NOT_FOUND)
    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
