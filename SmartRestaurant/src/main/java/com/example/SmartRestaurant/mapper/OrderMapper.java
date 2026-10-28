package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.response.OrderResponse;
import com.example.SmartRestaurant.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    TableMapper tableMapper;

    public OrderResponse toResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .table(tableMapper.toResponse(order.getTable()))
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod() == null
                        ? null
                        : order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .paidAt(order.getPaidAt() == null
                        ? null
                        : order.getPaidAt())
                .build();
    }
}
