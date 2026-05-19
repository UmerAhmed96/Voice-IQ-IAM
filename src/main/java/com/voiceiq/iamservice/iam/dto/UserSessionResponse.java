package com.voiceiq.iamservice.iam.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserSessionResponse {
    private UUID sessionId;
    private String deviceName;
    private String ipAddress;
    private Instant lastUsedAt;
    private Instant createdAt;
    private boolean current;
}