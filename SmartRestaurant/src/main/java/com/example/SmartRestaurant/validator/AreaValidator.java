package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.AreaRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class AreaValidator {
    public static void validateAreaRequest(AreaRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên khu vực không được để trống");
        }
        if (request.getName().length() > 100) {
            throw new ValidateException("Tên khu vực không được vượt quá 100 kí tự");
        }
    }
}
