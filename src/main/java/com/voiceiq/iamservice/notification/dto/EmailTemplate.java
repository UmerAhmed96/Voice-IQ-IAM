package com.voiceiq.iamservice.notification.dto;

public enum EmailTemplate {
    PASSWORD_RESET("password-reset"),
    EMAIL_VERIFICATION("email-verification"),
    ORGANIZATION_INVITATION("organization-invitation"),
    PROCESSING_COMPLETE("processing-complete"),
    REPORT_READY("report-ready"),
    FACT_CHECK_COMPLETE("fact-check-complete"),
    WEEKLY_DIGEST("weekly-digest"),
    SECURITY_ALERT("security-alert");
    
    private final String templateName;
    
    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateName() {
        return templateName;
    }
}