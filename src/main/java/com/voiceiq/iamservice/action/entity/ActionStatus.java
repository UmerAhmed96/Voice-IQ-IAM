package com.voiceiq.iamservice.action.entity;

public enum ActionStatus {
    SUGGESTED("Detected by system but not yet reviewed by user"),
    APPROVED("User approved execution"),
    EDITED("User modified action details before approval"),
    REJECTED("User rejected suggestion"),
    SCHEDULED("Action is approved and scheduled for later execution"),
    EXECUTING("Action execution is in progress"),
    COMPLETED("Action executed successfully"),
    FAILED("Action execution failed"),
    CANCELLED("User/admin cancelled action");
    
    private final String description;
    
    ActionStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}