package com.voiceiq.iamservice.factcheck.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "fact_check_jobs")
@Getter
@Setter
public class FactCheckJob extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.QUEUED;
    
    @Column(name = "started_at")
    private Instant startedAt;
    
    @Column(name = "completed_at")
    private Instant completedAt;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "schema_version", length = 10)
    private String schemaVersion = "1.0";
    
    @Column(name = "external_job_id")
    private String externalJobId;
    
    @Column(name = "claims_extracted_count")
    private Integer claimsExtractedCount = 0;
    
    @Column(name = "claims_verified_count")
    private Integer claimsVerifiedCount = 0;
    
    @Column(name = "correlation_id")
    private String correlationId;
}