package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.OrderStatus;
import com.example.SmartRestaurant.common.enums.PaymentMethod;
import com.example.SmartRestaurant.common.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private TableResponse table;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
    private BigDecimal totalPrice;
    private List<OrderItemResponse> itemResponses;
}
