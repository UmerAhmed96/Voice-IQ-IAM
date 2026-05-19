package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserActivitySummary {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private UUID organizationId;
    private String organizationName;
    private Instant lastLoginAt;
    private Long sessionsCreated;
    private Long reportsGenerated;
    private Long totalLoginCount;
    private Boolean isActive;
}