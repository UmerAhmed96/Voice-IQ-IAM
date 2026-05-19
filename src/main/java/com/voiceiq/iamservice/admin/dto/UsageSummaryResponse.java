package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageSummaryResponse {
    private Long totalSessions;
    private Long totalProcessingMinutes;
    private Long totalStorageBytes;
    private Long failedJobsCount;
    private Long reportsGenerated;
    private Long activeUsers;
    private Long totalInvitations;
    private Double averageSessionDuration;
    private String topProcessedLanguage;
    private Long totalFactsChecked;
}