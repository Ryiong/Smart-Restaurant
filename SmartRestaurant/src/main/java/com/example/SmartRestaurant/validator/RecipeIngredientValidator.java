package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.RecipeIngredientCreateRequest;
import com.example.SmartRestaurant.dto.request.RecipeIngredientUpdateRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import java.math.BigDecimal;
import java.util.List;

public final class RecipeIngredientValidator {
    public static void validateRecipeIngredientRequest(RecipeIngredientCreateRequest request) {
        if (request.getIngredientId() == null) {
            throw new ValidateException("Nguyên liệu không được bỏ trống");
        }
        if (request.getQuantity() == null) {
            throw new ValidateException("Số lượng không được bỏ trống");
        }
        if (request.getQuantity().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new ValidateException("Số lượng phải lớn hơn 0");
        }
    }

    public static void validateRecipeIngredientCreate(Long recipeId, List<RecipeIngredientCreateRequest> requests) {
        if (recipeId == null) {
            throw new ValidateException("Id công thức không được trống");
        }
        if (requests == null || requests.isEmpty()) {
            throw new ValidateException("Danh sách nguyên liệu không được bỏ trống");
        }
        requests.forEach(x -> {
            if (x == null) {
                throw new ValidateException("Dữ liệu định lượng không hợp lệ");
            }
            if (x.getIngredientId() == null || x.getIngredientId() <= 0) {
                throw new ValidateException("Id định lượng không hợp lệ");
            }
            if (x.getQuantity() == null || x.getQuantity().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new ValidateException("Định lượng phải lớn hơn 0");
            }
        });
    }

    public static void validateRecipeIngredientUpdate(Long recipeId, List<RecipeIngredientUpdateRequest> requests) {
        if (recipeId == null) {
            throw new ValidateException("Id công thức không được bỏ trống");
        }
        if (requests == null || requests.isEmpty()) {
            throw new ValidateException("Danh sách không được bỏ trống");
        }
        requests.forEach(x -> {
            if (x == null) {
                throw new ValidateException("Dữ liệu định lượng không hợp lệ");
            }
            if (x.getId() == null || x.getId() <= 0) {
                throw new ValidateException("Id định lượng không hợp lệ");
            }
            if (x.getQuantity() == null || x.getQuantity().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new ValidateException("Định lượng phải lớn hơn 0");
            }
        });
    }

    public static void validateRecipeIngredientDelete(Long recipeId, List<Long> requestIds) {
        if (recipeId == null) {
            throw new ValidateException("Id công thức không được bỏ trống");
        }
        if (requestIds == null || requestIds.isEmpty()) {
            throw new ValidateException("Danh sách id không được bỏ trống");
        }
        requestIds.forEach(x -> {
            if (x == null || x <= 0) {
                throw new ValidateException("Id không hợp lệ");
            }
        });
    }
}
