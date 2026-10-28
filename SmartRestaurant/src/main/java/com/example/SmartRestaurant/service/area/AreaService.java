package com.example.SmartRestaurant.service.area;

import com.example.SmartRestaurant.common.enums.AreaStatus;
import com.example.SmartRestaurant.dto.request.AreaRequest;
import com.example.SmartRestaurant.dto.response.AreaResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AreaService extends ParentResourceBaseService<AreaRequest, AreaResponse, Long> {
    Page<AreaResponse> getAllByShopId(Long shopId, AreaStatus status, Pageable pageable);

    void activate(Long areaId);

}
