package com.voiceiq.iamservice.iam.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RoleResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isSystemRole;
    private List<String> permissions;
    private Instant createdAt;
    private Instant updatedAt;
}