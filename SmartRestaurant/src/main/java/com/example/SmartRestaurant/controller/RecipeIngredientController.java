package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.request.RecipeIngredientCreateRequest;
import com.example.SmartRestaurant.dto.request.RecipeIngredientUpdateRequest;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.RecipeIngredientResponse;
import com.example.SmartRestaurant.service.recipeingredient.RecipeIngredientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/recipe-ingredients/{recipeId}")
public class RecipeIngredientController {
    RecipeIngredientService service;

    @PostMapping
    @Operation(summary = "Thêm định lượng vào công thức")
    @PreAuthorize("hasAuthority('PERM_RECIPE_INGREDIENT_CREATE')")
    ResponseEntity<ApiResponse<List<RecipeIngredientResponse>>> create(
            @PathVariable Long recipeId,
            @RequestBody List<RecipeIngredientCreateRequest> recipeIngredientRequestList
    ) {
        return ResponseEntity.status(201).body(new ApiResponse<List<RecipeIngredientResponse>>(
                201,
                "Thành công",
                service.create(recipeId, recipeIngredientRequestList),
                LocalDateTime.now()
        ));
    }

    @PutMapping
    @Operation(summary = "Điều chỉnh định lượng nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_RECIPE_INGREDIENT_UPDATE')")
    ResponseEntity<ApiResponse<List<RecipeIngredientResponse>>> update(
            @PathVariable Long recipeId,
            @RequestBody List<RecipeIngredientUpdateRequest> recipeIngredientRequestList
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<List<RecipeIngredientResponse>>(
                200,
                "Thành công",
                service.update(recipeId, recipeIngredientRequestList),
                LocalDateTime.now()
        ));
    }

    @DeleteMapping
    @Operation(summary = "Xóa định lượng nguyên liệu")
    @PreAuthorize("hasAuthority('PERM_RECIPE_INGREDIENT_DELETE')")
    ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long recipeId,
            @RequestBody List<Long> recipeIngredientIdList
    ) {
        service.delete(recipeId, recipeIngredientIdList);
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                null,
                LocalDateTime.now()
        ));
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách định lượng theo công thức")
    @PreAuthorize("hasAuthority('PERM_RECIPE_INGREDIENT_VIEW')")
    ResponseEntity<ApiResponse<List<RecipeIngredientResponse>>> getByRecipeId(
            @PathVariable Long recipeId
    ) {
        return ResponseEntity.status(200).body(new ApiResponse<>(
                200,
                "Thành công",
                service.getByRecipeId(recipeId),
                LocalDateTime.now()
        ));
    }
}
