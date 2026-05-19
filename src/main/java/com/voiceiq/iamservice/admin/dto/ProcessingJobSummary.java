package com.voiceiq.iamservice.admin.dto;

import com.voiceiq.iamservice.session.entity.SessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ProcessingJobSummary {
    private UUID id;
    private UUID sessionId;
    private String sessionTitle;
    private UUID organizationId;
    private String organizationName;
    private UUID userId;
    private String userEmail;
    private SessionStatus status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private Integer retryCount;
    private String errorMessage;
    private Integer progressPercentage;
    private String externalJobId;
}