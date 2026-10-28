package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.InventoryDocumentStatus;
import com.example.SmartRestaurant.dto.request.InventoryDocumentApproveRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRejectRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.InventoryDocumentResponse;
import com.example.SmartRestaurant.service.inventorydocument.InventoryDocumentService;
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
@RequestMapping(URL + "/inventory-document")
public class InventoryDocumentController {
    InventoryDocumentService inventoryDocumentService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo phiếu")
    @PreAuthorize("hasAuthority('PERM_INVENTORY_CREATE')")
    ResponseEntity<ApiResponse<InventoryDocumentResponse>> create(
            @PathVariable Long shopId,
            @RequestBody InventoryDocumentRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<InventoryDocumentResponse>(
                201,
                "Thành công",
                inventoryDocumentService.create(shopId, request),
                LocalDateTime.now()
        ));
    }

    @PutMapping("/{shopId}/approve")
    @Operation(summary = "Duyệt phiếu")
    @PreAuthorize("hasAuthority('PERM_INVENTORY_APPROVE')")
    ResponseEntity<ApiResponse<InventoryDocumentResponse>> approve(
            @PathVariable Long shopId,
            @RequestBody InventoryDocumentApproveRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<InventoryDocumentResponse>(
                200,
                "Thành công",
                inventoryDocumentService.approve(shopId, request),
                LocalDateTime.now()
        ));
    }

    @PutMapping("/{shopId}/reject")
    @Operation(summary = "Từ chối phiếu")
    @PreAuthorize("hasAuthority('PERM_INVENTORY_REJECT')")
    ResponseEntity<ApiResponse<InventoryDocumentResponse>> reject(
            @PathVariable Long shopId,
            @RequestBody InventoryDocumentRejectRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<InventoryDocumentResponse>(
                200,
                "Thành công",
                inventoryDocumentService.reject(shopId, request),
                LocalDateTime.now()
        ));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Lấy ra tất cả các phiếu vật tư của shop")
    @PreAuthorize("hasAuthority('PERM_INVENTORY_VIEW')")
    ResponseEntity<ApiResponse<Page<InventoryDocumentResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) InventoryDocumentStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                inventoryDocumentService.getAllByShopId(shopId, status, pageable),
                LocalDateTime.now()
        ));
    }

    @GetMapping("/{inventoryDocumentId}")
    @Operation(summary = "Xem chi tiết phiếu vật tư")
    @PreAuthorize("hasAuthority('PERM_INVENTORY_VIEW')")
    ResponseEntity<ApiResponse<InventoryDocumentResponse>> getById(
            @PathVariable Long inventoryDocumentId
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                inventoryDocumentService.getById(inventoryDocumentId),
                LocalDateTime.now()
        ));
    }
}
