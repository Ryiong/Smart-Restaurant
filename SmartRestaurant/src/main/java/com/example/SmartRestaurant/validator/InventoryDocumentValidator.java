package com.example.SmartRestaurant.validator;

import com.example.SmartRestaurant.dto.request.InventoryDocumentApproveRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRejectRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRequest;
import com.example.SmartRestaurant.exception.ValidateException;

public final class InventoryDocumentValidator {
    public static void validateRequest(InventoryDocumentRequest request) {
        if (request.getType() == null) {
            throw new ValidateException("Loại phiếu không được bỏ trống");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidateException("Danh sách vật tư không được bỏ trống");
        }
    }

    public static void validateApproveRequest(InventoryDocumentApproveRequest request) {
        if (request.getId() == null || request.getId() <= 0) {
            throw new ValidateException("Id phiếu không hợp lệ");
        }
    }

    public static void validateRejectRequest(InventoryDocumentRejectRequest request) {
        if (request.getId() == null || request.getId() <= 0) {
            throw new ValidateException("Id phiếu không hợp lệ");
        }
        if (request.getRejectReason() == null || request.getRejectReason().isBlank()) {
            throw new ValidateException("Lý do từ chối không được bỏ trống");
        }
    }
}
