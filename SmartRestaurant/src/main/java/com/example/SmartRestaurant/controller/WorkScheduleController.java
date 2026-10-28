package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.WorkScheduleActionRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleExplainRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleUpdateRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.WorkScheduleAttendanceResponse;
import com.example.SmartRestaurant.dto.response.WorkScheduleResponse;
import com.example.SmartRestaurant.service.workschedule.WorkScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/work-schedule")
public class WorkScheduleController {
    WorkScheduleService workScheduleService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Xếp lịch làm  theo ngày")
    @PreAuthorize("hasAuthority('PERM_WORK_SCHEDULE_CREATE')")
    ResponseEntity<ApiResponse<List<WorkScheduleResponse>>> create(
            @PathVariable Long shopId,
            @RequestParam LocalDate date,
            @RequestBody List<WorkScheduleRequest> requests
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201,
                "Thành công",
                workScheduleService.create(shopId, date, requests),
                LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shopId}")
    @Operation(summary = "Sửa lịch làm theo ngày của 1 nhân viên")
    @PreAuthorize("hasAuthority('PERM_WORK_SCHEDULE_UPDATE')")
    ResponseEntity<ApiResponse<WorkScheduleResponse>> update(
            @PathVariable Long shopId,
            @RequestParam Long workScheduleId,
            @RequestBody WorkScheduleUpdateRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                workScheduleService.update(shopId, workScheduleId, request),
                LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{workScheduleId}")
    @Operation(summary = "Xóa lịch làm của nhân viên theo id")
    @PreAuthorize("hasAuthority('PERM_WORK_SCHEDULE_DELETE')")
    ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long workScheduleId
    ) {
        workScheduleService.delete(workScheduleId);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                null,
                LocalDateTime.now()
        ));

    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Xem lịch làm việc theo ngày của shop")
    @PreAuthorize("hasAuthority('PERM_WORK_SCHEDULE_VIEW')")
    ResponseEntity<ApiResponse<Set<WorkScheduleResponse>>> getByShopIdAndDate(
            @PathVariable Long shopId,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                workScheduleService.getByShopIdAndDate(shopId, date),
                LocalDateTime.now()
        ));

    }

    @GetMapping("/{shopId}/attendance/{employmentId}")
    @Operation(summary = "Tìm tất cả các công làm việc của nhân viên trong cửa hàng trong khoảng thời gian")
    @PreAuthorize("hasAuthority('')")
    ResponseEntity<ApiResponse<List<WorkScheduleAttendanceResponse>>> getAllByShopIdAndEmploymentIdFromDateToDate(
            @PathVariable Long shopId,
            @PathVariable Long employmentId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                workScheduleService.getAllByShopIdAndEmploymentIdFromDateToDate(shopId,
                        employmentId,
                        startDate,
                        endDate),
                LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/update-attendance-status/{employmentId}")
    @Operation(summary = "Duyệt hoặc từ chối công theo danh sách công của nhân viên")
    @PreAuthorize("hasAuthority('')")
    ResponseEntity<ApiResponse<List<WorkScheduleAttendanceResponse>>> updateAttendanceStatus(
            @PathVariable Long shopId,
            @RequestBody List<WorkScheduleActionRequest> requests,
            @PathVariable Long employmentId
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                workScheduleService.updateAttendanceStatus(shopId, employmentId, requests),
                LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/explain/{workScheduleId}")
    @Operation(summary = "Giải trình công")
    @PreAuthorize("hasAuthority('')")
    ResponseEntity<ApiResponse<Void>> explain(
            @PathVariable Long shopId,
            @RequestBody WorkScheduleExplainRequest explainRequest,
            @PathVariable Long workScheduleId
    ) {
        workScheduleService.explain(shopId, workScheduleId, explainRequest);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Giải trình thành công",
                null,
                LocalDateTime.now()
        ));
    }
}
