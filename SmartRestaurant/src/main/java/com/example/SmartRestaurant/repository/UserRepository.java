package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("select u from UserEntity u left join fetch u.roles r left join fetch r.permissions where u.email=:email")
    UserEntity findByEmailHasRoleAndPermission(String email);

    @Query("""
                select count(e) > 0
                from EmploymentEntity e
                join e.user u
                join u.roles r
                join r.permissions p
                where u.id = :userId
                  and e.shop.id = :shopId
                  and e.status = :employmentStatus
                  and r.shop.id = :shopId
                  and r.status = :roleStatus
                  and p.name = :permissionName
            """)
    boolean hasPermissionInShop(
            @Param("userId") Long userId,
            @Param("shopId") Long shopId,
            @Param("employmentStatus") EmploymentStatus employmentStatus,
            @Param("roleStatus") RoleStatus roleStatus,
            @Param("permissionName") String permissionName
    );
}
