package com.longsocial.chat.exception;

import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.exception.AppException;
import com.longsocial.chat.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class HandlerException {
    private static final String MIN_VALUE="min";
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse>handlingMethodArgumentNotValidException (MethodArgumentNotValidException exception){
        ErrorCode errorCode= ErrorCode.valueOf(exception.getFieldError().getDefaultMessage());
        Map<String, Object> attributes;
        var constraintViolation =
                exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

        attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        ApiResponse response=ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes)
                        ?mapAtributes(errorCode.getMessage(),attributes)
                        : errorCode.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    private String mapAtributes(String message, Map<String, Object> attributes) {
        String minValue= String.valueOf(attributes.get(MIN_VALUE));

        return message.replace("{" + MIN_VALUE + "}", minValue);
    }

}
