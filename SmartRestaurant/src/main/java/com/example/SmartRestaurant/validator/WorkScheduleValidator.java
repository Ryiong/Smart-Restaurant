package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.WorkScheduleActionRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleExplainRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleUpdateRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import java.time.LocalDate;

public final class WorkScheduleValidator {
    public static void validateRequest(WorkScheduleRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin phân công không hợp lệ");
        }
        if (request.getDate().isBefore(LocalDate.now())) {
            throw new ValidateException("Không thể phân công cho ngày cũ");
        }
        if (request.getShiftId() == null) {
            throw new ValidateException("Ca làm không được bỏ trống");
        }
        if (request.getEmploymentId() == null) {
            throw new ValidateException("Nhân viên không được bỏ trống");
        }
    }

    public static void validateUpdateRequest(WorkScheduleUpdateRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin phân công không hợp lệ");
        }
        if (request.getShiftId() == null) {
            throw new ValidateException("Ca làm không được bỏ trống");
        }
    }

    public static void validateWorkScheduleActionRequest(WorkScheduleActionRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin cập nhật trạng thái công không hợp lệ");
        }
        if (request.getId() == null) {
            throw new ValidateException("Id công không được bỏ trống");
        }
        if (request.getStatus() == null) {
            throw new ValidateException("Trạng thái công không được bỏ trống");
        }
    }

    public static void validateWorkScheduleExplainRequest(WorkScheduleExplainRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin giải trình công không hợp lệ");
        }
        if (request.getExplaination() == null || request.getExplaination().isBlank()) {
            throw new ValidateException("Lý do giải trình không được bỏ trống");
        }
    }
}
