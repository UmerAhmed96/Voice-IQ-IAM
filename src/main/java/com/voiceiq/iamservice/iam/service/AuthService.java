package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.*;
import com.voiceiq.iamservice.iam.entity.UserSession;
import com.voiceiq.iamservice.iam.entity.RefreshToken;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import com.voiceiq.iamservice.iam.repository.RefreshTokenRepository;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import com.voiceiq.iamservice.iam.repository.UserSessionRepository;
import com.voiceiq.iamservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.conflict("User with this email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsEmailVerified(false);
        user.setIsActive(true);
        
        user = userRepository.save(user);
        
        return generateAuthResponse(user, UserRole.USER, null);
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> BusinessException.unauthorized("Invalid credentials"));
        
        if (!user.getIsActive()) {
            throw BusinessException.unauthorized("User account is inactive");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw BusinessException.unauthorized("Invalid credentials");
        }
        
        return generateAuthResponse(user, UserRole.USER, null);
    }
    
    public AuthResponse refreshToken(String refreshTokenValue) {
        String tokenHash = hashToken(refreshTokenValue);
        
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> BusinessException.unauthorized("Invalid refresh token"));
        
        if (!refreshToken.isValid()) {
            throw BusinessException.unauthorized("Refresh token is expired or revoked");
        }
        
        return generateAuthResponse(refreshToken.getUser(), UserRole.USER, null);
    }
    
    public void logout(String refreshTokenValue) {
        String tokenHash = hashToken(refreshTokenValue);
        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.setIsRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }
    
    private AuthResponse generateAuthResponse(User user, UserRole role, UUID organizationId) {
        String accessToken = jwtTokenProvider.generateAccessToken(user, role, organizationId);
        String refreshTokenValue = jwtTokenProvider.generateRefreshToken();
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(refreshTokenValue));
        refreshToken.setExpiresAt(Instant.now().plusMillis(604800000)); // 7 days
        
        refreshTokenRepository.save(refreshToken);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(3600) // 1 hour in seconds
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(role)
                .organizationId(organizationId)
                .build();
    }
    
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    public void resendVerificationEmail(ResendVerificationRequest request) {
        // Security: Don't reveal whether email exists or not
        log.info("Resend verification email request for: {}", request.getEmail());
        
        // TODO: Send email if user exists and not verified
        // This is a placeholder - actual email sending should be implemented
    }
    
    public void changePassword(ChangePasswordRequest request, UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.notFound("User not found"));
            
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw BusinessException.badRequest("Current password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        // Revoke all other sessions except current one
        userSessionRepository.revokeAllSessionsForUser(userId, Instant.now(), "Password changed");
        
        log.info("Password changed for user: {}", userId);
    }
    
    public List<UserSessionResponse> getActiveSessions(UUID userId) {
        List<UserSession> sessions = userSessionRepository.findByUserIdAndRevokedAtIsNullOrderByLastUsedAtDesc(userId);
        
        return sessions.stream()
            .map(this::mapToUserSessionResponse)
            .collect(java.util.stream.Collectors.toList());
    }
    
    public void revokeSession(UUID sessionId, UUID userId) {
        int updated = userSessionRepository.revokeSession(sessionId, userId, Instant.now(), "Revoked by user");
        
        if (updated == 0) {
            throw BusinessException.notFound("Session not found or already revoked");
        }
        
        log.info("Session {} revoked for user: {}", sessionId, userId);
    }
    
    public void logoutAll(UUID userId) {
        userSessionRepository.revokeAllSessionsForUser(userId, Instant.now(), "Logout all sessions");
        log.info("All sessions revoked for user: {}", userId);
    }
    
    private UserSessionResponse mapToUserSessionResponse(UserSession session) {
        return UserSessionResponse.builder()
            .sessionId(session.getId())
            .deviceName(session.getDeviceName())
            .ipAddress(maskIpAddress(session.getIpAddressHash()))
            .lastUsedAt(session.getLastUsedAt())
            .createdAt(session.getCreatedAt())
            .current(false) // TODO: Determine if this is current session
            .build();
    }
    
    private String maskIpAddress(String ipHash) {
        // For security, show masked IP instead of actual IP
        return ipHash != null ? "***." + ipHash.substring(Math.max(0, ipHash.length() - 6)) : "unknown";
    }
}