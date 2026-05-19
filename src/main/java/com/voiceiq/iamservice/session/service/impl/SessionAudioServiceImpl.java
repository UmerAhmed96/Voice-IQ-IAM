package com.voiceiq.iamservice.session.service.impl;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.session.dto.AudioMetadataResponse;
import com.voiceiq.iamservice.session.dto.AudioUploadCompleteRequest;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.repository.ConversationSessionRepository;
import com.voiceiq.iamservice.session.service.SessionAudioService;
import com.voiceiq.iamservice.storage.dto.PresignedUploadUrl;
import com.voiceiq.iamservice.storage.dto.UploadUrlRequest;
import com.voiceiq.iamservice.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SessionAudioServiceImpl implements SessionAudioService {
    
    private final ConversationSessionRepository sessionRepository;
    private final StorageService storageService;

    @Override
    public PresignedUploadUrl generateUploadUrl(UUID sessionId, UUID userId, UUID organizationId, UploadUrlRequest request) {
        ConversationSession session = findSessionWithOwnershipCheck(sessionId, userId);
        
        // TODO: Check if audio already uploaded
        // TODO: Validate user permissions
        
        return storageService.generateUploadUrl(sessionId, organizationId, request);
    }

    @Override
    public void markUploadComplete(UUID sessionId, UUID userId, AudioUploadCompleteRequest request) {
        ConversationSession session = findSessionWithOwnershipCheck(sessionId, userId);
        
        // TODO: Validate upload and update session status
        // TODO: Create AudioFile entity
        // TODO: Update session status to AUDIO_UPLOADED
        
        log.info("Marked upload complete for session: {}", sessionId);
    }

    @Override
    public AudioMetadataResponse getAudioMetadata(UUID sessionId, UUID userId) {
        ConversationSession session = findSessionWithOwnershipCheck(sessionId, userId);
        
        // TODO: Get audio file metadata from database
        
        return AudioMetadataResponse.builder()
                .isUploaded(false)
                .status("DRAFT")
                .build();
    }

    @Override
    public String getDownloadUrl(UUID sessionId, UUID userId, Integer expirationSeconds) {
        ConversationSession session = findSessionWithOwnershipCheck(sessionId, userId);
        
        // TODO: Get audio file storage key and generate download URL
        
        Duration expiration = Duration.ofSeconds(expirationSeconds != null ? expirationSeconds : 3600);
        return storageService.getDownloadUrl("dummy-storage-key", expiration);
    }

    @Override
    public void deleteAudioFile(UUID sessionId, UUID userId) {
        ConversationSession session = findSessionWithOwnershipCheck(sessionId, userId);
        
        // TODO: Delete audio file from storage and database
        
        log.info("Deleted audio file for session: {}", sessionId);
    }
    
    private ConversationSession findSessionWithOwnershipCheck(UUID sessionId, UUID userId) {
        return sessionRepository.findById(sessionId)
                .filter(session -> session.getUser().getId().equals(userId))
                .orElseThrow(() -> BusinessException.notFound("Session not found or access denied"));
    }
}