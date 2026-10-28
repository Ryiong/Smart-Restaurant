package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.ReasonRequest;
import com.example.SmartRestaurant.dto.request.ShopLocationRequest;
import com.example.SmartRestaurant.dto.request.ShopRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class ShopValidator {
    public static void validateShopRequest(ShopRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên shop không được để trống");
        }
    }

    public static void validateReject(ReasonRequest reason) {
        if (reason.getReason() == null || reason.getReason().isBlank()) {
            throw new ValidateException("Lý do từ chối không được để trống");
        }
    }

    public static void validateLock(ReasonRequest reason) {
        if (reason.getReason() == null || reason.getReason().isBlank()) {
            throw new ValidateException("Lý do khóa không được để trống");
        }
    }

    public static void validateShopLocationRequest(ShopLocationRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin địa chỉ không hợp lệ");
        }
        if (request.getLongitude() == null || request.getLongitude() < -180 || request.getLongitude() > 180) {
            throw new ValidateException("Kinh độ không hợp lệ");
        }
        if (request.getLatitude() == null || request.getLatitude() < -90 || request.getLatitude() > 90) {
            throw new ValidateException("Vĩ độ không hợp lệ");
        }
        if (request.getAttendanceRadius() < 0) {
            throw new ValidateException("Khoảng cách cho phép chấm công không được âm");
        }
    }
}
