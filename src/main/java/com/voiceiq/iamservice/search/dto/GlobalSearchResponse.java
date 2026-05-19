package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GlobalSearchResponse {
    private String query;
    private Integer totalResults;
    private List<SessionSearchResult> sessions;
    private List<TranscriptSearchResult> transcripts;
    private List<InsightSearchResult> insights;
    private Long searchTimeMs;
}