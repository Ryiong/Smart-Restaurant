package com.example.SmartRestaurant.service.inventorydocumentitem;

import com.example.SmartRestaurant.dto.request.InventoryDocumentItemRequest;
import com.example.SmartRestaurant.entity.InventoryDocumentEntity;
import com.example.SmartRestaurant.entity.InventoryDocumentItemEntity;

import java.util.List;

public interface InventoryDocumentItemService {
    List<InventoryDocumentItemEntity> createAll(InventoryDocumentEntity documentEntity, List<InventoryDocumentItemRequest> itemRequests);
}
