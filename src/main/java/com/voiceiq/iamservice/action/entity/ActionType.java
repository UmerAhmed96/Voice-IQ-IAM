package com.voiceiq.iamservice.action.entity;

public enum ActionType {
    EMAIL("Send follow-up email"),
    CALENDAR("Create calendar meeting"),
    TASK("Create task or todo item"),
    NOTIFICATION("Send notification to team/channel"),
    CRM_UPDATE("Update CRM record"),
    DOCUMENT("Create or update document"),
    REMINDER("Set reminder"),
    ESCALATION("Escalate to manager/admin");
    
    private final String description;
    
    ActionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}