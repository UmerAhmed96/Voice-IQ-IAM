package com.voiceiq.iamservice.session.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.security.CustomAuthenticationDetails;
import com.voiceiq.iamservice.storage.dto.PresignedUploadUrl;
import com.voiceiq.iamservice.storage.dto.UploadUrlRequest;
import com.voiceiq.iamservice.session.dto.AudioUploadCompleteRequest;
import com.voiceiq.iamservice.session.dto.AudioMetadataResponse;
import com.voiceiq.iamservice.session.service.SessionAudioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Tag(name = "Session Audio Management", description = "Audio upload and management for conversation sessions")
@SecurityRequirement(name = "Bearer Authentication")
public class SessionAudioController {
    
    private final SessionAudioService sessionAudioService;
    
    @Operation(summary = "Generate presigned upload URL for audio file")
    @PostMapping("/{sessionId}/audio/upload-url")
    public ResponseEntity<ApiResponse<PresignedUploadUrl>> generateUploadUrl(
            @PathVariable UUID sessionId,
            @Valid @RequestBody UploadUrlRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        PresignedUploadUrl response = sessionAudioService.generateUploadUrl(
            sessionId, details.getUserId(), details.getOrganizationId(), request);
        
        ApiResponse<PresignedUploadUrl> apiResponse = ApiResponse.success(
            response, "Upload URL generated successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Mark audio upload as completed")
    @PostMapping("/{sessionId}/audio/complete")
    public ResponseEntity<ApiResponse<Void>> markUploadComplete(
            @PathVariable UUID sessionId,
            @Valid @RequestBody AudioUploadCompleteRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        sessionAudioService.markUploadComplete(sessionId, details.getUserId(), request);
        
        ApiResponse<Void> apiResponse = ApiResponse.success(
            null, "Audio upload completed successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get audio file metadata")
    @GetMapping("/{sessionId}/audio/metadata")
    public ResponseEntity<ApiResponse<AudioMetadataResponse>> getAudioMetadata(
            @PathVariable UUID sessionId,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        AudioMetadataResponse response = sessionAudioService.getAudioMetadata(
            sessionId, details.getUserId());
        
        ApiResponse<AudioMetadataResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get audio file download URL")
    @GetMapping("/{sessionId}/audio/download")
    public ResponseEntity<ApiResponse<String>> getDownloadUrl(
            @PathVariable UUID sessionId,
            @RequestParam(defaultValue = "3600") Integer expirationSeconds,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        String downloadUrl = sessionAudioService.getDownloadUrl(
            sessionId, details.getUserId(), expirationSeconds);
        
        ApiResponse<String> apiResponse = ApiResponse.success(
            downloadUrl, "Download URL generated successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Delete audio file")
    @DeleteMapping("/{sessionId}/audio")
    public ResponseEntity<ApiResponse<Void>> deleteAudioFile(
            @PathVariable UUID sessionId,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        sessionAudioService.deleteAudioFile(sessionId, details.getUserId());
        
        ApiResponse<Void> apiResponse = ApiResponse.success(
            null, "Audio file deleted successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}