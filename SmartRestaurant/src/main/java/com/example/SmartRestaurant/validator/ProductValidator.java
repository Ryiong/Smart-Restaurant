package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.ProductRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import java.math.BigDecimal;

public final class ProductValidator {
    public static void validateProductRequest(ProductRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên sản phẩm không được để trống");
        }
        if (request.getName().length() > 50) {
            throw new ValidateException("Tên sản phẩm không được vượt quá 50 kí tự");
        }
        if (request.getPrice() == null) {
            throw new ValidateException("Giá sản phẩm không được để trống");
        }
        if (request.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ValidateException("Giá sản phẩm không thể là số âm");
        }
    }
}
