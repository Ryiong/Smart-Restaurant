package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.ActivateRequest;
import com.example.SmartRestaurant.dto.request.LoginRequest;
import com.example.SmartRestaurant.dto.request.RegisterRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.LoginResponse;
import com.example.SmartRestaurant.dto.response.UserResponse;
import com.example.SmartRestaurant.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/auth")
public class AuthController {
    UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Đăng kí tài khoản ")
    ResponseEntity<ApiResponse<?>> register(
            @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Đăng kí thành công.Vui lòng kiểm tra email của bạn"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Gửi lại mã OTP")
    ResponseEntity<ApiResponse<?>> register(
            @RequestBody String email) {
        userService.resendOTP(email);
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Gửi lại mã OTP thành công.Vui lòng kiểm tra email của bạn"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/activate-account")
    @Operation(summary = "Kích hoạt tài khoản")
    ResponseEntity<ApiResponse<?>> activate(
            @RequestBody ActivateRequest activateRequest) {
        userService.activateAccount(activateRequest);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Đã kích hoạt tài khoản thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập")
    ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Đăng nhập tài khoản thành công"
                , userService.login(loginRequest)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/me")
    @Operation(summary = "Xem thông tin user đang đăng nhập")
    ResponseEntity<ApiResponse<UserResponse>> me() {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , userService.getCurrentUser()
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Xin cấp access token mới")
    ResponseEntity<ApiResponse<String>> refresh(
            String refreshToken
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , userService.refresh(refreshToken)
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất")
    ResponseEntity<ApiResponse<?>> logout(
            String refreshToken
    ) {
        userService.logout(refreshToken);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
