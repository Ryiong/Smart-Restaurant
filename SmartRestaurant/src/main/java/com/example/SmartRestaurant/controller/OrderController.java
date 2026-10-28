package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.OrderItemDeleteRequest;
import com.example.SmartRestaurant.dto.request.OrderRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.OrderResponse;
import com.example.SmartRestaurant.service.order.OrderService;
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

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/order")
public class OrderController {
    OrderService orderService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo đơn hàng hoặc thêm món")
    //dành cho user có quyền trong quán
    @PreAuthorize("hasAuthority('PERM_ORDER_CREATE')")
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @PathVariable Long shopId,
            @RequestBody OrderRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , orderService.create(request, shopId)
                , LocalDateTime.now()
        ));
    }


    @DeleteMapping("/{shopId}")
    @Operation(summary = "Xóa món khỏi đơn hàng")
    @PreAuthorize("hasAuthority('PERM_ORDER_ITEM_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteItems(
            @PathVariable Long shopId,
            @RequestBody OrderItemDeleteRequest request
    ) {
        orderService.deleteAll(shopId, request);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping
    @Operation(summary = "Tạo dơn hàng hoặc thêm món bằng id bàn")
    //khách hay bất kỳ ai có mã qr đều có thể gọi món
    public ResponseEntity<ApiResponse<OrderResponse>> createByQR(
            @RequestBody OrderRequest request
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , orderService.createByQR(request)
                , LocalDateTime.now()
        ));
    }


    @GetMapping("/{shopId}")
    @Operation(summary = "Lấy ra tất cả các đơn hàng theo ngày")
    @PreAuthorize("hasAuthority('PERM_ORDER_VIEW')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllByDateAndShopId(
            @PathVariable Long shopId,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , orderService.getAllByShopIdAndDate(shopId, date)
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/merge-table")
    @Operation(summary = "Gộp bàn")
    @PreAuthorize("hasAuthority('PERM_ORDER_TABLE_MERGE')")
    public ResponseEntity<ApiResponse<Void>> mergeOrderTable(
            @PathVariable Long shopId,
            @RequestParam Long fromId,
            @RequestParam Long toId
    ) {
        orderService.mergeOrderTable(shopId, fromId, toId);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/move-table")
    @Operation(summary = "Chuyển bàn")
    @PreAuthorize("hasAuthority('PERM_ORDER_TABLE_MOVE')")
    public ResponseEntity<ApiResponse<Void>> moveOrderTable(
            @PathVariable Long shopId,
            @RequestParam Long fromId,
            @RequestParam Long toId
    ) {
        orderService.moveOrderTable(shopId, fromId, toId);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PostMapping("/{shopId}/payment/{orderId}")
    @Operation(summary = "Thanh toán")
    @PreAuthorize("hasAuthority('PERM_ORDER_PAYMENT')")
    public ResponseEntity<ApiResponse<OrderResponse>> payOrder(
            @PathVariable Long shopId,
            @PathVariable Long orderId
    ) {

        return ResponseEntity.status(200).body(new ApiResponse<>(
                200
                , "Thành công"
                , orderService.pay(shopId, orderId)
                , LocalDateTime.now()
        ));
    }
}
