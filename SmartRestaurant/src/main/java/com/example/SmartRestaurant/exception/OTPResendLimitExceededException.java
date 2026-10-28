package com.example.SmartRestaurant.exception;

public class OTPResendLimitExceededException extends AppException {
    public OTPResendLimitExceededException() {
        super("Bạn đã đạt giới hạn gửi lại OTP. Vui lòng thử lại sau.");
    }
}
