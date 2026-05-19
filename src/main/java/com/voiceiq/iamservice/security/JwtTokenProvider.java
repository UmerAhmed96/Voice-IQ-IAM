package com.voiceiq.iamservice.security;

import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    
    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${app.jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
    
    public String generateAccessToken(User user, UserRole role, UUID organizationId) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(accessTokenExpiration);
        
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("role", role.name());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        
        if (organizationId != null) {
            claims.put("organizationId", organizationId.toString());
        }
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .signWith(secretKey)
                .compact();
    }
    
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
    
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token");
        }
    }
    
    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }
    
    public UserRole getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String role = (String) claims.get("role");
        return UserRole.valueOf(role);
    }
    
    public UUID getOrganizationIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String orgId = (String) claims.get("organizationId");
        return orgId != null ? UUID.fromString(orgId) : null;
    }
}