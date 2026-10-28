package com.example.SmartRestaurant.controller;

import com.example.SmartRestaurant.dto.response.ApiResponse;
import com.example.SmartRestaurant.dto.response.PermissionResponse;
import com.example.SmartRestaurant.service.permisison.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.SmartRestaurant.common.Constant.URL;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(URL + "/permissions")
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Thành công",
                        permissionService.getAll(),
                        LocalDateTime.now()
                )
        );
    }
}
