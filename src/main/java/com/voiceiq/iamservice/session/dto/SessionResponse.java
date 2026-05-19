package com.voiceiq.iamservice.session.dto;

import com.voiceiq.iamservice.session.entity.SessionStatus;
import com.voiceiq.iamservice.session.entity.SourceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SessionResponse {
    private UUID id;
    private UUID userId;
    private UUID organizationId;
    private UUID teamId;
    private String title;
    private String description;
    private SourceType sourceType;
    private String languageCode;
    private Integer expectedSpeakers;
    private SessionStatus status;
    private Boolean isArchived;
    private Instant createdAt;
    private Instant updatedAt;
    
    // Audio file info if available
    private String originalFilename;
    private String contentType;
    private Long fileSizeBytes;
    private Integer durationSeconds;
    private Instant uploadCompletedAt;
}