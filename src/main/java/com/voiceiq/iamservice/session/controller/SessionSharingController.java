package com.voiceiq.iamservice.session.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.security.CustomAuthenticationDetails;
import com.voiceiq.iamservice.session.dto.SessionAccessResponse;
import com.voiceiq.iamservice.session.dto.ShareSessionRequest;
import com.voiceiq.iamservice.session.service.SessionAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Tag(name = "Session Sharing", description = "Session access control and sharing endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class SessionSharingController {
    
    private final SessionAccessService sessionAccessService;
    
    @Operation(summary = "Share session with user, team, or organization")
    @PostMapping("/{sessionId}/share")
    public ResponseEntity<ApiResponse<SessionAccessResponse>> shareSession(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ShareSessionRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        SessionAccessResponse response = sessionAccessService.shareSession(sessionId, request, details.getUserId());
        
        ApiResponse<SessionAccessResponse> apiResponse = ApiResponse.success(response, "Session shared successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get session access list")
    @GetMapping("/{sessionId}/access")
    public ResponseEntity<ApiResponse<List<SessionAccessResponse>>> getSessionAccess(
            @PathVariable UUID sessionId,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        List<SessionAccessResponse> accessList = sessionAccessService.getSessionAccess(sessionId, details.getUserId());
        
        ApiResponse<List<SessionAccessResponse>> apiResponse = ApiResponse.success(accessList);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Revoke session access")
    @DeleteMapping("/{sessionId}/access/{accessId}")
    public ResponseEntity<ApiResponse<Void>> revokeSessionAccess(
            @PathVariable UUID sessionId,
            @PathVariable UUID accessId,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        sessionAccessService.revokeSessionAccess(sessionId, accessId, details.getUserId());
        
        ApiResponse<Void> apiResponse = ApiResponse.success(null, "Session access revoked successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}