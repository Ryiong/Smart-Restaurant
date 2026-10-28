package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.InventoryDocumentItemRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import java.math.BigDecimal;

public final class InventoryDocumentItemValidator {
    public static void validateRequest(InventoryDocumentItemRequest request) {
        if (request == null) {
            throw new ValidateException(
                    "Thông tin nguyên liệu không hợp lệ"
            );
        }
        if (request.getIngredientId() == null || request.getIngredientId() <= 0) {
            throw new ValidateException("Id nguyên liệu không hợp lệ");
        }
        if (request.getQuantity() == null) {
            throw new ValidateException("Số lượng không được bỏ trống");
        }
        if (request.getQuantity().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new ValidateException("Số lượng phải lớn hơn 0");
        }
    }
}
