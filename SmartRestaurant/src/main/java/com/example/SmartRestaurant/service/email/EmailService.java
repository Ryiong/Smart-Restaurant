package com.example.SmartRestaurant.service.email;

import com.example.SmartRestaurant.dto.request.MailRequest;


public interface EmailService {
    void sendOtpEmail(MailRequest mailRequest);

    void sendResetPasswordEmail(MailRequest mailRequest);
}
