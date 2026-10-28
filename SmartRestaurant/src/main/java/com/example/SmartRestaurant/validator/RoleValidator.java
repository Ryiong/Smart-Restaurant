package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.RoleRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class RoleValidator {
    public static void validateRequest(RoleRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin vai trò không hợp lệ");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên vai trò không được để trống");
        }
        if (request.getPermissionIds() == null || request.getPermissionIds().isEmpty()) {
            throw new ValidateException("Danh sách permisison không được bỏ trống");
        }
    }
}
