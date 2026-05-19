package com.voiceiq.iamservice.search.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class SaveSearchRequest {
    
    @NotBlank(message = "Search name is required")
    @Size(max = 255, message = "Search name must not exceed 255 characters")
    private String name;
    
    @NotBlank(message = "Query is required")
    @Size(max = 1000, message = "Query must not exceed 1000 characters")
    private String query;
    
    private String searchType;
    private String filters; // JSON string of filters
    private UUID userId; // Set by controller
}