package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.HostDeviceRequest;
import com.example.SmartRestaurant.dto.response.HostDeviceResponse;
import com.example.SmartRestaurant.entity.HostDeviceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostDeviceMapper {
    ShopMapper shopMapper;

    public HostDeviceEntity toEntity(HostDeviceRequest request) {
        return HostDeviceEntity.builder()
                .name(request.getName())
                .build();
    }

    public HostDeviceResponse toResponse(HostDeviceEntity hostDevice) {
        return HostDeviceResponse.builder()
                .id(hostDevice.getId())
                .name(hostDevice.getName())
                .token(hostDevice.getToken())
                .status(hostDevice.getStatus())
                .build();
    }
}
