package com.voiceiq.iamservice.session.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SessionAccessResponse {
    private UUID accessId;
    private UUID sessionId;
    private String targetType;
    private UUID targetId;
    private String targetName; // User name, team name, etc.
    private String accessLevel;
    private UUID grantedBy;
    private String grantedByName;
    private Instant expiresAt;
    private Instant createdAt;
}