package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TranscriptSearchRequest {
    private String query;
    private UUID sessionId;
    private UUID speakerId;
    private String sentiment;
    private Long startTimeMs;
    private Long endTimeMs;
    private UUID userId;
}