package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.IngredientRequest;
import com.example.SmartRestaurant.dto.response.IngredientResponse;
import com.example.SmartRestaurant.entity.IngredientEntity;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public IngredientEntity toEntity(IngredientRequest request) {
        return IngredientEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .type(request.getType())
                .unit(request.getUnit())
                .minStock(request.getMinStock())
                .yieldRate(request.getYieldRate())
                .build();
    }

    public IngredientResponse toResponse(IngredientEntity ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .type(ingredient.getType())
                .unit(ingredient.getUnit())
                .minStock(ingredient.getMinStock())
                .yieldRate(ingredient.getYieldRate())
                .currentStock(ingredient.getCurrentStock())
                .status(ingredient.getStatus())
                .build();
    }
}
