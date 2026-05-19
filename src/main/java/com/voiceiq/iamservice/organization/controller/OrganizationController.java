package com.voiceiq.iamservice.organization.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.organization.dto.OrganizationRequest;
import com.voiceiq.iamservice.organization.dto.OrganizationResponse;
import com.voiceiq.iamservice.organization.service.OrganizationService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization Management", description = "Organization CRUD and membership management")
@SecurityRequirement(name = "Bearer Authentication")
public class OrganizationController {
    
    private final OrganizationService organizationService;
    
    @Operation(summary = "Create new organization")
    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody OrganizationRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        OrganizationResponse response = organizationService.createOrganization(details.getUserId(), request);
        
        ApiResponse<OrganizationResponse> apiResponse = ApiResponse.success(response, "Organization created successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "List user's organizations")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getUserOrganizations(
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        List<OrganizationResponse> response = organizationService.getUserOrganizations(details.getUserId());
        
        ApiResponse<List<OrganizationResponse>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get organization details")
    @GetMapping("/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganization(
            @PathVariable UUID orgId,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        OrganizationResponse response = organizationService.getOrganization(details.getUserId(), orgId);
        
        ApiResponse<OrganizationResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Update organization")
    @PatchMapping("/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable UUID orgId,
            @Valid @RequestBody OrganizationRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        OrganizationResponse response = organizationService.updateOrganization(details.getUserId(), orgId, request);
        
        ApiResponse<OrganizationResponse> apiResponse = ApiResponse.success(response, "Organization updated successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}