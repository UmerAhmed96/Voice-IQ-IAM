package com.voiceiq.iamservice.insight.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.transcript.entity.Speaker;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "speaker_insights")
@Getter
@Setter
public class SpeakerInsight extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Speaker speaker;
    
    @Column(name = "talk_time_ratio")
    private Double talkTimeRatio; // 0.0 to 1.0
    
    @Column(name = "interruption_count")
    private Integer interruptionCount = 0;
    
    @Column(name = "average_sentiment")
    private Double averageSentiment;
    
    @Column(name = "dominant_topics")
    private String dominantTopics; // JSON array
    
    @Column(name = "engagement_level")
    private Double engagementLevel;
    
    @Column(name = "speaking_pace")
    private Double speakingPace; // words per minute
    
    @Column(name = "question_count")
    private Integer questionCount = 0;
    
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;
}