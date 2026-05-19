package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class MaintenanceResult {
    private boolean success;
    private Instant executedAt;
    private Map<String, Long> deletedCounts; // task -> count deleted
    private List<String> errors;
    private Long totalExecutionTimeMs;
}