package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);


    boolean existsByNameAndShopId(String name, Long parentId);

    Page<RoleEntity> findAllByShopId(Long shopId, Pageable pageable);

    Page<RoleEntity> findAllByShopIdAndStatus(Long shopId, RoleStatus status, Pageable pageable);

    boolean existsByNameAndShopIdAndIdNot(String name, Long shopId, Long id);

    Set<RoleEntity> findAllByIdInAndShopIdAndStatus(Set<Long> roleIds, Long parentId, RoleStatus active);
}
