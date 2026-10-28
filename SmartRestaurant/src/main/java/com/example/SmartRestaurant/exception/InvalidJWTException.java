package com.example.SmartRestaurant.exception;

public class InvalidJWTException extends AppException {
    public InvalidJWTException() {
        super("Token không hợp lệ");
    }
}
