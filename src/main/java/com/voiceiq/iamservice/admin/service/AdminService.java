package com.voiceiq.iamservice.admin.service;

import com.voiceiq.iamservice.admin.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface AdminService {
    
    /**
     * Get overall system health and metrics
     */
    SystemHealthResponse getSystemHealth();
    
    /**
     * Get processing job statistics
     */
    ProcessingStatsResponse getProcessingStats(UUID organizationId, Integer days);
    
    /**
     * Get paginated list of processing jobs with filters
     */
    Page<ProcessingJobSummary> getProcessingJobs(UUID organizationId, String status, 
                                                 LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Retry a failed processing job
     */
    void retryProcessingJob(UUID jobId);
    
    /**
     * Get audit logs with search and filters
     */
    Page<AuditLogResponse> getAuditLogs(UUID userId, UUID organizationId, String resourceType, 
                                       String action, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Get usage summary for organization
     */
    UsageSummaryResponse getUsageSummary(UUID organizationId, Integer days);
    
    /**
     * Get user activity summary
     */
    Page<UserActivitySummary> getUserActivity(UUID organizationId, Integer days, Pageable pageable);
    
    /**
     * Get organization sessions for admin view
     */
    Page<AdminSessionSummary> getOrganizationSessions(UUID organizationId, String status, 
                                                      LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Run system maintenance tasks
     */
    MaintenanceResult runMaintenance(MaintenanceRequest request);
}