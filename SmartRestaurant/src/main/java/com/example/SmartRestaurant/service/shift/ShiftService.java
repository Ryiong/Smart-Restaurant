package com.example.SmartRestaurant.service.shift;

import com.example.SmartRestaurant.common.enums.ShiftStatus;
import com.example.SmartRestaurant.dto.request.ShiftRequest;
import com.example.SmartRestaurant.dto.response.ShiftResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShiftService extends ParentResourceBaseService<ShiftRequest, ShiftResponse, Long> {
    Page<ShiftResponse> getAllByShopId(Long shopId, ShiftStatus status, Pageable pageable);

    void activate(Long shiftId);
    
}
