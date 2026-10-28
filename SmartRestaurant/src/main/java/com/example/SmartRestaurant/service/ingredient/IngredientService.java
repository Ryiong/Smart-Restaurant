package com.example.SmartRestaurant.service.ingredient;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.dto.request.IngredientRequest;
import com.example.SmartRestaurant.dto.response.IngredientResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredientService extends ParentResourceBaseService<IngredientRequest, IngredientResponse, Long> {
    Page<IngredientResponse> getAllByShopId(Long shopId, IngredientStatus status, Pageable pageable);

    void activate(Long ingredientId);

}
