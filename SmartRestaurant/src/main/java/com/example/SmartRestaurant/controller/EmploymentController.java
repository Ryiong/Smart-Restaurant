package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.EmploymentRehireRequest;
import com.example.SmartRestaurant.dto.request.EmploymentRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.EmploymentResponse;
import com.example.SmartRestaurant.service.employment.EmploymentService;
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
@RequestMapping(URL + "/employment")
public class EmploymentController {
    EmploymentService employmentService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Thêm mới quan hệ giữa nhân viên và shop")
    @PreAuthorize("hasAuthority('PERM_EMPLOYMENT_CREATE')")
    ResponseEntity<ApiResponse<EmploymentResponse>> create(
            @PathVariable Long shopId,
            @RequestBody EmploymentRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201,
                "Thành công",
                employmentService.create(request, shopId),
                LocalDateTime.now()
        ));
    }

    @PutMapping("/{employmentId}")
    @Operation(summary = "Sửa quan hệ giữa nhân viên và shop")
    @PreAuthorize("hasAuthority('PERM_EMPLOYMENT_UPDATE')")
    ResponseEntity<ApiResponse<EmploymentResponse>> update(
            @PathVariable Long employmentId,
            @RequestBody EmploymentRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                employmentService.update(employmentId, request),
                LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{employmentId}")
    @Operation(summary = "Xóa quan hệ")
    @PreAuthorize("hasAuthority('PERM_EMPLOYMENT_TERMINATE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long employmentId
    ) {
        employmentService.delete(employmentId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/existing-users/{userId}")
    @Operation(summary = "Tạo quan hệ mới với nhân viên cũ")
    @PreAuthorize("hasAuthority('PERM_EMPLOYMENT_CREATE')")
    public ResponseEntity<ApiResponse<EmploymentResponse>> createFromExistingUser(
            @PathVariable("shopId") Long shopId,
            @PathVariable("userId") Long userId,
            @RequestBody EmploymentRehireRequest request
    ) {

        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , employmentService.createFromExistingUser(shopId, userId, request)
                , LocalDateTime.now()
        ));
    }
}
