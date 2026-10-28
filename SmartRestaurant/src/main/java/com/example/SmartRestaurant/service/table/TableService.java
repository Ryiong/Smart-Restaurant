package com.example.SmartRestaurant.service.table;

import com.example.SmartRestaurant.common.enums.TableStatus;
import com.example.SmartRestaurant.dto.request.TableRequest;
import com.example.SmartRestaurant.dto.response.TableResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TableService extends ParentResourceBaseService<TableRequest, TableResponse, Long> {
    Page<TableResponse> getAllByAreaId(Long areaId, TableStatus status, Pageable pageable);

    void activate(Long tableId);
}
