package com.example.SmartRestaurant.service.orderitem;

import com.example.SmartRestaurant.dto.request.OrderItemRequest;
import com.example.SmartRestaurant.dto.response.OrderItemResponse;
import com.example.SmartRestaurant.entity.OrderEntity;
import com.example.SmartRestaurant.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemService {
    List<OrderItemResponse> createAll(OrderEntity order,
                                      List<OrderItemRequest> itemRequests,
                                      UserEntity createdBy);


    void deleteAll(OrderEntity order, List<Long> orderItemIds);

    void mergeOrder(OrderEntity fromOrder, OrderEntity toOrder);


    BigDecimal getTotalPriceByOrderId(Long orderId);
}
