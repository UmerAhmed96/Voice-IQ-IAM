package com.voiceiq.iamservice.session.entity;

public enum SourceType {
    AUDIO_UPLOAD("Direct audio file upload"),
    LIVE_RECORDING("Live recording session"),
    API_INTEGRATION("External API integration");
    
    private final String description;
    
    SourceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}