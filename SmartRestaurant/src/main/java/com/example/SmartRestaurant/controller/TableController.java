package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.TableStatus;
import com.example.SmartRestaurant.dto.request.TableRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.TableResponse;
import com.example.SmartRestaurant.service.table.TableService;
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
@RequestMapping(URL + "/tables")
public class TableController {
    TableService tableService;

    @PostMapping("/area/{areaId}")
    @Operation(summary = "Tao bàn theo khu vực")
    @PreAuthorize("hasAuthority('PERM_TABLE_CREATE')")
    ResponseEntity<ApiResponse<TableResponse>> create(
            @PathVariable Long areaId,
            @RequestBody TableRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , tableService.create(request, areaId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{tableId}")
    @Operation(summary = "Xem chi tiết bàn")
    @PreAuthorize("hasAuthority('PERM_TABLE_VIEW')")
    ResponseEntity<ApiResponse<TableResponse>> getById(
            @PathVariable Long tableId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , tableService.getById(tableId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/area/{areaId}")
    @Operation(summary = "Danh sách bàn trong khu vực")
    @PreAuthorize("hasAuthority('PERM_TABLE_VIEW')")
    ResponseEntity<ApiResponse<Page<TableResponse>>> getAllByAreaId(
            @PathVariable Long areaId
            , @RequestParam(required = false) TableStatus status
            , Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , tableService.getAllByAreaId(areaId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{tableId}")
    @Operation(summary = "Sửa bàn")
    @PreAuthorize("hasAuthority('PERM_TABLE_UPDATE')")
    ResponseEntity<ApiResponse<TableResponse>> update(
            @PathVariable Long tableId
            , @RequestBody TableRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , tableService.update(tableId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{tableId}")
    @Operation(summary = "Xóa bàn")
    @PreAuthorize("hasAuthority('PERM_TABLE_DELETE')")
    ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long tableId
    ) {
        tableService.delete(tableId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{tableId}/activate")
    @Operation(summary = "Mở lại bàn")
    @PreAuthorize("hasAuthority('PERM_TABLE_ACTIVATE'))")
    ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long tableId
    ) {
        tableService.activate(tableId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
