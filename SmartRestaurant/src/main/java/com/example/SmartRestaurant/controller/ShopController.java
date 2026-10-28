package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.ShopStatus;
import com.example.SmartRestaurant.dto.request.ReasonRequest;
import com.example.SmartRestaurant.dto.request.ShopLocationRequest;
import com.example.SmartRestaurant.dto.request.ShopRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.ShopResponse;
import com.example.SmartRestaurant.service.shop.ShopService;
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
import java.util.List;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/shop")
public class ShopController {
    ShopService shopService;

    @PostMapping
    @Operation(summary = "Tạo shop mới")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(
            @RequestBody ShopRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , shopService.create(request)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy tất cả các shop của người dùng")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<List<ShopResponse>>> getMyShops() {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , shopService.getAllShopOfCurrentUser()
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/me/{id}")
    @Operation(summary = "Lấy thông tin shop theo shopId của người dùng")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<ShopResponse>> getMyShops(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , shopService.getShopByIdOfCurrentUser(id)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{shopId}")
    @Operation(summary = "Người dùng tự sửa thông tin shop")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(
            @PathVariable Long shopId,
            @RequestBody ShopRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , shopService.update(shopId, request)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{shopId}/location")
    @Operation(summary = "Người dùng chọn địa chỉ cho shop trên bản đồ thật")
    @PreAuthorize("hasRole('OWNER') and hasAuthority('PERM_SHOP_SET_LOCATION')")
    public ResponseEntity<ApiResponse<ShopResponse>> setLocation(
            @PathVariable Long shopId,
            @RequestBody ShopLocationRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , shopService.setLocation(shopId, request)
                , LocalDateTime.now()
        ));
    }

    @GetMapping
    @Operation(summary = "Admin xem tất cả các shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ShopResponse>>> getAll(
            Pageable pageable
            , @RequestParam(required = false) ShopStatus status
    ) {
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , shopService.getAll(pageable, status)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Admin xem thông tin shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShopResponse>> getById(
            @PathVariable Long shopId
    ) {
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , shopService.getById(shopId)
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shopId}/approve")
    @Operation(summary = "Admin duyệt shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> approve(
            @PathVariable("shopId") Long shopId
    ) {
        shopService.approve(shopId);
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shopId}/reject")
    @Operation(summary = "Admin từ chối shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> reject(
            @PathVariable("shopId") Long shopId,
            @RequestBody ReasonRequest reason) {
        shopService.reject(shopId, reason);
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shopId}/lock")
    @Operation(summary = "Admin khóa shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> lock(
            @PathVariable("shopId") Long shopId,
            @RequestBody ReasonRequest reason) {
        shopService.lock(shopId, reason);
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{shopId}/unlock")
    @Operation(summary = "Admin mở khóa shop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> unlock(
            @PathVariable("shopId") Long shopId
    ) {
        shopService.unlock(shopId);
        return ResponseEntity.ok(new ApiResponse(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }


}
