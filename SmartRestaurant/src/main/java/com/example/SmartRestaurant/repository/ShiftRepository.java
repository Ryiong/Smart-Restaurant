package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.ShiftStatus;
import com.example.SmartRestaurant.entity.ShiftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {
    Page<ShiftEntity> findAllByShopId(Long shopId, Pageable pageable);

    Page<ShiftEntity> findAllByShopIdAndStatus(Long shopId, ShiftStatus status, Pageable pageable);


    Set<ShiftEntity> findAllByIdInAndStatusAndShopId(Set<Long> shiftIds, ShiftStatus active, Long shopId);

    ShiftEntity findByIdAndShopIdAndStatus(Long shiftId, Long shopId, ShiftStatus active);

}
