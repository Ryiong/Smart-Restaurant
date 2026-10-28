package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.common.enums.IngredientType;
import com.example.SmartRestaurant.dto.request.IngredientRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import java.math.BigDecimal;

public final class IngredientValidator {
    public static void validateIngredientRequest(IngredientRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên nguyên liệu không được để trống");
        }
        if (request.getName().length() > 100) {
            throw new ValidateException("Tên nguyên liệu không được dài quá 100 kí tự");
        }
        if (request.getType() == null) {
            throw new ValidateException("Loại nguyên liệu không được để trống");
        }

        if (request.getUnit() == null || request.getUnit().isBlank()) {
            throw new ValidateException("Đơn vị nguyên liệu không được để trống");
        }

        if (request.getUnit().trim().length() > 20) {
            throw new ValidateException("Đơn vị không được vượt quá 20 ký tự");
        }

        if (request.getMinStock() == null) {
            throw new ValidateException("Tồn kho tối thiểu không được để trống");
        }

        if (request.getMinStock().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new ValidateException("Tồn kho tối thiểu không được nhỏ hơn 0");
        }

        if (request.getType() == IngredientType.FRESH) {
            if (request.getYieldRate() == null) {
                throw new ValidateException("Tỷ lệ thu hồi cùa nguyên liệu tươi không được để trống");
            }

            if (request.getYieldRate().compareTo(BigDecimal.valueOf(0)) <= 0
                    || request.getYieldRate().compareTo(BigDecimal.valueOf(1)) > 0) {
                throw new ValidateException("Tỷ lệ thu hồi phải lớn hơn 0 và nhỏ hơn hoặc bằng 1");
            }
        }
    }
}
