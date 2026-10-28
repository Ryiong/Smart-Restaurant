package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.EmploymentRehireRequest;
import com.example.SmartRestaurant.dto.request.EmploymentRequest;
import com.example.SmartRestaurant.exception.ValidateException;

import static com.example.SmartRestaurant.validator.AuthValidator.validateRegister;

public final class EmploymentValidator {
    public static void validateRequest(EmploymentRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin quan hệ nhân viên-shop không hợp lệ");
        }
        validateRegister(request.getRegisterRequest());
        if (request.getSalary() == null) {
            throw new ValidateException("Lương không được bỏ trống");
        }
        if (request.getSalary() <= 0) {
            throw new ValidateException("Lương phải lớn hơn 0");
        }
        if (request.getWorkFrom() == null) {
            throw new ValidateException("Ngày bắt đầu làm việc không được bỏ trống");
        }
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ValidateException("Danh sách chức vụ không được bỏ trống");
        }
    }

    public static void validateRehireRequest(EmploymentRehireRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin quan hệ nhân viên-shop không hợp lệ");
        }
        if (request.getSalary() == null) {
            throw new ValidateException("Lương không được bỏ trống");
        }
        if (request.getSalary() <= 0) {
            throw new ValidateException("Lương phải lớn hơn 0");
        }
        if (request.getWorkFrom() == null) {
            throw new ValidateException("Ngày bắt đầu làm việc không được bỏ trống");
        }
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ValidateException("Danh sách chức vụ không được bỏ trống");
        }
    }

    public static void validateUpdateRequest(EmploymentRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin quan hệ nhân viên-shop không hợp lệ");
        }
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ValidateException("Danh sách chức vụ không được bỏ trống");
        }
        if (request.getSalary() <= 0) {
            throw new ValidateException("Lương phải lớn hơn 0");
        }
    }
}
