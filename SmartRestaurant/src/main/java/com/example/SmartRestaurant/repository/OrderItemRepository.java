package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.OrderItemStatus;
import com.example.SmartRestaurant.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findAllByIdInAndOrderId(Set<Long> itemIds, Long orderId);

    List<OrderItemEntity> findAllByOrderId(Long id);

    List<OrderItemEntity> findAllByOrderIdAndStatus(Long orderId, OrderItemStatus status);
}
