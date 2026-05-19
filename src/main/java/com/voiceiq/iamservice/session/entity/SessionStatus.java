package com.voiceiq.iamservice.session.entity;

public enum SessionStatus {
    DRAFT("Session created but audio not uploaded yet"),
    AUDIO_UPLOADED("Audio metadata and storage object are available"),
    QUEUED("Processing has been requested but not started"),
    PROCESSING("Processing is in progress"),
    COMPLETED("Processing completed successfully"),
    FAILED("Processing failed"),
    ARCHIVED("Session hidden/archived by user");
    
    private final String description;
    
    SessionStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canTransitionTo(SessionStatus newStatus) {
        switch (this) {
            case DRAFT:
                return newStatus == AUDIO_UPLOADED || newStatus == ARCHIVED;
            case AUDIO_UPLOADED:
                return newStatus == QUEUED || newStatus == ARCHIVED;
            case QUEUED:
                return newStatus == PROCESSING || newStatus == FAILED || newStatus == ARCHIVED;
            case PROCESSING:
                return newStatus == COMPLETED || newStatus == FAILED;
            case COMPLETED:
                return newStatus == ARCHIVED;
            case FAILED:
                return newStatus == QUEUED || newStatus == ARCHIVED; // Allow retry
            case ARCHIVED:
                return false; // No transitions from archived
            default:
                return false;
        }
    }
}