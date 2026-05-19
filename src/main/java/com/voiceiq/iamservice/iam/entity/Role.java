package com.voiceiq.iamservice.iam.entity;

import com.voiceiq.iamservice.common.entity.BaseAuditEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseAuditEntity {
    
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "is_system_role", nullable = false)
    private Boolean isSystemRole = false;
    
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Permission> permissions = new HashSet<>();
}