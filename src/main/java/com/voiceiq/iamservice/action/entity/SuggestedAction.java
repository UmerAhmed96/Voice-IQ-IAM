package com.voiceiq.iamservice.action.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.transcript.entity.TranscriptSegment;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "suggested_actions")
@Getter
@Setter
public class SuggestedAction extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_segment_id")
    private TranscriptSegment sourceSegment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
    
    @Column(name = "title", nullable = false, length = 500)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActionStatus status = ActionStatus.SUGGESTED;
    
    @Column(name = "confidence")
    private Double confidence;
    
    @Column(name = "priority")
    private Integer priority = 3; // 1=High, 2=Medium, 3=Low
    
    @Column(name = "due_date_hint")
    private Instant dueDateHint;
    
    @Column(name = "action_payload")
    private String actionPayload; // Integration-specific data
    
    @Column(name = "approved_at")
    private Instant approvedAt;
    
    @Column(name = "rejected_at")
    private Instant rejectedAt;
    
    @Column(name = "executed_at")
    private Instant executedAt;
    
    @Column(name = "execution_result")
    private String executionResult;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
}