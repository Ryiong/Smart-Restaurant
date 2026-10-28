package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.dto.request.IngredientRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.IngredientResponse;
import com.example.SmartRestaurant.service.ingredient.IngredientService;
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
@RequestMapping(URL + "/ingredient")
public class IngredientController {
    IngredientService ingredientService;

    @PostMapping("/{shopId}")
    @Operation(summary = "Tạo nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_CREATE')")
    public ResponseEntity<ApiResponse<IngredientResponse>> create(
            @PathVariable Long shopId,
            @RequestBody IngredientRequest request
    ) {

        return ResponseEntity.status(201).body(new ApiResponse<>(
                201
                , "Thành công"
                , ingredientService.create(request, shopId)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Tìm tất cả nguyên liệu theo shopId")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_VIEW')")
    public ResponseEntity<ApiResponse<Page<IngredientResponse>>> getAllByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) IngredientStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , ingredientService.getAllByShopId(shopId, status, pageable)
                , LocalDateTime.now()
        ));
    }

    @GetMapping("/{ingredientId}")
    @Operation(summary = "Tìm nguyên liệu theo id")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_VIEW')")
    public ResponseEntity<ApiResponse<IngredientResponse>> getById(
            @PathVariable Long ingredientId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , ingredientService.getById(ingredientId)
                , LocalDateTime.now()
        ));
    }

    @PutMapping("/{ingredientId}")
    @Operation(summary = "Sửa thông tin nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_UPDATE')")
    public ResponseEntity<ApiResponse<IngredientResponse>> update(
            @PathVariable Long ingredientId,
            @RequestBody IngredientRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , ingredientService.update(ingredientId, request)
                , LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{ingredientId}")
    @Operation(summary = "Xóa nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long ingredientId
    ) {
        ingredientService.delete(ingredientId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }

    @PatchMapping("/{ingredientId}/activate")
    @Operation(summary = "Bật lại nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_INGREDIENT_ACTIVATE')")
    public ResponseEntity<ApiResponse<Void>> activate(
            @PathVariable Long ingredientId
    ) {
        ingredientService.activate(ingredientId);
        return ResponseEntity.ok(new ApiResponse<>(
                200
                , "Thành công"
                , null
                , LocalDateTime.now()
        ));
    }
}
