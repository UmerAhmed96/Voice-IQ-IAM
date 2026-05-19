package com.voiceiq.iamservice.common.entity;

import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.organization.entity.Organization;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    @Column(name = "resource_type", nullable = false, length = 100)
    private String resourceType;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;
    
    @Column(name = "action", nullable = false, length = 100)
    private String action;
    
    @Column(name = "old_values")
    private String oldValues;
    
    @Column(name = "new_values")
    private String newValues;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}