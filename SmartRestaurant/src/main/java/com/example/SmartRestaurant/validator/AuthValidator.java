package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.ActivateRequest;
import com.example.SmartRestaurant.dto.request.LoginRequest;
import com.example.SmartRestaurant.dto.request.RegisterRequest;
import com.example.SmartRestaurant.exception.ValidateException;
import jakarta.validation.ValidationException;

public final class AuthValidator {
    public static void validateRegister(RegisterRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidateException("Tên không được bỏ trống");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidateException("Email không được bỏ trống");
        }

        if (!request.getEmail().trim().endsWith("@gmail.com")) {
            throw new ValidateException("Email không hợp lệ");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ValidateException("Mật khẩu không được bỏ trống");
        }
        if (request.getPassword().length() < 8) {
            throw new ValidateException("Mật khẩu phải có từ 8 kí tự trở lên");
        }
    }

    public static void validateResendOTP(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidateException("Email không được bỏ trống");
        }

        if (!email.trim().endsWith("@gmail.com")) {
            throw new ValidateException("Email không hợp lệ");
        }
    }

    public static void validateActivateAccount(ActivateRequest activateRequest) {
        if (activateRequest.getCode() == null || activateRequest.getCode().isBlank()) {
            throw new ValidateException("Mã OTP không được bỏ trống");
        }
        if (activateRequest.getCode().length() < 6) {
            throw new ValidateException("Mã OTP không hợp lệ");
        }
    }

    public static void validateLogin(LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
            throw new ValidationException("Email không được để trống");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống");
        }
    }
}
