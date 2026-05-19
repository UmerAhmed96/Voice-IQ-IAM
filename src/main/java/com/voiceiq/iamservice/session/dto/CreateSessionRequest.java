package com.voiceiq.iamservice.session.dto;

import com.voiceiq.iamservice.session.entity.SourceType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSessionRequest {
    
    private UUID organizationId;
    
    private UUID teamId;
    
    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private SourceType sourceType = SourceType.AUDIO_UPLOAD;
    
    @Size(max = 10, message = "Language code must not exceed 10 characters")
    private String languageCode = "en";
    
    private Integer expectedSpeakers;
}