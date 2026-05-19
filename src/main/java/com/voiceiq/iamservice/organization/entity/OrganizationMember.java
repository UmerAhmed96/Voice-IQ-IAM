package com.voiceiq.iamservice.organization.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "organization_members")
@Getter
@Setter
public class OrganizationMember extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt = Instant.now();
}