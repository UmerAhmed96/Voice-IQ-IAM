package com.voiceiq.iamservice.transcript.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transcript_segments")
@Getter
@Setter
public class TranscriptSegment extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Speaker speaker;
    
    @Column(name = "start_ms", nullable = false)
    private Long startMs;
    
    @Column(name = "end_ms", nullable = false)
    private Long endMs;
    
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
    
    @Column(name = "confidence")
    private Double confidence;
    
    @Column(name = "language", length = 10)
    private String language;
    
    @Column(name = "sentiment_label", length = 50)
    private String sentimentLabel;
    
    @Column(name = "sentiment_score")
    private Double sentimentScore;
}