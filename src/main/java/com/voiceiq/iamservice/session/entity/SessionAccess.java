package com.voiceiq.iamservice.session.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "session_access")
@Getter
@Setter
public class SessionAccess extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private TargetType targetType;
    
    @Column(name = "target_id", nullable = false)
    private UUID targetId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false, length = 20)
    private AccessLevel accessLevel;
    
    @Column(name = "granted_by", nullable = false)
    private UUID grantedBy;
    
    @Column(name = "expires_at")
    private Instant expiresAt;
    
    @Column(name = "revoked_at")
    private Instant revokedAt;
    
    public enum TargetType {
        USER, TEAM, ORGANIZATION
    }
    
    public enum AccessLevel {
        OWNER, EDITOR, VIEWER, REPORT_VIEWER
    }
}