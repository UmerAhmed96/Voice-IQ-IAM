package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.UpdateUserRequest;
import com.voiceiq.iamservice.iam.dto.UpdateUserStatusRequest;
import com.voiceiq.iamservice.iam.dto.UserResponse;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserResponse getCurrentUser(UUID userId) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        return mapToUserResponse(user);
    }
    
    @Transactional
    public UserResponse updateCurrentUser(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        user = userRepository.save(user);
        return mapToUserResponse(user);
    }
    
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .isEmailVerified(user.getIsEmailVerified())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        return mapToUserResponse(user);
    }
    
    @Transactional
    public void updateUserStatus(UUID userId, UpdateUserStatusRequest request, UUID adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("User not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> BusinessException.notFound("Admin user not found"));
        
        // Convert DTO enum to entity enum
        User.UserStatus status;
        switch (request.getStatus()) {
            case ACTIVE:
                status = User.UserStatus.ACTIVE;
                break;
            case SUSPENDED:
                status = User.UserStatus.SUSPENDED;
                break;
            case DISABLED:
                status = User.UserStatus.DISABLED;
                break;
            default:
                throw BusinessException.badRequest("Invalid status");
        }
        
        user.setStatus(status);
        user.setIsActive(status == User.UserStatus.ACTIVE);
        userRepository.save(user);
    }
    
    public boolean isUserInSameOrganization(UUID adminId, UUID targetUserId) {
        // TODO: Implement organization membership check
        // For now, return true as a placeholder
        return true;
    }
}