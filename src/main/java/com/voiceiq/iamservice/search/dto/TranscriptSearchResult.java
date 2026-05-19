package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TranscriptSearchResult {
    private UUID segmentId;
    private UUID sessionId;
    private String sessionTitle;
    private UUID speakerId;
    private String speakerName;
    private Long startMs;
    private Long endMs;
    private String text;
    private String sentimentLabel;
    private Double confidence;
    private List<String> highlights;
    private Double relevanceScore;
}