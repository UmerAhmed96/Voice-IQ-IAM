package com.voiceiq.iamservice.search.dto;

import com.voiceiq.iamservice.session.entity.SessionStatus;
import com.voiceiq.iamservice.session.entity.SourceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SessionSearchResult {
    private UUID id;
    private String title;
    private String description;
    private SessionStatus status;
    private SourceType sourceType;
    private String languageCode;
    private Instant createdAt;
    private String organizationName;
    private String teamName;
    private String userEmail;
    private Integer durationSeconds;
    private List<String> highlights; // Matching text snippets
    private Double relevanceScore;
}