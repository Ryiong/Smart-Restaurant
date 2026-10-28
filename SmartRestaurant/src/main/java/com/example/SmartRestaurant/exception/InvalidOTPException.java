package com.example.SmartRestaurant.exception;

public class InvalidOTPException extends AppException {
    public InvalidOTPException() {
        super("Mã OTP không hợp lệ");
    }
}
