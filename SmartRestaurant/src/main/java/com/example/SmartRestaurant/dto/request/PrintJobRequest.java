package com.example.SmartRestaurant.dto.request;

import com.example.SmartRestaurant.dto.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrintJobRequest {
    private Long id;
    private Long printType;
    private Long orderId;
    private String tableName;
    private String areaName;
    private String userName;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal vat;
    private BigDecimal totalAmount;
    private BigDecimal totalPayment;
    private BigDecimal change;
    private String paymentMethod;
}
