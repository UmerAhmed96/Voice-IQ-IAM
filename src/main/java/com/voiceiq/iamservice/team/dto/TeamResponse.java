package com.voiceiq.iamservice.team.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class TeamResponse {
    private UUID id;
    private UUID organizationId;
    private String name;
    private String description;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Long memberCount;
}