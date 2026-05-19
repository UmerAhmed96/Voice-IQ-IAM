package com.voiceiq.iamservice.common.exception;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.common.response.ErrorDetail;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        List<ErrorDetail.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorDetail.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("VALIDATION_ERROR")
                .details(fieldErrors)
                .build();

        ApiResponse<Void> response = ApiResponse.error("Validation failed", errorDetail);
        response.setTraceId(getTraceId(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {
        
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("AUTHENTICATION_REQUIRED")
                .build();

        ApiResponse<Void> response = ApiResponse.error("Invalid credentials", errorDetail);
        response.setTraceId(getTraceId(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("ACCESS_DENIED")
                .build();

        ApiResponse<Void> response = ApiResponse.error("Access denied", errorDetail);
        response.setTraceId(getTraceId(request));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code(ex.getErrorCode())
                .build();

        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), errorDetail);
        response.setTraceId(getTraceId(request));

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Unexpected error occurred", ex);
        
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INTERNAL_ERROR")
                .build();

        ApiResponse<Void> response = ApiResponse.error("Internal server error", errorDetail);
        response.setTraceId(getTraceId(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String getTraceId(HttpServletRequest request) {
        return (String) request.getAttribute("traceId");
    }
}