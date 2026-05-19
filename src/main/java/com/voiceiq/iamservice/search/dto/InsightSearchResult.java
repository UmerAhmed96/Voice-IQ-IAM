package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class InsightSearchResult {
    private UUID sessionId;
    private String sessionTitle;
    private String flagType;
    private Integer severity;
    private String description;
    private Double score;
    private List<String> highlights;
    private Instant createdAt;
    private String organizationName;
    private Double relevanceScore;
}