package com.voiceiq.iamservice.iam.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyEmailRequest {
    
    @NotBlank(message = "Verification token is required")
    private String token;
}