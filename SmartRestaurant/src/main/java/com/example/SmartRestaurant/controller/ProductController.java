package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.ProductStatus;
import com.example.SmartRestaurant.dto.request.ProductRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.ProductResponse;
import com.example.SmartRestaurant.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/product")
public class ProductController {
    ProductService productService;

    @PostMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo sản phẩm")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_CREATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @PathVariable Long categoryId,
            @ModelAttribute ProductRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , productService.create(request, categoryId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Tìm tất cả sản phẩm theo danh mục")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_VIEW')")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(required = false) ProductStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , productService.getAllByCategoryId(categoryId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Tìm sản phẩm theo id")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_VIEW')")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , productService.getById(productId)
                , LocalDateTime.now()
        ));
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Sửa thông tin sản phẩm")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long productId,
            @ModelAttribute ProductRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , productService.update(productId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Xóa sản phẩm")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long productId
    ) {
        productService.delete(productId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{productId}/activate")
    @Operation(summary = "Đăng bán lại sản phẩm")
    @PreAuthorize("hasAuthority('PERM_PRODUCT_ACTIVATE')")
    public ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long productId
    ) {
        productService.activate(productId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
