package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.HostDeviceStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HostDeviceResponse {
    private Long id;
    private String name;
    private String token;
    private HostDeviceStatus status;
}
