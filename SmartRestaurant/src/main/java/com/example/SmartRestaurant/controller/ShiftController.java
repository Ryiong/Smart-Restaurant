package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.ShiftStatus;
import com.example.SmartRestaurant.dto.request.ShiftRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.ShiftResponse;
import com.example.SmartRestaurant.service.shift.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/shift")
public class ShiftController {
    ShiftService shiftService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo ca làm")
    @PreAuthorize("hasAuthority('PERM_SHIFT_CREATE')")
    public ResponseEntity<ApiResponse<ShiftResponse>> create(
            @PathVariable Long shopId,
            @RequestBody ShiftRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , shiftService.create(request, shopId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Tìm tất cả ca làm theo shopId")
    @PreAuthorize("hasAuthority('PERM_SHIFT_VIEW')")
    public ResponseEntity<ApiResponse<Page<ShiftResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) ShiftStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , shiftService.getAllByShopId(shopId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{shiftId}")
    @Operation(summary = "Xem thông tin ca làm")
    @PreAuthorize("hasAuthority('PERM_SHIFT_VIEW')")
    public ResponseEntity<ApiResponse<ShiftResponse>> getById(
            @PathVariable Long shiftId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , shiftService.getById(shiftId)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{shiftId}")
    @Operation(summary = "Sửa thông tin ca làm")
    @PreAuthorize("hasAuthority('PERM_SHIFT_UPDATE')")
    public ResponseEntity<ApiResponse<ShiftResponse>> update(
            @PathVariable Long shiftId,
            @RequestBody ShiftRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , shiftService.update(shiftId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{shiftId}")
    @Operation(summary = "Xóa ca làm")
    @PreAuthorize("hasAuthority('PERM_SHIFT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long shiftId
    ) {
        shiftService.delete(shiftId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shiftId}/activate")
    @Operation(summary = "Bật lại khu vực")
    @PreAuthorize("hasAuthority('PERM_SHIFT_ACTIVATE')")
    public ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long shiftId
    ) {
        shiftService.activate(shiftId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
