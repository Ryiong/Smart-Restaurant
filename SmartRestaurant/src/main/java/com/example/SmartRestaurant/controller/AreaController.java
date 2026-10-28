package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.AreaStatus;
import com.example.SmartRestaurant.dto.request.AreaRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.AreaResponse;
import com.example.SmartRestaurant.service.area.AreaService;
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
@RequestMapping(URL + "/area")
public class AreaController {
    AreaService areaService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo khu vực")
    @PreAuthorize("hasAuthority('PERM_AREA_CREATE')")
    public ResponseEntity<ApiResponse<AreaResponse>> create(
            @PathVariable Long shopId,
            @RequestBody AreaRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , areaService.create(request, shopId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Tìm tất cả khu vực theo shopId")
    @PreAuthorize("hasAuthority('PERM_AREA_VIEW')")
    public ResponseEntity<ApiResponse<Page<AreaResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) AreaStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , areaService.getAllByShopId(shopId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{areaId}")
    @Operation(summary = "Tìm khu vực theo id")
    @PreAuthorize("hasAuthority('PERM_AREA_VIEW')")
    public ResponseEntity<ApiResponse<AreaResponse>> getById(
            @PathVariable Long areaId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , areaService.getById(areaId)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{areaId}")
    @Operation(summary = "Sửa thông tin khu vực")
    @PreAuthorize("hasAuthority('PERM_AREA_UPDATE')")
    public ResponseEntity<ApiResponse<AreaResponse>> update(
            @PathVariable Long areaId,
            @RequestBody AreaRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , areaService.update(areaId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{areaId}")
    @Operation(summary = "Xóa khu vực")
    @PreAuthorize("hasAuthority('PERM_AREA_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long areaId
    ) {
        areaService.delete(areaId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{areaId}/activate")
    @Operation(summary = "Bật lại khu vực")
    @PreAuthorize("hasAuthority('PERM_AREA_ACTIVATE')")
    public ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long areaId
    ) {
        areaService.activate(areaId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
