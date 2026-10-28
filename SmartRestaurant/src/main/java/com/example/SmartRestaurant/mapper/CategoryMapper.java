package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.CategoryRequest;
import com.example.SmartRestaurant.dto.response.CategoryResponse;
import com.example.SmartRestaurant.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(CategoryEntity category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .build();
    }
}
