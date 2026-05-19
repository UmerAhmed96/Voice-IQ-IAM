package com.voiceiq.iamservice.factcheck.entity;

public enum FactStatus {
    PENDING("Claim extracted but not checked yet"),
    CHECKING("Fact-checking job is running"),
    SUPPORTED("Evidence supports the claim"),
    CONTRADICTED("Evidence contradicts the claim"),
    INSUFFICIENT_EVIDENCE("No reliable evidence found"),
    NEEDS_REVIEW("Low confidence or sensitive claim requiring manual review"),
    MANUALLY_OVERRIDDEN("A human reviewer changed or confirmed the result");
    
    private final String description;
    
    FactStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}