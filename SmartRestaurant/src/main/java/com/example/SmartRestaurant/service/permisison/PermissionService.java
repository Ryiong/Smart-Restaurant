package com.example.SmartRestaurant.service.permisison;

import com.example.SmartRestaurant.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> getAll();
}
