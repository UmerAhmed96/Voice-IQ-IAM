package com.voiceiq.iamservice.session.repository;

import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionStatus;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.organization.entity.Organization;
import com.voiceiq.iamservice.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationSessionRepository extends JpaRepository<ConversationSession, UUID> {
    
    List<ConversationSession> findByUserAndIsArchivedFalse(User user);
    
    List<ConversationSession> findByOrganizationAndIsArchivedFalse(Organization organization);
    
    List<ConversationSession> findByTeamAndIsArchivedFalse(Team team);
    
    Page<ConversationSession> findByUserAndIsArchivedFalse(User user, Pageable pageable);
    
    List<ConversationSession> findByStatus(SessionStatus status);
    
    @Query("SELECT cs FROM ConversationSession cs WHERE cs.user = :user OR " +
           "(cs.organization IS NOT NULL AND EXISTS (" +
           "SELECT om FROM OrganizationMember om WHERE om.organization = cs.organization AND om.user = :user AND om.isActive = true))")
    Page<ConversationSession> findAccessibleSessions(User user, Pageable pageable);
    
    Optional<ConversationSession> findByIdAndIsArchivedFalse(UUID id);
}