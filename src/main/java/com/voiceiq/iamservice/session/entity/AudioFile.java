package com.voiceiq.iamservice.session.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "audio_files")
@Getter
@Setter
public class AudioFile extends BaseAuditEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;
    
    @Column(name = "original_filename", length = 500)
    private String originalFilename;
    
    @Column(name = "content_type", length = 100)
    private String contentType;
    
    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "storage_key", nullable = false, length = 1000)
    private String storageKey;
    
    @Column(name = "checksum")
    private String checksum;
    
    @Column(name = "upload_completed_at")
    private Instant uploadCompletedAt;
}