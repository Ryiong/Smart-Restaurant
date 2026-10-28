package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.ShiftRequest;
import com.example.SmartRestaurant.dto.response.ShiftResponse;
import com.example.SmartRestaurant.entity.ShiftEntity;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {
    public ShiftEntity toEntity(ShiftRequest request) {
        return ShiftEntity.builder()
                .name(request.getName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }

    public ShiftResponse toResponse(ShiftEntity shift) {
        return ShiftResponse.builder()
                .name(shift.getName())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .status(shift.getStatus())
                .build();
    }
}
