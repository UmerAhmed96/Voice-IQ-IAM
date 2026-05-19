package com.voiceiq.iamservice.organization.repository;

import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import com.voiceiq.iamservice.organization.entity.Organization;
import com.voiceiq.iamservice.organization.entity.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, UUID> {
    
    List<OrganizationMember> findByOrganizationAndIsActiveTrue(Organization organization);
    
    Optional<OrganizationMember> findByOrganizationAndUserAndIsActiveTrue(Organization organization, User user);
    
    boolean existsByOrganizationAndUserAndIsActiveTrue(Organization organization, User user);
    
    Optional<OrganizationMember> findByIdAndOrganizationAndIsActiveTrue(UUID id, Organization organization);
    
    boolean existsByOrganizationAndRoleAndIsActiveTrue(Organization organization, UserRole role);
}