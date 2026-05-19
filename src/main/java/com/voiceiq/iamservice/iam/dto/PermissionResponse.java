package com.voiceiq.iamservice.iam.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class PermissionResponse {
    private UUID id;
    private String name;
    private String description;
    private String resource;
    private String action;
    private Instant createdAt;
    private Instant updatedAt;
}