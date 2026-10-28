package com.example.SmartRestaurant.service.recipeingredient;

import com.example.SmartRestaurant.dto.request.RecipeIngredientCreateRequest;
import com.example.SmartRestaurant.dto.request.RecipeIngredientUpdateRequest;
import com.example.SmartRestaurant.dto.response.RecipeIngredientResponse;

import java.util.List;

public interface RecipeIngredientService {
    List<RecipeIngredientResponse> create(Long recipeId, List<RecipeIngredientCreateRequest> requests);

    List<RecipeIngredientResponse> update(Long recipeId, List<RecipeIngredientUpdateRequest> recipeIngredientRequestList);

    void delete(Long recipeId, List<Long> recipeIngredientIdList);

    List<RecipeIngredientResponse> getByRecipeId(Long recipeId);

}
