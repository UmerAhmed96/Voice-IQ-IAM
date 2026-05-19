package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.PermissionResponse;
import com.voiceiq.iamservice.iam.dto.RoleResponse;
import com.voiceiq.iamservice.iam.dto.UserPermissionsResponse;
import com.voiceiq.iamservice.iam.entity.Permission;
import com.voiceiq.iamservice.iam.entity.Role;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.entity.UserRole;
import com.voiceiq.iamservice.iam.repository.PermissionRepository;
import com.voiceiq.iamservice.iam.repository.RoleRepository;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolePermissionService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAllWithPermissions();
        return roles.stream()
            .map(this::mapToRoleResponse)
            .collect(Collectors.toList());
    }
    
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
            .map(this::mapToPermissionResponse)
            .collect(Collectors.toList());
    }
    
    public UserPermissionsResponse getUserPermissions(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        // Get permissions based on user's role
        String roleName = getUserRoleName(user);
        List<String> permissions = permissionRepository.findPermissionNamesByRoleName(roleName);
        
        return UserPermissionsResponse.builder()
            .userId(user.getId())
            .organizationId(null) // TODO: Get from user's organization
            .role(roleName)
            .permissions(permissions)
            .build();
    }
    
    private String getUserRoleName(User user) {
        // TODO: Get actual role from user or organization membership
        // For now, return a default based on user status
        return user.getIsActive() ? "USER" : "INACTIVE";
    }
    
    private RoleResponse mapToRoleResponse(Role role) {
        List<String> permissionNames = role.getPermissions().stream()
            .map(Permission::getName)
            .collect(Collectors.toList());
            
        return RoleResponse.builder()
            .id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .isSystemRole(role.getIsSystemRole())
            .permissions(permissionNames)
            .createdAt(role.getCreatedAt())
            .updatedAt(role.getUpdatedAt())
            .build();
    }
    
    private PermissionResponse mapToPermissionResponse(Permission permission) {
        return PermissionResponse.builder()
            .id(permission.getId())
            .name(permission.getName())
            .description(permission.getDescription())
            .resource(permission.getResource())
            .action(permission.getAction())
            .createdAt(permission.getCreatedAt())
            .updatedAt(permission.getUpdatedAt())
            .build();
    }
}