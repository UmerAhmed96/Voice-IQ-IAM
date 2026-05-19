package com.voiceiq.iamservice.search.dto;

import com.voiceiq.iamservice.session.entity.SessionStatus;
import com.voiceiq.iamservice.session.entity.SourceType;
import javax.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class SessionSearchRequest {
    
    @Size(max = 1000, message = "Query must not exceed 1000 characters")
    private String query;
    
    private UUID organizationId;
    private UUID teamId;
    private UUID userId;
    private List<SessionStatus> statuses;
    private List<SourceType> sourceTypes;
    private List<String> languageCodes;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minDuration; // seconds
    private Integer maxDuration; // seconds
    private Boolean hasTranscript;
    private Boolean hasInsights;
    private Boolean hasFactCheck;
}