package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.AttendanceRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.AttendanceResponse;
import com.example.SmartRestaurant.service.attendance.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/attendance")
public class AttendanceController {
    AttendanceService attendanceService;

    @PostMapping("/check-attendance")
    @Operation(summary = "Chấm công")
    @PreAuthorize("hasAuthority('PERM_CHECK_ATTENDANCE')")
    ResponseEntity<ApiResponse<AttendanceResponse>> checkAttendance(
            @RequestBody AttendanceRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                attendanceService.checkAttendance(request),
                LocalDateTime.now()
        ));
    }

}
