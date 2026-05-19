package com.voiceiq.iamservice.organization.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization extends BaseAuditEntity {
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
}