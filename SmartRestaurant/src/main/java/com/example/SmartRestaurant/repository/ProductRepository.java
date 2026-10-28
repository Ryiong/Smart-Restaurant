package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.ProductStatus;
import com.example.SmartRestaurant.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByNameAndCategoryId(String name, Long categoryId);

    boolean existsByNameAndCategoryIdAndIdNot(String name, Long categoryId, Long id);

    Page<ProductEntity> findAllByCategoryId(Long categoryId, Pageable pageable);

    Page<ProductEntity> findAllByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    List<ProductEntity> findAllByIdInAndAndStatus(Set<Long> productIds, ProductStatus active);
}
