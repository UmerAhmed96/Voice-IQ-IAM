package com.voiceiq.iamservice.iam.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.iam.dto.PermissionResponse;
import com.voiceiq.iamservice.iam.dto.RoleResponse;
import com.voiceiq.iamservice.iam.dto.UserPermissionsResponse;
import com.voiceiq.iamservice.iam.service.RolePermissionService;
import com.voiceiq.iamservice.security.CustomAuthenticationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Roles and Permissions", description = "Role and permission management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class RolePermissionController {
    
    private final RolePermissionService rolePermissionService;
    
    @Operation(summary = "Get all available roles")
    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles(
            HttpServletRequest httpRequest) {
        
        List<RoleResponse> roles = rolePermissionService.getAllRoles();
        ApiResponse<List<RoleResponse>> apiResponse = ApiResponse.success(roles);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get all available permissions")
    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissions(
            HttpServletRequest httpRequest) {
        
        List<PermissionResponse> permissions = rolePermissionService.getAllPermissions();
        ApiResponse<List<PermissionResponse>> apiResponse = ApiResponse.success(permissions);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get current user's effective permissions")
    @GetMapping("/users/me/permissions")
    public ResponseEntity<ApiResponse<UserPermissionsResponse>> getCurrentUserPermissions(
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        UserPermissionsResponse permissions = rolePermissionService.getUserPermissions(details.getUserId());
        
        ApiResponse<UserPermissionsResponse> apiResponse = ApiResponse.success(permissions);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}