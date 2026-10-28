package com.example.SmartRestaurant.service.otp;

import com.example.SmartRestaurant.entity.OTPEntity;

public interface OTPService {
    void save(OTPEntity otpEntity);

    void resendOTP(Long userId);

    void activate(Long userId, String code);
}
