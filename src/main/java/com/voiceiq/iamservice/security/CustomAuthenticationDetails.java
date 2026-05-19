package com.voiceiq.iamservice.security;

import com.voiceiq.iamservice.iam.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CustomAuthenticationDetails {
    private final UUID userId;
    private final UserRole role;
    private final UUID organizationId;
}