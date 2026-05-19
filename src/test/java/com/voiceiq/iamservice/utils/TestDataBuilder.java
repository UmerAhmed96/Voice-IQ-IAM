package com.voiceiq.iamservice.utils;

import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserSession;
import com.voiceiq.iamservice.session.entity.ConversationSession;
import com.voiceiq.iamservice.session.entity.SessionAccess;

import java.time.Instant;
import java.util.UUID;

public class TestDataBuilder {
    
    public static User createUser() {
        return User.builder()
                .email("test@example.com")
                .passwordHash("$2a$10$hashedPassword")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+1234567890")
                .isEmailVerified(true)
                .isActive(true)
                .status(User.UserStatus.ACTIVE)
                .build();
    }
    
    public static User createUser(String email) {
        return User.builder()
                .email(email)
                .passwordHash("$2a$10$hashedPassword")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+1234567890")
                .isEmailVerified(true)
                .isActive(true)
                .status(User.UserStatus.ACTIVE)
                .build();
    }
    
    public static User createAdmin() {
        return User.builder()
                .email("admin@example.com")
                .passwordHash("$2a$10$hashedPassword")
                .firstName("Admin")
                .lastName("User")
                .isEmailVerified(true)
                .isActive(true)
                .status(User.UserStatus.ACTIVE)
                .build();
    }
    
    public static UserSession createUserSession(User user) {
        return UserSession.builder()
                .user(user)
                .deviceName("Chrome on Windows")
                .ipAddressHash("192.168.1.1")
                .userAgent("Mozilla/5.0...")
                .lastUsedAt(Instant.now())
                .build();
    }
    
    public static ConversationSession createConversationSession(User user) {
        return ConversationSession.builder()
                .user(user)
                .title("Test Session")
                .description("Test Description")
                .build();
    }
    
    public static SessionAccess createSessionAccess(ConversationSession session, UUID targetId, UUID grantedBy) {
        return SessionAccess.builder()
                .session(session)
                .targetType(SessionAccess.TargetType.USER)
                .targetId(targetId)
                .accessLevel(SessionAccess.AccessLevel.VIEWER)
                .grantedBy(grantedBy)
                .build();
    }
}