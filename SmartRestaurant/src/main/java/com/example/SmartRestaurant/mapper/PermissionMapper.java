package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.response.PermissionResponse;
import com.example.SmartRestaurant.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public PermissionResponse toResponse(PermissionEntity permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }
}
