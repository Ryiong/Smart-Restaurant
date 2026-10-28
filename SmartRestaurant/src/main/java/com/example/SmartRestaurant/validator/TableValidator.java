package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.TableRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class TableValidator {
    public static void validateTableRequest(TableRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên bàn không được để trống");
        }
        if (request.getName().length() > 50) {
            throw new ValidateException("Tên bàn không được vượt quá 50 kí tự");
        }
    }
}
