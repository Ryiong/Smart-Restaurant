package com.example.SmartRestaurant.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponse {
    private Long id;
    private ProductResponse productResponse;
    private Long orderId;
    private Long userId;
    private String userName;
    private String productName;
    private BigDecimal price;
    private Integer quantity;//snapshot
    private BigDecimal totalPrice;//snapshot
    private LocalDateTime createdAt;
}
