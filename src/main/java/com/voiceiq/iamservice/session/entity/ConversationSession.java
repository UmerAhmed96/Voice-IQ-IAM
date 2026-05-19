package com.voiceiq.iamservice.session.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.organization.entity.Organization;
import com.voiceiq.iamservice.team.entity.Team;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSession extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    
    @Column(name = "title", nullable = false, length = 500)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType = SourceType.AUDIO_UPLOAD;
    
    @Column(name = "language_code", length = 10)
    private String languageCode = "en";
    
    @Column(name = "expected_speakers")
    private Integer expectedSpeakers;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.DRAFT;
    
    @Column(name = "is_archived")
    private Boolean isArchived = false;
}