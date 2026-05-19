package com.voiceiq.iamservice.report.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;
    
    @Column(name = "title", nullable = false, length = 500)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.QUEUED;
    
    @Column(name = "file_format", length = 10)
    private String fileFormat = "PDF";
    
    @Column(name = "storage_key", length = 1000)
    private String storageKey;
    
    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;
    
    @Column(name = "generated_at")
    private Instant generatedAt;
    
    @Column(name = "expires_at")
    private Instant expiresAt;
    
    @Column(name = "download_count")
    private Integer downloadCount = 0;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "template_version", length = 10)
    private String templateVersion = "1.0";
}