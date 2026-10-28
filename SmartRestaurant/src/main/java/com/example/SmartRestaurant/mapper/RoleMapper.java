package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.RoleRequest;
import com.example.SmartRestaurant.dto.response.RoleResponse;
import com.example.SmartRestaurant.entity.RoleEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleMapper {
    PermissionMapper permissionMapper;

    public RoleEntity toEntity(RoleRequest request) {
        return RoleEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .build();
    }

    public RoleResponse toResponse(RoleEntity role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions().stream().map(permissionMapper::toResponse).toList())
                .build();
    }
}
