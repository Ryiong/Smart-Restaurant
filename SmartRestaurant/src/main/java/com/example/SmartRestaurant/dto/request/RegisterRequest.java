package com.example.SmartRestaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

}
