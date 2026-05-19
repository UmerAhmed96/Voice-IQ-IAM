package com.voiceiq.iamservice.factcheck.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.transcript.entity.Speaker;
import com.voiceiq.iamservice.transcript.entity.TranscriptSegment;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "claims")
@Getter
@Setter
public class Claim extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private TranscriptSegment segment;
    
    @Column(name = "claim_text", nullable = false, columnDefinition = "TEXT")
    private String claimText;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id")
    private Speaker speaker;
    
    @Column(name = "start_ms")
    private Long startMs;
    
    @Column(name = "claim_type", length = 100)
    private String claimType; // FACT, OPINION, STATISTIC, etc.
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FactStatus status = FactStatus.PENDING;
    
    @Column(name = "confidence")
    private Double confidence;
    
    @Column(name = "verification_summary", columnDefinition = "TEXT")
    private String verificationSummary;
}