package com.voiceiq.iamservice.session.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AudioMetadataResponse {
    private String originalFilename;
    private String contentType;
    private Long fileSizeBytes;
    private Integer durationSeconds;
    private String storageKey;
    private String checksum;
    private Instant uploadCompletedAt;
    private Boolean isUploaded;
    private String status; // PENDING, UPLOADED, PROCESSING, COMPLETED, FAILED
}