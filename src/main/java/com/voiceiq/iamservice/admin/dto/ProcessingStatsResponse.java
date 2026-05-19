package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessingStatsResponse {
    private Long totalJobsProcessed;
    private Long successfulJobs;
    private Long failedJobs;
    private Long jobsInProgress;
    private Long jobsQueued;
    private Double averageProcessingTimeMinutes;
    private Double totalProcessingHours;
    private Long audioFilesProcessed;
    private Long totalAudioDurationHours;
}