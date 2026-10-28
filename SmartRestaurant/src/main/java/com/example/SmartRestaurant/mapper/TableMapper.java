package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.TableRequest;
import com.example.SmartRestaurant.dto.response.TableResponse;
import com.example.SmartRestaurant.entity.TableEntity;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    public TableEntity toEntity(TableRequest request) {
        return TableEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .qrEnabled(request.isQrEnabled())
                .build();
    }

    public TableResponse toResponse(TableEntity table) {
        return TableResponse.builder()
                .id(table.getId())
                .name(table.getName())
                .qrEnabled(table.isQrEnabled())
                .orderStatus(table.getOrderStatus())
                .status(table.getStatus())
                .build();
    }
}
