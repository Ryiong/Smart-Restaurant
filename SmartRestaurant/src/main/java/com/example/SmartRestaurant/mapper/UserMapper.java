package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.RegisterRequest;
import com.example.SmartRestaurant.dto.response.UserResponse;
import com.example.SmartRestaurant.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(RegisterRequest registerRequest) {
        return UserEntity.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }

    public UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .type(user.getType())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
