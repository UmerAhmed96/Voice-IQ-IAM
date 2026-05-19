package com.voiceiq.iamservice.session.service;

import com.voiceiq.iamservice.session.dto.AudioMetadataResponse;
import com.voiceiq.iamservice.session.dto.AudioUploadCompleteRequest;
import com.voiceiq.iamservice.storage.dto.PresignedUploadUrl;
import com.voiceiq.iamservice.storage.dto.UploadUrlRequest;

import java.util.UUID;

public interface SessionAudioService {
    
    /**
     * Generate presigned upload URL for session audio
     */
    PresignedUploadUrl generateUploadUrl(UUID sessionId, UUID userId, UUID organizationId, 
                                        UploadUrlRequest request);
    
    /**
     * Mark audio upload as completed
     */
    void markUploadComplete(UUID sessionId, UUID userId, AudioUploadCompleteRequest request);
    
    /**
     * Get audio metadata for session
     */
    AudioMetadataResponse getAudioMetadata(UUID sessionId, UUID userId);
    
    /**
     * Generate download URL for audio file
     */
    String getDownloadUrl(UUID sessionId, UUID userId, Integer expirationSeconds);
    
    /**
     * Delete audio file from storage
     */
    void deleteAudioFile(UUID sessionId, UUID userId);
}