package com.example.SmartRestaurant.service.email;

import com.example.SmartRestaurant.dto.request.MailRequest;
import com.example.SmartRestaurant.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.example.SmartRestaurant.common.Constant.sendOTP;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImplement implements EmailService {
    final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String fromMail;


    @Override
    public void sendOtpEmail(MailRequest mailRequest) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    "UTF-8"
            );

            helper.setFrom(fromMail);
            helper.setTo(mailRequest.getToMail());
            helper.setSubject("Smart Restaurant - Mã xác thực OTP");
            helper.setText(sendOTP
                    .replace(
                            "{{HO_TEN}}",
                            mailRequest.getToName()
                    )
                    .replace(
                            "{{MA_OTP}}",
                            mailRequest.getOTP()
                    ), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException("Đã có lỗi khi gửi mail: " + e.getMessage());
        }
    }

    @Override
    public void sendResetPasswordEmail(MailRequest mailRequest) {

    }
}
