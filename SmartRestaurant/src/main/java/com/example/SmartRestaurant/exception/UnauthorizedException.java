package com.example.SmartRestaurant.exception;

public class UnauthorizedException extends AppException {
    public UnauthorizedException() {
        super("Email hoặc mật khẩu không chính xác");
    }
}
