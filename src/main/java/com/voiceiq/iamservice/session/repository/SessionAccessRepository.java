package com.voiceiq.iamservice.session.repository;

import com.voiceiq.iamservice.session.entity.SessionAccess;
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
public interface SessionAccessRepository extends JpaRepository<SessionAccess, UUID> {
    
    List<SessionAccess> findBySessionIdAndRevokedAtIsNull(UUID sessionId);
    
    Optional<SessionAccess> findByIdAndSessionIdAndRevokedAtIsNull(UUID accessId, UUID sessionId);
    
    @Query("SELECT sa FROM SessionAccess sa WHERE sa.session.id = :sessionId " +
           "AND sa.targetType = :targetType AND sa.targetId = :targetId " +
           "AND sa.revokedAt IS NULL " +
           "AND (sa.expiresAt IS NULL OR sa.expiresAt > :now)")
    Optional<SessionAccess> findActiveAccess(@Param("sessionId") UUID sessionId,
                                           @Param("targetType") SessionAccess.TargetType targetType,
                                           @Param("targetId") UUID targetId,
                                           @Param("now") Instant now);
    
    @Modifying
    @Query("UPDATE SessionAccess sa SET sa.revokedAt = :revokedAt " +
           "WHERE sa.id = :accessId AND sa.session.id = :sessionId AND sa.revokedAt IS NULL")
    int revokeAccess(@Param("accessId") UUID accessId,
                    @Param("sessionId") UUID sessionId,
                    @Param("revokedAt") Instant revokedAt);
    
    @Query("SELECT DISTINCT sa.session.id FROM SessionAccess sa WHERE " +
           "sa.targetType = 'USER' AND sa.targetId = :userId " +
           "AND sa.revokedAt IS NULL " +
           "AND (sa.expiresAt IS NULL OR sa.expiresAt > :now)")
    List<UUID> findAccessibleSessionIds(@Param("userId") UUID userId, @Param("now") Instant now);
}