package com.voiceiq.iamservice.storage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PresignedUploadUrl {
    private String uploadUrl;
    private String storageKey;
    private Integer expiresInSeconds;
    private String uploadMethod; // PUT or POST
    
    // Additional fields for POST uploads (multipart)
    private String formAction;
    private java.util.Map<String, String> formFields;
}