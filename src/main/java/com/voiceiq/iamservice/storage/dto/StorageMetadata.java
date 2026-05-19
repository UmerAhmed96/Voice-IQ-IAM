package com.voiceiq.iamservice.storage.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class StorageMetadata {
    private String storageKey;
    private long fileSizeBytes;
    private String contentType;
    private String checksum;
    private Instant lastModified;
}