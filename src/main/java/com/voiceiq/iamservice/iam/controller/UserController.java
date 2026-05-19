package com.voiceiq.iamservice.iam.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.iam.dto.UpdateUserRequest;
import com.voiceiq.iamservice.iam.dto.UserResponse;
import com.voiceiq.iamservice.iam.service.UserService;
import com.voiceiq.iamservice.security.CustomAuthenticationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        UserResponse response = userService.getCurrentUser(details.getUserId());
        
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Update current user profile")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        UserResponse response = userService.updateCurrentUser(details.getUserId(), request);
        
        ApiResponse<UserResponse> apiResponse = ApiResponse.success(response, "User profile updated successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}