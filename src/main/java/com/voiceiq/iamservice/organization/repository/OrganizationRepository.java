package com.voiceiq.iamservice.organization.repository;

import com.voiceiq.iamservice.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    
    @Query("SELECT o FROM Organization o JOIN OrganizationMember om ON o.id = om.organization.id " +
           "WHERE om.user.id = :userId AND om.isActive = true AND o.isActive = true")
    List<Organization> findOrganizationsByUserId(UUID userId);
    
    Optional<Organization> findByIdAndIsActiveTrue(UUID id);
    
    boolean existsByNameAndIsActiveTrue(String name);
}