package com.voiceiq.iamservice.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklyDigestData {
    private int sessionsProcessed;
    private int reportsGenerated;
    private int factsChecked;
    private double totalProcessingHours;
    private String topInsight;
}