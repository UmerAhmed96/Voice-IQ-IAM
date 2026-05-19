package com.voiceiq.iamservice.organization.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import com.voiceiq.iamservice.organization.dto.OrganizationRequest;
import com.voiceiq.iamservice.organization.dto.OrganizationResponse;
import com.voiceiq.iamservice.organization.entity.Organization;
import com.voiceiq.iamservice.organization.entity.OrganizationMember;
import com.voiceiq.iamservice.organization.repository.OrganizationMemberRepository;
import com.voiceiq.iamservice.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;
    private final UserRepository userRepository;
    
    public OrganizationResponse createOrganization(UUID userId, OrganizationRequest request) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        if (organizationRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw BusinessException.conflict("Organization with this name already exists");
        }
        
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setDomain(request.getDomain());
        organization.setIsActive(true);
        
        organization = organizationRepository.save(organization);
        
        // Add creator as ORG_ADMIN
        OrganizationMember member = new OrganizationMember();
        member.setOrganization(organization);
        member.setUser(user);
        member.setRole(UserRole.ORG_ADMIN);
        member.setIsActive(true);
        
        memberRepository.save(member);
        
        return mapToOrganizationResponse(organization, 1L);
    }
    
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getUserOrganizations(UUID userId) {
        List<Organization> organizations = organizationRepository.findOrganizationsByUserId(userId);
        
        return organizations.stream()
                .map(org -> {
                    Long memberCount = (long) memberRepository.findByOrganizationAndIsActiveTrue(org).size();
                    return mapToOrganizationResponse(org, memberCount);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganization(UUID userId, UUID organizationId) {
        Organization organization = findOrganizationWithMembershipCheck(userId, organizationId);
        Long memberCount = (long) memberRepository.findByOrganizationAndIsActiveTrue(organization).size();
        
        return mapToOrganizationResponse(organization, memberCount);
    }
    
    public OrganizationResponse updateOrganization(UUID userId, UUID organizationId, OrganizationRequest request) {
        Organization organization = findOrganizationWithAdminCheck(userId, organizationId);
        
        if (!organization.getName().equals(request.getName()) && 
            organizationRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw BusinessException.conflict("Organization with this name already exists");
        }
        
        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setDomain(request.getDomain());
        
        organization = organizationRepository.save(organization);
        Long memberCount = (long) memberRepository.findByOrganizationAndIsActiveTrue(organization).size();
        
        return mapToOrganizationResponse(organization, memberCount);
    }
    
    private Organization findOrganizationWithMembershipCheck(UUID userId, UUID organizationId) {
        Organization organization = organizationRepository.findByIdAndIsActiveTrue(organizationId)
                .orElseThrow(() -> BusinessException.notFound("Organization not found"));
        
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        if (!memberRepository.existsByOrganizationAndUserAndIsActiveTrue(organization, user)) {
            throw BusinessException.accessDenied("Access denied to organization");
        }
        
        return organization;
    }
    
    private Organization findOrganizationWithAdminCheck(UUID userId, UUID organizationId) {
        Organization organization = findOrganizationWithMembershipCheck(userId, organizationId);
        
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        OrganizationMember member = memberRepository.findByOrganizationAndUserAndIsActiveTrue(organization, user)
                .orElseThrow(() -> BusinessException.accessDenied("Access denied to organization"));
        
        if (member.getRole() != UserRole.ORG_ADMIN && member.getRole() != UserRole.SUPER_ADMIN) {
            throw BusinessException.accessDenied("Organization admin access required");
        }
        
        return organization;
    }
    
    private OrganizationResponse mapToOrganizationResponse(Organization organization, Long memberCount) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .description(organization.getDescription())
                .domain(organization.getDomain())
                .isActive(organization.getIsActive())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .memberCount(memberCount)
                .build();
    }
}