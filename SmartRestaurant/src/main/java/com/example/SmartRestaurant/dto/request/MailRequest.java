package com.example.SmartRestaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailRequest {
    private String toMail;
    private String OTP;
    private String toName;
}
