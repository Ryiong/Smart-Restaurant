package com.example.SmartRestaurant.service.user;

import com.example.SmartRestaurant.dto.request.ActivateRequest;
import com.example.SmartRestaurant.dto.request.LoginRequest;
import com.example.SmartRestaurant.dto.request.RegisterRequest;
import com.example.SmartRestaurant.dto.response.LoginResponse;
import com.example.SmartRestaurant.dto.response.UserResponse;

public interface UserService {
    void register(RegisterRequest registerRequest);

    void resendOTP(String email);

    void activateAccount(ActivateRequest activateRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponse getCurrentUser();

    String refresh(String refreshToken);

    void logout(String refreshToken);
}
