package com.voiceiq.iamservice.iam.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserPermissionsResponse {
    private UUID userId;
    private UUID organizationId;
    private String role;
    private List<String> permissions;
}