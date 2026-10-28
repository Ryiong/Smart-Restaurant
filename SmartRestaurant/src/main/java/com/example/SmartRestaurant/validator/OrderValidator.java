package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.OrderItemDeleteRequest;
import com.example.SmartRestaurant.dto.request.OrderItemRequest;
import com.example.SmartRestaurant.dto.request.OrderRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class OrderValidator {
    public static void validateOrderRequest(OrderRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin đơn hàng không hợp lệ");
        }
        if (request.getTableId() == null) {
            throw new ValidateException("Id bàn không được bỏ trống");
        }
        if (request.getTableId() <= 0) {
            throw new ValidateException("Id bàn không hợp lệ");
        }
        if (request.getItemRequests() == null || request.getItemRequests().isEmpty()) {
            throw new ValidateException("Danh sách món trong đơn hàng không được bỏ trống");
        }
        for (OrderItemRequest item : request.getItemRequests()) {
            if (item == null) {
                throw new ValidateException("Thông tin món không hợp lệ");
            }

            if (item.getProductId() == null || item.getProductId() <= 0) {
                throw new ValidateException("Id sản phẩm không hợp lệ");
            }

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new ValidateException("Số lượng món phải lớn hơn 0");
            }
        }
    }

    public static void validateOrderItemDeleteRequest(OrderItemDeleteRequest request) {
        if (request == null) {
            throw new ValidateException("Thông tin đơn hàng không hợp lệ");
        }
        if (request.getTableId() == null) {
            throw new ValidateException("Id bàn không được bỏ trống");
        }
        if (request.getOrderItemIds() == null || request.getOrderItemIds().isEmpty()) {
            throw new ValidateException("Danh sách món trong đơn hàng không được bỏ trống");
        }
        if (request.getTableId() <= 0) {
            throw new ValidateException("Id bàn không hợp lệ");
        }

        for (Long orderItemId : request.getOrderItemIds()) {
            if (orderItemId == null || orderItemId <= 0) {
                throw new ValidateException("Id món trong đơn hàng không hợp lệ");
            }
        }
    }
    
}
