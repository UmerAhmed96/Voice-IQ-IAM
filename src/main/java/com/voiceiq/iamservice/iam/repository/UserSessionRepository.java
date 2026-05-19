package com.voiceiq.iamservice.iam.repository;

import com.voiceiq.iamservice.iam.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    
    List<UserSession> findByUserIdAndRevokedAtIsNullOrderByLastUsedAtDesc(UUID userId);
    
    Optional<UserSession> findByIdAndUserIdAndRevokedAtIsNull(UUID sessionId, UUID userId);
    
    @Modifying
    @Query("UPDATE UserSession us SET us.revokedAt = :revokedAt, us.revokedReason = :reason " +
           "WHERE us.user.id = :userId AND us.revokedAt IS NULL")
    void revokeAllSessionsForUser(@Param("userId") UUID userId, 
                                 @Param("revokedAt") Instant revokedAt, 
                                 @Param("reason") String reason);
    
    @Modifying
    @Query("UPDATE UserSession us SET us.revokedAt = :revokedAt, us.revokedReason = :reason " +
           "WHERE us.id = :sessionId AND us.user.id = :userId AND us.revokedAt IS NULL")
    int revokeSession(@Param("sessionId") UUID sessionId, 
                     @Param("userId") UUID userId, 
                     @Param("revokedAt") Instant revokedAt, 
                     @Param("reason") String reason);
}