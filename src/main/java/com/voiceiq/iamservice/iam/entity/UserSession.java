package com.voiceiq.iamservice.iam.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
public class UserSession extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id", nullable = false)
    private RefreshToken refreshToken;
    
    @Column(name = "device_name", length = 255)
    private String deviceName;
    
    @Column(name = "ip_address_hash", length = 64)
    private String ipAddressHash;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(name = "last_used_at", nullable = false)
    private Instant lastUsedAt;
    
    @Column(name = "revoked_at")
    private Instant revokedAt;
    
    @Column(name = "revoked_reason", length = 255)
    private String revokedReason;
}