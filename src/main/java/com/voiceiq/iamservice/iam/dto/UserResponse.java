package com.voiceiq.iamservice.iam.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}