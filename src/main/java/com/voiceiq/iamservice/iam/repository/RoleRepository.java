package com.voiceiq.iamservice.iam.repository;

import com.voiceiq.iamservice.iam.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByName(String name);
    
    List<Role> findByIsSystemRoleTrue();
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.name = :roleName")
    Optional<Role> findByNameWithPermissions(@Param("roleName") String roleName);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.permissions")
    List<Role> findAllWithPermissions();
}