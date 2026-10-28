package com.example.SmartRestaurant.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PermissionResponse {
    private Long id;
    private String name;
}
