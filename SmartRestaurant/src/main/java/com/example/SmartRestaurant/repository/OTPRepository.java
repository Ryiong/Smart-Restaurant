package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity, Long> {
    OTPEntity findByUserId(Long id);
}
