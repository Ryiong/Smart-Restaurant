package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.TableStatus;
import com.example.SmartRestaurant.entity.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    boolean existsByNameAndAreaId(String name, Long areaId);

    boolean existsByNameAndAreaIdAndIdNot(String name, Long areaId, Long id);

    Page<TableEntity> findAllByAreaId(Long shopId, Pageable pageable);

    Page<TableEntity> findAllByAreaIdAndStatus(Long shopId, TableStatus status, Pageable pageable);


}
