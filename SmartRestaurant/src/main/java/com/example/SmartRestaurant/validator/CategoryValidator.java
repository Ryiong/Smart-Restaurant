package com.example.SmartRestaurant.validator;


import com.example.SmartRestaurant.dto.request.CategoryRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class CategoryValidator {
    public static void validateCategoryRequest(CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên danh mục không được để trống");
        }
        if (request.getName().length() > 100) {
            throw new ValidateException("Tên danh mục không được vượt quá 100 kí tự");
        }
    }
}
