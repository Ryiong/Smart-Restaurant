package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.HostDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostDeviceRepository extends JpaRepository<HostDeviceEntity, Long> {
}
