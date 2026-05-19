package com.voiceiq.iamservice.iam.service;

import com.voiceiq.iamservice.common.exception.BusinessException;
import com.voiceiq.iamservice.iam.dto.UpdateUserRequest;
import com.voiceiq.iamservice.iam.dto.UpdateUserStatusRequest;
import com.voiceiq.iamservice.iam.dto.UserResponse;
import com.voiceiq.iamservice.iam.entity.User;
import com.voiceiq.iamservice.iam.repository.UserRepository;
import com.voiceiq.iamservice.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;
    private UUID testUserId;
    private UUID adminUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        adminUserId = UUID.randomUUID();
        
        testUser = TestDataBuilder.createUser();
        testUser.setId(testUserId);
        
        adminUser = TestDataBuilder.createAdmin();
        adminUser.setId(adminUserId);
    }

    @Test
    void getCurrentUser_WithValidUserId_ShouldReturnUserResponse() {
        // Given
        when(userRepository.findByIdAndIsActiveTrue(testUserId)).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = userService.getCurrentUser(testUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(result.getIsActive()).isEqualTo(testUser.getIsActive());
        assertThat(result.getIsEmailVerified()).isEqualTo(testUser.getIsEmailVerified());
    }

    @Test
    void getCurrentUser_WithInvalidUserId_ShouldThrowException() {
        // Given
        when(userRepository.findByIdAndIsActiveTrue(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getCurrentUser(testUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateCurrentUser_WithValidData_ShouldUpdateAndReturnUser() {
        // Given
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");
        request.setLastName("Name");
        request.setPhoneNumber("+9876543210");

        when(userRepository.findByIdAndIsActiveTrue(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateCurrentUser(testUserId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(testUser.getFirstName()).isEqualTo("Updated");
        assertThat(testUser.getLastName()).isEqualTo("Name");
        assertThat(testUser.getPhoneNumber()).isEqualTo("+9876543210");
        verify(userRepository).save(testUser);
    }

    @Test
    void updateCurrentUser_WithNullValues_ShouldNotUpdateNullFields() {
        // Given
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");
        // lastName and phoneNumber are null

        String originalLastName = testUser.getLastName();
        String originalPhoneNumber = testUser.getPhoneNumber();

        when(userRepository.findByIdAndIsActiveTrue(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateCurrentUser(testUserId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(testUser.getFirstName()).isEqualTo("Updated");
        assertThat(testUser.getLastName()).isEqualTo(originalLastName);
        assertThat(testUser.getPhoneNumber()).isEqualTo(originalPhoneNumber);
        verify(userRepository).save(testUser);
    }

    @Test
    void getUserById_WithValidUserId_ShouldReturnUserResponse() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = userService.getUserById(testUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void getUserById_WithInvalidUserId_ShouldThrowException() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(testUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateUserStatus_WithValidData_ShouldUpdateStatus() {
        // Given
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UpdateUserStatusRequest.UserStatus.SUSPENDED);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));

        // When
        userService.updateUserStatus(testUserId, request, adminUserId);

        // Then
        assertThat(testUser.getStatus()).isEqualTo(User.UserStatus.SUSPENDED);
        assertThat(testUser.getIsActive()).isFalse();
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUserStatus_WithActiveStatus_ShouldSetIsActiveTrue() {
        // Given
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UpdateUserStatusRequest.UserStatus.ACTIVE);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));

        // When
        userService.updateUserStatus(testUserId, request, adminUserId);

        // Then
        assertThat(testUser.getStatus()).isEqualTo(User.UserStatus.ACTIVE);
        assertThat(testUser.getIsActive()).isTrue();
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUserStatus_WithDisabledStatus_ShouldSetIsActiveFalse() {
        // Given
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UpdateUserStatusRequest.UserStatus.DISABLED);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));

        // When
        userService.updateUserStatus(testUserId, request, adminUserId);

        // Then
        assertThat(testUser.getStatus()).isEqualTo(User.UserStatus.DISABLED);
        assertThat(testUser.getIsActive()).isFalse();
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUserStatus_WithNonExistentUser_ShouldThrowException() {
        // Given
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UpdateUserStatusRequest.UserStatus.SUSPENDED);

        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUserStatus(testUserId, request, adminUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateUserStatus_WithNonExistentAdmin_ShouldThrowException() {
        // Given
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UpdateUserStatusRequest.UserStatus.SUSPENDED);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(adminUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUserStatus(testUserId, request, adminUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Admin user not found");
    }

    @Test
    void isUserInSameOrganization_ShouldReturnTrue() {
        // Given & When
        boolean result = userService.isUserInSameOrganization(adminUserId, testUserId);

        // Then
        assertThat(result).isTrue(); // Current implementation always returns true
    }
}