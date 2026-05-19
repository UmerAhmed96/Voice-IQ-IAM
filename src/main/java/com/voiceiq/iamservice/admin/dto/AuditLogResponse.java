package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AuditLogResponse {
    private UUID id;
    private UUID userId;
    private String userEmail;
    private UUID organizationId;
    private String organizationName;
    private String resourceType;
    private UUID resourceId;
    private String action;
    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;
    private String ipAddress;
    private String userAgent;
    private Instant createdAt;
}