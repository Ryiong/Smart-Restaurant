package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.ShopStatus;
import com.example.SmartRestaurant.entity.ShopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    List<ShopEntity> findAllByUser_Id(Long id);

    ShopEntity findShopEntityByIdAndUser_Id(Long id, Long userId);

    Page<ShopEntity> findAllByStatus(ShopStatus status, Pageable pageable);

    boolean existsByNameAndUserId(String name, Long currentUserId);

    boolean existsByNameAndUserIdAndIdNot(String name, Long currentUserId, Long id);
}
