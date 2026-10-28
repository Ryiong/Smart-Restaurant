package com.example.SmartRestaurant.exception;

public class OTPRateLimitException extends AppException {
    public OTPRateLimitException() {
        super("Vui lòng chờ trước khi thử lại");
    }
}
