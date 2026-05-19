package com.voiceiq.iamservice.insight.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "analysis_results")
@Getter
@Setter
public class AnalysisResult extends BaseAuditEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Column(name = "schema_version", length = 10)
    private String schemaVersion = "1.0";
    
    @Column(name = "overall_summary", columnDefinition = "TEXT")
    private String overallSummary;
    
    @Column(name = "short_summary", columnDefinition = "TEXT")
    private String shortSummary;
    
    @Column(name = "detailed_summary", columnDefinition = "TEXT")
    private String detailedSummary;
    
    @Column(name = "key_points")
    private String keyPoints; // JSON array
    
    // Scores (0.0 to 1.0)
    @Column(name = "sentiment_score")
    private Double sentimentScore;
    
    @Column(name = "escalation_score")
    private Double escalationScore;
    
    @Column(name = "clarity_score")
    private Double clarityScore;
    
    @Column(name = "risk_score")
    private Double riskScore;
    
    @Column(name = "engagement_score")
    private Double engagementScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.PROCESSING;
    
    @Column(name = "raw_analysis_payload")
    private String rawAnalysisPayload;
}