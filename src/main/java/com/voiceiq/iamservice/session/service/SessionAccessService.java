package com.voiceiq.iamservice.session.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.session.dto.SessionAccessResponse;
import com.voiceiq.iamservice.session.dto.ShareSessionRequest;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionAccess;
import com.voiceiq.iamservice.session.repository.ConversationSessionRepository;
import com.voiceiq.iamservice.session.repository.SessionAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionAccessService {
    
    private final SessionAccessRepository sessionAccessRepository;
    private final ConversationSessionRepository conversationSessionRepository;
    
    @Transactional
    public SessionAccessResponse shareSession(UUID sessionId, ShareSessionRequest request, UUID granterUserId) {
        ConversationSession session = conversationSessionRepository.findById(sessionId)
            .orElseThrow(() -> BusinessException.notFound("Session not found"));
        
        // Check if granter has permission to share
        if (!hasSharePermission(session, granterUserId)) {
            throw BusinessException.accessDenied("Insufficient permission to share session");
        }
        
        // Convert DTO enums to entity enums
        SessionAccess.TargetType targetType = SessionAccess.TargetType.valueOf(request.getTargetType().name());
        SessionAccess.AccessLevel accessLevel = SessionAccess.AccessLevel.valueOf(request.getAccessLevel().name());
        
        // Check for existing access
        sessionAccessRepository.findActiveAccess(sessionId, targetType, request.getTargetId(), Instant.now())
            .ifPresent(existing -> {
                throw BusinessException.conflict("Access already granted to this target");
            });
        
        // Create new access record
        SessionAccess access = new SessionAccess();
        access.setSession(session);
        access.setTargetType(targetType);
        access.setTargetId(request.getTargetId());
        access.setAccessLevel(accessLevel);
        access.setGrantedBy(granterUserId);
        access.setExpiresAt(request.getExpiresAt());
        
        access = sessionAccessRepository.save(access);
        
        log.info("Session {} shared with {} {} by user {}", 
                sessionId, targetType, request.getTargetId(), granterUserId);
        
        return mapToSessionAccessResponse(access);
    }
    
    public List<SessionAccessResponse> getSessionAccess(UUID sessionId, UUID requestingUserId) {
        ConversationSession session = conversationSessionRepository.findById(sessionId)
            .orElseThrow(() -> BusinessException.notFound("Session not found"));
        
        // Check if user has permission to view access list
        if (!hasViewAccessPermission(session, requestingUserId)) {
            throw BusinessException.accessDenied("Insufficient permission to view access list");
        }
        
        List<SessionAccess> accessList = sessionAccessRepository.findBySessionIdAndRevokedAtIsNull(sessionId);
        
        return accessList.stream()
            .map(this::mapToSessionAccessResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void revokeSessionAccess(UUID sessionId, UUID accessId, UUID requestingUserId) {
        ConversationSession session = conversationSessionRepository.findById(sessionId)
            .orElseThrow(() -> BusinessException.notFound("Session not found"));
        
        // Check if user has permission to revoke access
        if (!hasRevokePermission(session, requestingUserId)) {
            throw BusinessException.accessDenied("Insufficient permission to revoke access");
        }
        
        int updated = sessionAccessRepository.revokeAccess(accessId, sessionId, Instant.now());
        
        if (updated == 0) {
            throw BusinessException.notFound("Access record not found or already revoked");
        }
        
        log.info("Session access {} revoked for session {} by user {}", 
                accessId, sessionId, requestingUserId);
    }
    
    public boolean hasSessionAccess(UUID sessionId, UUID userId) {
        ConversationSession session = conversationSessionRepository.findById(sessionId)
            .orElse(null);
        
        if (session == null) {
            return false;
        }
        
        // Owner always has access
        if (session.getCreatedBy().equals(userId)) {
            return true;
        }
        
        // Check explicit access grants
        List<UUID> accessibleSessions = sessionAccessRepository.findAccessibleSessionIds(userId, Instant.now());
        return accessibleSessions.contains(sessionId);
    }
    
    private boolean hasSharePermission(ConversationSession session, UUID userId) {
        // Owner and editors can share
        if (session.getCreatedBy().equals(userId)) {
            return true;
        }
        
        // Check if user has EDITOR access
        return sessionAccessRepository.findActiveAccess(
                session.getId(), 
                SessionAccess.TargetType.USER, 
                userId, 
                Instant.now()
        ).map(access -> access.getAccessLevel() == SessionAccess.AccessLevel.EDITOR)
         .orElse(false);
    }
    
    private boolean hasViewAccessPermission(ConversationSession session, UUID userId) {
        // Owner can always view access list
        return session.getCreatedBy().equals(userId);
    }
    
    private boolean hasRevokePermission(ConversationSession session, UUID userId) {
        // Only owner can revoke access for now
        return session.getCreatedBy().equals(userId);
    }
    
    private SessionAccessResponse mapToSessionAccessResponse(SessionAccess access) {
        return SessionAccessResponse.builder()
            .accessId(access.getId())
            .sessionId(access.getSession().getId())
            .targetType(access.getTargetType().name())
            .targetId(access.getTargetId())
            .targetName("Unknown") // TODO: Resolve target name
            .accessLevel(access.getAccessLevel().name())
            .grantedBy(access.getGrantedBy())
            .grantedByName("Unknown") // TODO: Resolve granter name
            .expiresAt(access.getExpiresAt())
            .createdAt(access.getCreatedAt())
            .build();
    }
}