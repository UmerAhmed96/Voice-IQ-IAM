package com.voiceiq.iamservice.iam.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.iam.dto.*;
import com.voiceiq.iamservice.iam.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {
    
    private final AuthService authService;
    
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        
        AuthResponse response = authService.register(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.success(response, "User registered successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Authenticate user and return tokens")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        AuthResponse response = authService.login(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.success(response, "Login successful");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        ApiResponse<AuthResponse> apiResponse = ApiResponse.success(response, "Token refreshed successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Revoke refresh token")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        
        authService.logout(request.getRefreshToken());
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Logout successful");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Send password reset instruction/token")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            HttpServletRequest httpRequest) {
        
        // TODO: Implement password reset logic
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Password reset instructions sent");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Reset password using reset token")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest) {
        
        // TODO: Implement password reset logic
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Password reset successful");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Verify email address")
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request,
            HttpServletRequest httpRequest) {
        
        // TODO: Implement email verification logic
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Email verified successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}