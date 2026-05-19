package com.voiceiq.iamservice.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SystemHealthResponse {
    private String status;
    private Instant timestamp;
    private DatabaseHealth database;
    private StorageHealth storage;
    private PythonServiceHealth pythonService;
    private SystemMetrics metrics;
    
    @Data
    @Builder
    public static class DatabaseHealth {
        private String status;
        private Long connectionCount;
        private Long activeConnections;
        private String version;
    }
    
    @Data
    @Builder
    public static class StorageHealth {
        private String status;
        private Long totalFilesStored;
        private Long totalStorageUsed;
        private String availableSpace;
    }
    
    @Data
    @Builder
    public static class PythonServiceHealth {
        private String status;
        private Integer responseTimeMs;
        private String version;
    }
    
    @Data
    @Builder
    public static class SystemMetrics {
        private Long totalSessions;
        private Long activeSessions;
        private Long totalUsers;
        private Long totalOrganizations;
        private Double cpuUsage;
        private Double memoryUsage;
    }
}