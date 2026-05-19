package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InsightSearchRequest {
    private String query;
    private String flagType;
    private Integer minSeverity;
    private Double minScore;
    private UUID organizationId;
    private UUID userId;
}