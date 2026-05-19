package com.voiceiq.iamservice.organization.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "organization_invitations")
@Getter
@Setter
public class OrganizationInvitation extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by", nullable = false)
    private User invitedBy;
    
    @Column(name = "invitation_token", nullable = false, unique = true)
    private String invitationToken;
    
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    
    @Column(name = "is_accepted")
    private Boolean isAccepted = false;
    
    @Column(name = "accepted_at")
    private Instant acceptedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by")
    private User acceptedBy;
    
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !isAccepted && !isExpired();
    }
}