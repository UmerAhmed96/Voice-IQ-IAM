package com.voiceiq.iamservice.organization.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class OrganizationResponse {
    private UUID id;
    private String name;
    private String description;
    private String domain;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Long memberCount;
}