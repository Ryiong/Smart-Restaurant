package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    boolean existsByName(String permissionName);


    Set<PermissionEntity> findAllByIdIn(Set<Long> permissionIds);

    PermissionEntity findByName(String permissionName);
}
