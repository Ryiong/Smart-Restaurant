package com.example.SmartRestaurant.service.role;

import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.dto.request.RoleRequest;
import com.example.SmartRestaurant.dto.response.RoleResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService extends ParentResourceBaseService<RoleRequest, RoleResponse, Long> {
    Page<RoleResponse> getAllByShopId(Long shopId, RoleStatus status, Pageable pageable);
}
