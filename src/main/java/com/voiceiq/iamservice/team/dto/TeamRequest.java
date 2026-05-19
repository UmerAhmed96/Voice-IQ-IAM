package com.voiceiq.iamservice.team.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamRequest {
    
    @NotBlank(message = "Team name is required")
    @Size(max = 255, message = "Team name must not exceed 255 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}