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

import java.util.UUID;

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
    
    @Operation(summary = "Resend verification email")
    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationRequest request,
            HttpServletRequest httpRequest) {
        
        authService.resendVerificationEmail(request);
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Verification email sent if the account exists and is not already verified");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Change password")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        
        // TODO: Get user ID from JWT token
        UUID userId = UUID.randomUUID(); // Placeholder
        
        authService.changePassword(request, userId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Password changed successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get active sessions")
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<java.util.List<UserSessionResponse>>> getSessions(
            HttpServletRequest httpRequest) {
        
        // TODO: Get user ID from JWT token
        UUID userId = UUID.randomUUID(); // Placeholder
        
        java.util.List<UserSessionResponse> sessions = authService.getActiveSessions(userId);
        ApiResponse<java.util.List<UserSessionResponse>> apiResponse = ApiResponse.success(sessions, "Active sessions retrieved");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Revoke specific session")
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> revokeSession(
            @PathVariable UUID sessionId,
            HttpServletRequest httpRequest) {
        
        // TODO: Get user ID from JWT token
        UUID userId = UUID.randomUUID(); // Placeholder
        
        authService.revokeSession(sessionId, userId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Session revoked successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Logout from all devices")
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            HttpServletRequest httpRequest) {
        
        // TODO: Get user ID from JWT token
        UUID userId = UUID.randomUUID(); // Placeholder
        
        authService.logoutAll(userId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Logged out from all devices");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}