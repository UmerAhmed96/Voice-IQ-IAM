package com.voiceiq.iamservice.session.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ShareSessionRequest {
    
    @NotNull(message = "Target type is required")
    private TargetType targetType;
    
    @NotNull(message = "Target ID is required")
    private UUID targetId;
    
    @NotNull(message = "Access level is required")
    private AccessLevel accessLevel;
    
    private Instant expiresAt;
    
    public enum TargetType {
        USER, TEAM, ORGANIZATION
    }
    
    public enum AccessLevel {
        OWNER, EDITOR, VIEWER, REPORT_VIEWER
    }
}