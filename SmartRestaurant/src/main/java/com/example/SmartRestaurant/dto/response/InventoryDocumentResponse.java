package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.InventoryDocumentStatus;
import com.example.SmartRestaurant.common.enums.InventoryDocumentType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InventoryDocumentResponse {
    private Long id;
    private InventoryDocumentType type;
    private String rejectReason;
    private String note;
    private LocalDateTime createdAt;
    private InventoryDocumentStatus status;
    private String createdByName;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
}
