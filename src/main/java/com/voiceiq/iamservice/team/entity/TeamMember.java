package com.voiceiq.iamservice.team.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.organization.entity.OrganizationMember;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "team_members")
@Getter
@Setter
public class TeamMember extends BaseAuditEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_member_id", nullable = false)
    private OrganizationMember organizationMember;
    
    @Column(name = "is_team_lead")
    private Boolean isTeamLead = false;
    
    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt = Instant.now();
}