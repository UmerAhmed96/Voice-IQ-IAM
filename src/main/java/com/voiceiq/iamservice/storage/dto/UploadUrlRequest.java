package com.voiceiq.iamservice.storage.dto;

import javax.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class UploadUrlRequest {
    
    @NotBlank(message = "File name is required")
    @Size(max = 500, message = "File name must not exceed 500 characters")
    private String fileName;
    
    @NotBlank(message = "Content type is required")
    @Pattern(regexp = "audio/(mpeg|wav|mp4|m4a|flac|ogg)", 
             message = "Invalid audio content type. Supported: mp3, wav, mp4, m4a, flac, ogg")
    private String contentType;
    
    @NotNull(message = "File size is required")
    @Min(value = 1, message = "File size must be greater than 0")
    @Max(value = 500000000, message = "File size cannot exceed 500MB")
    private Long fileSizeBytes;
    
    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "Invalid SHA-256 checksum format")
    private String checksum;
    
    @Min(value = 1, message = "Duration must be greater than 0 seconds")
    @Max(value = 28800, message = "Duration cannot exceed 8 hours")
    private Integer durationSeconds;
    
    // Audio format validation
    public void validateAudioFormat() {
        List<String> supportedFormats = List.of(
            "audio/mpeg", "audio/wav", "audio/mp4", 
            "audio/m4a", "audio/flac", "audio/ogg"
        );
        
        if (!supportedFormats.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported audio format: " + contentType);
        }
        
        // File size validation based on format
        long maxSizeForFormat = getMaxSizeForFormat(contentType);
        if (fileSizeBytes > maxSizeForFormat) {
            throw new IllegalArgumentException(
                String.format("File size %d exceeds maximum for format %s: %d", 
                             fileSizeBytes, contentType, maxSizeForFormat));
        }
    }
    
    private long getMaxSizeForFormat(String contentType) {
        switch (contentType) {
            case "audio/mpeg":
                return 200_000_000L; // 200MB for MP3
            case "audio/wav":
                return 500_000_000L;  // 500MB for WAV
            case "audio/flac":
                return 400_000_000L; // 400MB for FLAC
            default:
                return 300_000_000L;           // 300MB for others
        }
    }
}