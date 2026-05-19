package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.*;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserSession;
import com.voiceiq.iamservice.iam.repository.RefreshTokenRepository;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import com.voiceiq.iamservice.iam.repository.UserSessionRepository;
import com.voiceiq.iamservice.security.JwtTokenProvider;
import com.voiceiq.iamservice.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @Mock
    private UserSessionRepository userSessionRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = TestDataBuilder.createUser();
        testUser.setId(testUserId);
    }

    @Test
    void register_WithValidData_ShouldCreateUser() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("+1234567890");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateAccessToken(any(), any(), any())).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken()).thenReturn("refreshToken");

        // When
        AuthResponse response = authService.register(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithExistingEmail_ShouldThrowException() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User with this email already exists");
    }

    @Test
    void resendVerificationEmail_WithValidEmail_ShouldNotThrow() {
        // Given
        ResendVerificationRequest request = new ResendVerificationRequest();
        request.setEmail("test@example.com");

        // When & Then
        assertThatCode(() -> authService.resendVerificationEmail(request))
                .doesNotThrowAnyException();
    }

    @Test
    void changePassword_WithCorrectCurrentPassword_ShouldUpdatePassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getCurrentPassword(), testUser.getPasswordHash())).thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("newHashedPassword");

        // When
        authService.changePassword(request, testUserId);

        // Then
        verify(userRepository).save(testUser);
        verify(userSessionRepository).revokeAllSessionsForUser(eq(testUserId), any(Instant.class), eq("Password changed"));
    }

    @Test
    void changePassword_WithIncorrectCurrentPassword_ShouldThrowException() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getCurrentPassword(), testUser.getPasswordHash())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> authService.changePassword(request, testUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Current password is incorrect");
    }

    @Test
    void changePassword_WithNonExistentUser_ShouldThrowException() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authService.changePassword(request, testUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User not found");
    }

    @Test
    void getActiveSessions_WithValidUser_ShouldReturnSessions() {
        // Given
        UserSession session1 = TestDataBuilder.createUserSession(testUser);
        session1.setId(UUID.randomUUID());
        session1.setDeviceName("Chrome");
        
        UserSession session2 = TestDataBuilder.createUserSession(testUser);
        session2.setId(UUID.randomUUID());
        session2.setDeviceName("Firefox");
        
        when(userSessionRepository.findByUserIdAndRevokedAtIsNullOrderByLastUsedAtDesc(testUserId))
                .thenReturn(Arrays.asList(session1, session2));

        // When
        List<UserSessionResponse> result = authService.getActiveSessions(testUserId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDeviceName()).isEqualTo("Chrome");
        assertThat(result.get(1).getDeviceName()).isEqualTo("Firefox");
    }

    @Test
    void revokeSession_WithValidSessionId_ShouldRevokeSession() {
        // Given
        UUID sessionId = UUID.randomUUID();
        when(userSessionRepository.revokeSession(eq(sessionId), eq(testUserId), any(Instant.class), eq("Revoked by user")))
                .thenReturn(1);

        // When
        authService.revokeSession(sessionId, testUserId);

        // Then
        verify(userSessionRepository).revokeSession(eq(sessionId), eq(testUserId), any(Instant.class), eq("Revoked by user"));
    }

    @Test
    void revokeSession_WithInvalidSessionId_ShouldThrowException() {
        // Given
        UUID sessionId = UUID.randomUUID();
        when(userSessionRepository.revokeSession(eq(sessionId), eq(testUserId), any(Instant.class), eq("Revoked by user")))
                .thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> authService.revokeSession(sessionId, testUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Session not found or already revoked");
    }

    @Test
    void logoutAll_WithValidUser_ShouldRevokeAllSessions() {
        // When
        authService.logoutAll(testUserId);

        // Then
        verify(userSessionRepository).revokeAllSessionsForUser(eq(testUserId), any(Instant.class), eq("Logout all sessions"));
    }
}