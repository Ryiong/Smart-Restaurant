package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.WorkScheduleRequest;
import com.example.SmartRestaurant.dto.response.WorkScheduleAttendanceResponse;
import com.example.SmartRestaurant.dto.response.WorkScheduleResponse;
import com.example.SmartRestaurant.entity.WorkScheduleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkScheduleMapper {
    ShiftMapper shiftMapper;
    EmploymentMapper employmentMapper;

    AttendanceMapper attendanceMapper;

    public WorkScheduleEntity toEntity(WorkScheduleRequest request) {
        return WorkScheduleEntity.builder()
                .date(request.getDate())
                .build();
    }

    public WorkScheduleResponse toResponse(WorkScheduleEntity workSchedule) {
        return WorkScheduleResponse.builder()
                .id(workSchedule.getId())
                .date(workSchedule.getDate())
                .shift(shiftMapper.toResponse(workSchedule.getShift()))
                .employmentResponse(employmentMapper.toResponse(workSchedule.getEmployment()))
                .build();
    }

    public WorkScheduleAttendanceResponse toAttendancResponse(WorkScheduleEntity workSchedule) {
        return WorkScheduleAttendanceResponse.builder()
                .id(workSchedule.getId())
                .date(workSchedule.getDate())
                .shift(shiftMapper.toResponse(workSchedule.getShift()))
                .employment(employmentMapper.toResponse(workSchedule.getEmployment()))
                .attendances(workSchedule.getAttendances()
                        .stream()
                        .map(x -> attendanceMapper.toResponse(x))
                        .collect(Collectors.toList()))
                .build();
    }
}
