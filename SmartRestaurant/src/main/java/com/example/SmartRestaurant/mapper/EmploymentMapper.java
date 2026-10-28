package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.EmploymentRehireRequest;
import com.example.SmartRestaurant.dto.request.EmploymentRequest;
import com.example.SmartRestaurant.dto.response.EmploymentResponse;
import com.example.SmartRestaurant.entity.EmploymentEntity;
import org.springframework.stereotype.Component;

@Component
public class EmploymentMapper {
    public EmploymentEntity toEntity(EmploymentRequest request) {
        return EmploymentEntity.builder()
                .salary(request.getSalary())
                .workFrom(request.getWorkFrom())
                .build();
    }

    public EmploymentEntity toRehireEntity(EmploymentRehireRequest request) {
        return EmploymentEntity.builder()
                .salary(request.getSalary())
                .workFrom(request.getWorkFrom())
                .build();
    }

    public EmploymentResponse toResponse(EmploymentEntity employment) {
        return EmploymentResponse.builder()
                .id(employment.getId())
                .salary(employment.getSalary())
                .workFrom(employment.getWorkFrom())
                .endedAt(employment.getEndedAt())
                .status(employment.getStatus())
                .build();
    }
}
