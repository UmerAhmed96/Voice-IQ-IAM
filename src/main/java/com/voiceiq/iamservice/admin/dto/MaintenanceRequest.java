package com.voiceiq.iamservice.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class MaintenanceRequest {
    private List<String> tasks; // CLEANUP_EXPIRED_TOKENS, CLEANUP_OLD_AUDIT_LOGS, etc.
    private boolean dryRun = true;
    private Integer retentionDays = 90;
}