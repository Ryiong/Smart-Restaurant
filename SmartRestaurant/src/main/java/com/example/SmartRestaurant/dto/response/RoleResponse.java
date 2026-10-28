package com.example.SmartRestaurant.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private List<PermissionResponse> permissions;
}
