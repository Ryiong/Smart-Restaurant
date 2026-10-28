package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.AttendanceRequest;
import com.example.SmartRestaurant.dto.response.AttendanceResponse;
import com.example.SmartRestaurant.entity.AttendanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceMapper {
    UserMapper userMapper;
    WorkScheduleMapper workScheduleMapper;

    public AttendanceEntity toEntity(AttendanceRequest request) {
        return AttendanceEntity.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }

    public AttendanceResponse toResponse(AttendanceEntity attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .createdAt(attendance.getCreatedAt())
                .user(userMapper.toResponse(attendance.getWorkSchedule().getEmployment().getUser()))
                .workSchedule(workScheduleMapper.toResponse(attendance.getWorkSchedule()))
                .type(attendance.getType())
                .build();
    }
}
