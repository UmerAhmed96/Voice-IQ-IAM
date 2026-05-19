package com.voiceiq.iamservice.report.entity;

public enum ReportType {
    EXECUTIVE_SUMMARY("Executive Summary Report"),
    FULL_CONVERSATION("Full Conversation Report"),
    FACT_CHECKING("Fact-Checking Report"),
    SPEAKER_ANALYSIS("Speaker Analysis Report"),
    CUSTOM("Custom Report");
    
    private final String description;
    
    ReportType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}