package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.ShiftRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class ShiftValidator {
    public static void validateRequest(ShiftRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin ca làm không hợp lệ");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên ca làm không được bỏ trống");
        }
        if (request.getStartTime() == null) {
            throw new ValidateException("Thời gian bắt đầu không được bỏ trống");
        }
        if (request.getEndTime() == null) {
            throw new ValidateException("Thời gian bắt đầu không được bỏ trống");
        }
    }
}
