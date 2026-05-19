package com.voiceiq.iamservice.admin.controller;

import com.voiceiq.iamservice.admin.dto.*;
import com.voiceiq.iamservice.admin.service.AdminService;
import com.voiceiq.iamservice.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Administrative endpoints for system monitoring and management")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ORG_ADMIN')")
public class AdminController {
    
    private final AdminService adminService;
    
    @Operation(summary = "Get system health and statistics")
    @GetMapping("/system/health")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SystemHealthResponse>> getSystemHealth(
            HttpServletRequest httpRequest) {
        
        SystemHealthResponse response = adminService.getSystemHealth();
        ApiResponse<SystemHealthResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get processing job statistics")
    @GetMapping("/processing/stats")
    public ResponseEntity<ApiResponse<ProcessingStatsResponse>> getProcessingStats(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(defaultValue = "30") Integer days,
            HttpServletRequest httpRequest) {
        
        ProcessingStatsResponse response = adminService.getProcessingStats(organizationId, days);
        ApiResponse<ProcessingStatsResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "List processing jobs with filters")
    @GetMapping("/processing/jobs")
    public ResponseEntity<ApiResponse<Page<ProcessingJobSummary>>> getProcessingJobs(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        
        Page<ProcessingJobSummary> response = adminService.getProcessingJobs(
            organizationId, status, startDate, endDate, pageable);
        
        ApiResponse<Page<ProcessingJobSummary>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Retry failed processing job")
    @PostMapping("/processing/jobs/{jobId}/retry")
    public ResponseEntity<ApiResponse<Void>> retryProcessingJob(
            @PathVariable UUID jobId,
            HttpServletRequest httpRequest) {
        
        adminService.retryProcessingJob(jobId);
        
        ApiResponse<Void> apiResponse = ApiResponse.success(
            null, "Processing job retry initiated successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get audit logs with search and filters")
    @GetMapping("/audit-logs")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> getAuditLogs(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        
        Page<AuditLogResponse> response = adminService.getAuditLogs(
            userId, organizationId, resourceType, action, startDate, endDate, pageable);
        
        ApiResponse<Page<AuditLogResponse>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get usage summary for organization")
    @GetMapping("/usage/summary")
    public ResponseEntity<ApiResponse<UsageSummaryResponse>> getUsageSummary(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(defaultValue = "30") Integer days,
            HttpServletRequest httpRequest) {
        
        UsageSummaryResponse response = adminService.getUsageSummary(organizationId, days);
        ApiResponse<UsageSummaryResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get user activity summary")
    @GetMapping("/users/activity")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserActivitySummary>>> getUserActivity(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(defaultValue = "7") Integer days,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        
        Page<UserActivitySummary> response = adminService.getUserActivity(
            organizationId, days, pageable);
        
        ApiResponse<Page<UserActivitySummary>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "View organization sessions (admin)")
    @GetMapping("/organizations/{orgId}/sessions")
    public ResponseEntity<ApiResponse<Page<AdminSessionSummary>>> getOrganizationSessions(
            @PathVariable UUID orgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        
        Page<AdminSessionSummary> response = adminService.getOrganizationSessions(
            orgId, status, startDate, endDate, pageable);
        
        ApiResponse<Page<AdminSessionSummary>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Run system maintenance and cleanup")
    @PostMapping("/maintenance/cleanup")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<MaintenanceResult>> runMaintenance(
            @RequestBody MaintenanceRequest request,
            HttpServletRequest httpRequest) {
        
        MaintenanceResult response = adminService.runMaintenance(request);
        
        ApiResponse<MaintenanceResult> apiResponse = ApiResponse.success(
            response, "Maintenance task completed");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}