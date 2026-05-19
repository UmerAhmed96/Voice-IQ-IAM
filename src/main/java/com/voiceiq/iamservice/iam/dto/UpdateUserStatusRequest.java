package com.voiceiq.iamservice.iam.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    
    @NotNull(message = "Status is required")
    private UserStatus status;
    
    public enum UserStatus {
        ACTIVE, SUSPENDED, DISABLED
    }
}