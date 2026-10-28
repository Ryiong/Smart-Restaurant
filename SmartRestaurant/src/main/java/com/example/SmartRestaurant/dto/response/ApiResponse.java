package com.example.SmartRestaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timeStamp;
}
