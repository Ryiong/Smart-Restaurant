package com.example.SmartRestaurant.entity;

import com.example.SmartRestaurant.common.enums.RoleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleStatus status;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = true)
    private ShopEntity shop;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission"
            , joinColumns = @JoinColumn(name = "role_id")
            , inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;
}
