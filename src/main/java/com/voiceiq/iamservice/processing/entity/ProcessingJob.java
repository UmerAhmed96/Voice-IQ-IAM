package com.voiceiq.iamservice.processing.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "processing_jobs")
@Getter
@Setter
public class ProcessingJob extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType = "FULL_PROCESSING";
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.QUEUED;
    
    @Column(name = "started_at")
    private Instant startedAt;
    
    @Column(name = "completed_at")
    private Instant completedAt;
    
    @Column(name = "failed_at")
    private Instant failedAt;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;
    
    @Column(name = "external_job_id")
    private String externalJobId;
    
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    @Column(name = "max_retries")
    private Integer maxRetries = 3;
    
    @Column(name = "correlation_id")
    private String correlationId;
    
    @Column(name = "raw_response")
    private String rawResponse;
    
    @Column(name = "schema_version")
    private String schemaVersion = "1.0";
    
    public boolean canRetry() {
        return retryCount < maxRetries && (status == SessionStatus.FAILED);
    }
    
    public boolean isTerminal() {
        return status == SessionStatus.COMPLETED || status == SessionStatus.ARCHIVED ||
               (status == SessionStatus.FAILED && !canRetry());
    }
}