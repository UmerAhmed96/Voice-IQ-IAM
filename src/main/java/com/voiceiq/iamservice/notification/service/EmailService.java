package com.voiceiq.iamservice.notification.service;

import com.voiceiq.iamservice.notification.dto.EmailTemplate;
import com.voiceiq.iamservice.notification.dto.WeeklyDigestData;

public interface EmailService {
    
    /**
     * Send password reset email with secure token
     */
    void sendPasswordResetEmail(String email, String firstName, String resetToken);
    
    /**
     * Send email verification with verification token
     */
    void sendEmailVerificationEmail(String email, String firstName, String verificationToken);
    
    /**
     * Send organization invitation email
     */
    void sendOrganizationInvitationEmail(String email, String organizationName, 
                                       String inviterName, String invitationToken);
    
    /**
     * Send processing completion notification
     */
    void sendProcessingCompleteNotification(String email, String sessionTitle, 
                                          String sessionId, boolean hasErrors);
    
    /**
     * Send report generation notification
     */
    void sendReportReadyNotification(String email, String reportTitle, 
                                   String downloadUrl, String expirationTime);
    
    /**
     * Send fact-check completion notification
     */
    void sendFactCheckCompleteNotification(String email, String sessionTitle, 
                                         int claimsVerified, int flaggedClaims);
    
    /**
     * Send weekly digest email
     */
    void sendWeeklyDigestEmail(String email, String organizationName, 
                             WeeklyDigestData digestData);
    
    /**
     * Send security alert email
     */
    void sendSecurityAlertEmail(String email, String alertType, String ipAddress, 
                               String userAgent, java.time.Instant timestamp);
    
    /**
     * Send custom template email
     */
    void sendTemplateEmail(String email, EmailTemplate template, 
                          java.util.Map<String, Object> variables);
}

