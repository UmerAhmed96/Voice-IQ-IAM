package com.voiceiq.iamservice.transcript.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "speakers")
@Getter
@Setter
public class Speaker extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Column(name = "display_name", length = 255)
    private String displayName;
    
    @Column(name = "speaker_label", nullable = false, length = 50)
    private String speakerLabel; // System generated: Speaker_1, Speaker_2
    
    @Column(name = "total_talk_time_ms")
    private Long totalTalkTimeMs = 0L;
    
    @Column(name = "segment_count")
    private Integer segmentCount = 0;
    
    @Column(name = "average_confidence")
    private Double averageConfidence;
}