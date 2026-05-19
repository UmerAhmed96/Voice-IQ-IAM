package com.voiceiq.iamservice.team.repository;

import com.voiceiq.iamservice.organization.entity.Organization;
import com.voiceiq.iamservice.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    
    List<Team> findByOrganizationAndIsActiveTrue(Organization organization);
    
    Optional<Team> findByIdAndOrganizationAndIsActiveTrue(UUID id, Organization organization);
    
    boolean existsByOrganizationAndNameAndIsActiveTrue(Organization organization, String name);
}