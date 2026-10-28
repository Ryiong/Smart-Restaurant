package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.CategoryStatus;
import com.example.SmartRestaurant.dto.request.CategoryRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.CategoryResponse;
import com.example.SmartRestaurant.service.category.CategoryService;
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
@RequestMapping(URL + "/category")
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo danh mục")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_CREATE')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @PathVariable Long shopId,
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , categoryService.create(request, shopId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Tìm tất cả danh mục theo shopId")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_VIEW')")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) CategoryStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , categoryService.getAllByShopId(shopId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Tìm danh mục theo id")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_VIEW')")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , categoryService.getById(categoryId)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Sửa thông tin danh mục")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_UPDATE')")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , categoryService.update(categoryId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Xóa danh mục")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long categoryId
    ) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{categoryId}/activate")
    @Operation(summary = "Bật lại danh mục")
    @PreAuthorize("hasAuthority('PERM_CATEGORY_ACTIVATE')")
    public ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long categoryId
    ) {
        categoryService.activate(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
