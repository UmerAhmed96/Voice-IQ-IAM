package com.voiceiq.iamservice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(message, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public static BusinessException conflict(String message) {
        return new BusinessException(message, "DUPLICATE_RESOURCE", HttpStatus.CONFLICT);
    }

    public static BusinessException invalidState(String message) {
        return new BusinessException(message, "INVALID_STATE_TRANSITION", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static BusinessException unauthorized(String message) {
        return new BusinessException(message, "AUTHENTICATION_REQUIRED", HttpStatus.UNAUTHORIZED);
    }

    public static BusinessException accessDenied(String message) {
        return new BusinessException(message, "ACCESS_DENIED", HttpStatus.FORBIDDEN);
    }
}