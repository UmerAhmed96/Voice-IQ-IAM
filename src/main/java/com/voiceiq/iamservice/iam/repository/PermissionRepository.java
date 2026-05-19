package com.voiceiq.iamservice.iam.repository;

import com.voiceiq.iamservice.iam.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    
    Optional<Permission> findByName(String name);
    
    List<Permission> findByResource(String resource);
    
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r WHERE r.name = :roleName")
    List<Permission> findByRoleName(@Param("roleName") String roleName);
    
    @Query("SELECT DISTINCT p.name FROM Permission p JOIN p.roles r WHERE r.name = :roleName")
    List<String> findPermissionNamesByRoleName(@Param("roleName") String roleName);
}