package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.InventoryDocumentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDocumentItemRepository extends JpaRepository<InventoryDocumentItemEntity, Long> {
}
