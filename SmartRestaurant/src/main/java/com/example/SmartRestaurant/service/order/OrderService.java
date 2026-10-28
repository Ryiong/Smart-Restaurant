package com.example.SmartRestaurant.service.order;

import com.example.SmartRestaurant.dto.request.OrderItemDeleteRequest;
import com.example.SmartRestaurant.dto.request.OrderRequest;
import com.example.SmartRestaurant.dto.response.OrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request, Long shopId);

    void deleteAll(Long shopId, OrderItemDeleteRequest request);

    OrderResponse createByQR(OrderRequest request);

    List<OrderResponse> getAllByShopIdAndDate(Long shopId, LocalDate date);

    void mergeOrderTable(Long shopId, Long fromId, Long toId);

    void moveOrderTable(Long shopId, Long fromId, Long toId);

    OrderResponse pay(Long shopId, Long orderId);

}
