package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import com.example.SmartRestaurant.entity.EmploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EmploymentRepository extends JpaRepository<EmploymentEntity, Long> {
    boolean existsByUserIdAndShopIdAndStatus(Long currentUserId, Long shopId, EmploymentStatus status);

    Set<EmploymentEntity> findAllByIdInAndStatusAndShopId(Set<Long> employmentIds, EmploymentStatus active, Long shopId);

    Optional<EmploymentEntity> findByIdAndShopId(Long employmentId, Long shopId);

}
