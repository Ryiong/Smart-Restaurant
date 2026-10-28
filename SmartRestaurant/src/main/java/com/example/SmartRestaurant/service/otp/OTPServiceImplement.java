package com.example.SmartRestaurant.service.otp;

import com.example.SmartRestaurant.common.enums.OTPStatus;
import com.example.SmartRestaurant.dto.request.MailRequest;
import com.example.SmartRestaurant.entity.OTPEntity;
import com.example.SmartRestaurant.exception.*;
import com.example.SmartRestaurant.repository.OTPRepository;
import com.example.SmartRestaurant.service.email.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OTPServiceImplement implements OTPService {
    OTPRepository repository;
    EmailService emailService;

    int maxResendCount = 5;

    @Override
    public void save(OTPEntity otpEntity) {
        otpEntity.setCode(generateOTP());
        otpEntity.setResendCount(0);
        otpEntity.setStatus(OTPStatus.PENDING);
        repository.save(otpEntity);
        emailService.sendOtpEmail(new MailRequest(
                otpEntity.getUser().getEmail()
                , otpEntity.getCode()
                , otpEntity.getUser().getName())
        );
    }

    @Override
    public void resendOTP(Long userId) {
        OTPEntity oldOTP = repository.findByUserId(userId);
        if (oldOTP == null) {
            throw new NotFoundException("Mã OTP");
        }
        LocalDateTime now = LocalDateTime.now();

        //Nếu vẫn còn trong thời gian bị chặn thì không cho resend
        if (oldOTP.getLockedUntil() != null
                && oldOTP.getLockedUntil().isAfter(now)) {
            throw new OTPResendLimitExceededException();
        }

        //Nếu thời gian chặn đã hết thì reset
        if (oldOTP.getLockedUntil() != null
                && !oldOTP.getLockedUntil().isAfter(now)) {
            oldOTP.setLockedUntil(null);
            oldOTP.setResendCount(0);
        }

        //Mỗi lần resend phải cách nhau ít nhất 60 giây
        if (oldOTP.getLastSentAt() != null
                && oldOTP.getLastSentAt().plusSeconds(60).isAfter(now)) {
            throw new OTPRateLimitException();
        }

        //Đã resend đủ số lần tối đa thì chặn 15 phút
        if (oldOTP.getResendCount() >= maxResendCount) {
            oldOTP.setLockedUntil(now.plusMinutes(15));
            repository.save(oldOTP);
            throw new OTPResendLimitExceededException();
        }
        oldOTP.setResendCount(oldOTP.getResendCount() + 1);
        oldOTP.setLastSentAt(now);
        oldOTP.setCode(generateOTP());
        oldOTP.setExpiredAt(now.plusMinutes(5));
        repository.save(oldOTP);
        emailService.sendOtpEmail(new MailRequest(
                oldOTP.getUser().getEmail()
                , oldOTP.getCode()
                , oldOTP.getUser().getName())
        );
    }

    @Override
    public void activate(Long userId, String code) {
        OTPEntity otpEntity = repository.findByUserId(userId);
        if (otpEntity == null) {
            throw new NotFoundException("Mã OTP");
        }
        if (!code.equals(otpEntity.getCode())) {
            throw new InvalidOTPException();
        }
        if (!otpEntity.getExpiredAt().isAfter(LocalDateTime.now())) {
            throw new ExpiredOTPException();
        }
        otpEntity.setStatus(OTPStatus.VERIFIED);
        repository.save(otpEntity);
    }

    public String generateOTP() {
        SecureRandom SECURE_RANDOM = new SecureRandom();
        StringBuilder otp = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            otp.append(SECURE_RANDOM.nextInt(10));
        }
        return otp.toString();
    }
}
