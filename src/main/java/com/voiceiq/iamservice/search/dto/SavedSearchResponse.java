package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class SavedSearchResponse {
    private UUID id;
    private String name;
    private String query;
    private String searchType;
    private String filters;
    private Instant createdAt;
    private Instant lastUsedAt;
    private Integer usageCount;
}