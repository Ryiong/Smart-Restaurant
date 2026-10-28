package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.HostDeviceRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.HostDeviceResponse;
import com.example.SmartRestaurant.service.hostdevice.HostDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/host-device")
public class HostDeviceController {
    HostDeviceService service;

    @PostMapping("/{shopId}/register")
    @Operation(summary = "Đăng ký máy chủ vật lý")
    @PreAuthorize("hasAuthority('PERM_HOST_DEVICE_REGISTER')")
    ResponseEntity<ApiResponse<HostDeviceResponse>> register(
            @PathVariable Long shopId,
            @RequestBody HostDeviceRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201,
                "Thành công",
                service.create(shopId, request),
                LocalDateTime.now()
        ));
    }
}
