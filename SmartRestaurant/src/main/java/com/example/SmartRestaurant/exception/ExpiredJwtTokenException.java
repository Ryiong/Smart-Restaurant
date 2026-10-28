package com.example.SmartRestaurant.exception;

public class ExpiredJwtTokenException extends AppException {
    public ExpiredJwtTokenException(String message) {
        super(message + " token đã hết hạn");
    }
}
