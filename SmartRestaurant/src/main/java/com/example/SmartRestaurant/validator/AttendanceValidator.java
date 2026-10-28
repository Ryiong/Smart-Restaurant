package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.AttendanceRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class AttendanceValidator {
    public static void validateRequest(AttendanceRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin chấm công không hơp lệ");
        }
        if (request.getWorkScheduleId() == null) {
            throw new ValidateException("Ca làm không được bỏ trống");
        }
        if (request.getShopId() == null) {
            throw new ValidateException("Cửa hàng không được bỏ trống");
        }
        if (request.getLatitude() == null || request.getLatitude() == null) {
            throw new ValidateException("Vị trí không được bỏ trống");
        }
        if (request.getLongitude() == null || request.getLongitude() < -180 || request.getLongitude() > 180) {
            throw new ValidateException("Kinh độ không hợp lệ");
        }
        if (request.getLatitude() == null || request.getLatitude() < -90 || request.getLatitude() > 90) {
            throw new ValidateException("Vĩ độ không hợp lệ");
        }
        if (request.getQrExpiredAt() == null) {
            throw new ValidateException("Thời điểm qr hết hạn không được bỏ trống");
        }
    }
}
