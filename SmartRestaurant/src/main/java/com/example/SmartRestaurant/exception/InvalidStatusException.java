package com.example.SmartRestaurant.exception;

public class InvalidStatusException extends AppException {
    public InvalidStatusException(String message) {
        super("Trạng thái " + message + " không được chấp thuận");
    }
}
