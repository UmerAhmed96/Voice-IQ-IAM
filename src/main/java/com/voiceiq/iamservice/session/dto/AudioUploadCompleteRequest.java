package com.voiceiq.iamservice.session.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AudioUploadCompleteRequest {
    
    @NotBlank(message = "Storage key is required")
    private String storageKey;
    
    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "Invalid SHA-256 checksum format")
    private String actualChecksum;
    
    private Long actualFileSizeBytes;
    
    private Integer actualDurationSeconds;
}