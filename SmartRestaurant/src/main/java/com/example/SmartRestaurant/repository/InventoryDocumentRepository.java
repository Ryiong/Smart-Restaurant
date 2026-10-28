package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.InventoryDocumentStatus;
import com.example.SmartRestaurant.entity.InventoryDocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDocumentRepository extends JpaRepository<InventoryDocumentEntity, Long> {
    Page<InventoryDocumentEntity> findAllByShopId(Long shopId, Pageable pageable);

    Page<InventoryDocumentEntity> findAllByShopIdAndStatus(
            Long shopId,
            InventoryDocumentStatus status,
            Pageable pageable);
}
