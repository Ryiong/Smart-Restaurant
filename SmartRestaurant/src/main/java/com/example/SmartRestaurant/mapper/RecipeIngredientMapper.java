package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.RecipeIngredientCreateRequest;
import com.example.SmartRestaurant.dto.response.RecipeIngredientResponse;
import com.example.SmartRestaurant.entity.RecipeIngredientEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientMapper {
    public RecipeIngredientEntity toEntity(RecipeIngredientCreateRequest request) {
        return RecipeIngredientEntity.builder()
                .quantity(request.getQuantity())
                .build();
    }

    public RecipeIngredientResponse toResponse(RecipeIngredientEntity entity) {
        return RecipeIngredientResponse.builder()
                .id(entity.getId())
                .ingredientName(entity.getIngredient().getName())
                .ingredientUnit(entity.getIngredient().getUnit())
                .quantity(entity.getQuantity())
                .build();
    }
}
