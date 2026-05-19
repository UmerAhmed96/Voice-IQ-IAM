package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.UpdateUserRequest;
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
}