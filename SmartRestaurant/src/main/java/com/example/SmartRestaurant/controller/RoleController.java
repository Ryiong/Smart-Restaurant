package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.Constant;
import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.dto.request.RoleRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.RoleResponse;
import com.example.SmartRestaurant.service.role.RoleService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(Constant.URL + "/role")
public class RoleController {
    RoleService roleService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Thêm role vào shop")
    @PreAuthorize("hasRole('OWNER')")
    ResponseEntity<ApiResponse<RoleResponse>> create(
            @PathVariable Long shopId,
            @RequestBody RoleRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201,
                "Thành công",
                roleService.create(request, shopId),
                LocalDateTime.now()
        ));
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "Sửa role")
    @PreAuthorize("hasRole('OWNER')")
    ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable Long roleId,
            @RequestBody RoleRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                roleService.update(roleId, request),
                LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "Xóa role")
    @PreAuthorize("hasRole('OWNER')")
    ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long roleId
    ) {
        roleService.delete(roleId);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                null,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "Xem thông tỉn role")
    @PreAuthorize("hasRole('OWNER')")
    ResponseEntity<ApiResponse<RoleResponse>> getById(
            @PathVariable Long roleId
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                roleService.getById(roleId),
                LocalDateTime.now()
        ));
    }

    @GetMapping("/roles/{shopId}")
    @Operation(summary = "Xem các role trong shop")
    @PreAuthorize("hasRole('OWNER')")
    ResponseEntity<ApiResponse<Page<RoleResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) RoleStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                roleService.getAllByShopId(shopId, status, pageable),
                LocalDateTime.now()
        ));
    }
}
