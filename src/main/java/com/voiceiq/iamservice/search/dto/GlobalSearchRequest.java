package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GlobalSearchRequest {
    private String query;
    private String searchType; // sessions, transcripts, insights
    private UUID organizationId;
    private UUID teamId;
    private UUID userId;
    private Integer limit;
}