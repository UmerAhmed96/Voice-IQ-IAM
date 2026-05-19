package com.voiceiq.iamservice.storage.service;

import com.voiceiq.iamservice.storage.dto.PresignedUploadUrl;
import com.voiceiq.iamservice.storage.dto.StorageMetadata;
import com.voiceiq.iamservice.storage.dto.UploadUrlRequest;

import java.time.Duration;
import java.util.UUID;

public interface StorageService {
    
    /**
     * Generate presigned upload URL for audio files
     */
    PresignedUploadUrl generateUploadUrl(UUID sessionId, UUID organizationId, UploadUrlRequest request);
    
    /**
     * Validate that file was uploaded successfully
     */
    void validateUpload(String storageKey, String expectedChecksum);
    
    /**
     * Generate signed download URL for file access
     */
    String getDownloadUrl(String storageKey, Duration expiration);
    
    /**
     * Delete file from storage
     */
    void deleteFile(String storageKey);
    
    /**
     * Get file metadata
     */
    StorageMetadata getFileMetadata(String storageKey);
    
    /**
     * Check if file exists
     */
    boolean fileExists(String storageKey);
}

