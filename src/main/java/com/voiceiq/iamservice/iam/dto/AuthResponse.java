package com.voiceiq.iamservice.iam.dto;

import com.voiceiq.iamservice.iam.entity.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UUID organizationId;
}