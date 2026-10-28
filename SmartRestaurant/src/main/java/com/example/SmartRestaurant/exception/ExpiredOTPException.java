package com.example.SmartRestaurant.exception;

public class ExpiredOTPException extends AppException {
    public ExpiredOTPException() {
        super("Mã OTP đã hết hạn");
    }
}
