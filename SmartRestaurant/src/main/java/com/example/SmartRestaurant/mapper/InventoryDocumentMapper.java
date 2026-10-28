package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.InventoryDocumentRequest;
import com.example.SmartRestaurant.dto.response.InventoryDocumentResponse;
import com.example.SmartRestaurant.entity.InventoryDocumentEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryDocumentMapper {
    public InventoryDocumentEntity toEntity(InventoryDocumentRequest request) {
        return InventoryDocumentEntity.builder()
                .note(request.getNote())
                .type(request.getType())
                .build();
    }

    public InventoryDocumentResponse toResponse(InventoryDocumentEntity entity) {
        return InventoryDocumentResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .rejectReason(entity.getRejectReason())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .createdByName(entity.getCreatedBy().getName())
                .reviewedBy(entity.getReviewedBy() == null
                        ? null
                        : entity.getReviewedBy().getName())
                .reviewedAt(entity.getReviewedAt())
                .build();
    }

}
