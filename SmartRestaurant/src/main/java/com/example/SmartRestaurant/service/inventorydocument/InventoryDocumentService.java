package com.example.SmartRestaurant.service.inventorydocument;

import com.example.SmartRestaurant.common.enums.InventoryDocumentStatus;
import com.example.SmartRestaurant.dto.request.InventoryDocumentApproveRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRejectRequest;
import com.example.SmartRestaurant.dto.request.InventoryDocumentRequest;
import com.example.SmartRestaurant.dto.response.InventoryDocumentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryDocumentService {

    InventoryDocumentResponse create(Long shopId, InventoryDocumentRequest request);

    InventoryDocumentResponse approve(Long shopId, InventoryDocumentApproveRequest request);

    InventoryDocumentResponse reject(Long shopId, InventoryDocumentRejectRequest request);

    Page<InventoryDocumentResponse> getAllByShopId(Long shopId, InventoryDocumentStatus status, Pageable pageable);

    InventoryDocumentResponse getById(Long inventoryDocumentId);

}
