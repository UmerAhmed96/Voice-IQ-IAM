package com.voiceiq.iamservice.iam.entity;

public enum UserRole {
    SUPER_ADMIN("Platform-level administrator"),
    ORG_ADMIN("Organization administrator"),
    MANAGER("Team manager with view access"),
    ANALYST("Can create/upload sessions"),
    VIEWER("Read-only access"),
    USER("Default authenticated user");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}