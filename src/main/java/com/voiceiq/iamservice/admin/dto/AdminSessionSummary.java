package com.voiceiq.iamservice.admin.dto;

import com.voiceiq.iamservice.session.entity.SessionStatus;
import com.voiceiq.iamservice.session.entity.SourceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class AdminSessionSummary {
    private UUID id;
    private String title;
    private UUID userId;
    private String userEmail;
    private UUID organizationId;
    private String organizationName;
    private UUID teamId;
    private String teamName;
    private SessionStatus status;
    private SourceType sourceType;
    private String languageCode;
    private Integer expectedSpeakers;
    private Boolean isArchived;
    private Instant createdAt;
    private Long fileSizeBytes;
    private Integer durationSeconds;
}