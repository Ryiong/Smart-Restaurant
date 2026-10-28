package com.example.SmartRestaurant.dto.request;

import com.example.SmartRestaurant.common.enums.InventoryDocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryDocumentRequest {
    private String note;
    private InventoryDocumentType type;
    private List<InventoryDocumentItemRequest> items;
}
