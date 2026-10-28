package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.AreaStatus;
import com.example.SmartRestaurant.entity.AreaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
    boolean existsByNameAndShopId(String name, Long shopId);

    boolean existsByNameAndShopIdAndIdNot(String name, Long shopId, Long id);

    Page<AreaEntity> findAllByShopId(Long shopId, Pageable pageable);

    Page<AreaEntity> findAllByShopIdAndStatus(Long shopId, AreaStatus status, Pageable pageable);

}
