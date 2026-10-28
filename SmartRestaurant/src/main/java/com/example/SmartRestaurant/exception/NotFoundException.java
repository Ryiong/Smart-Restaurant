package com.example.SmartRestaurant.exception;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message + " không tồn tại");
    }
}
