package com.example.SmartRestaurant.config.init;


import com.example.SmartRestaurant.common.enums.AccountType;
import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.common.enums.UserStatus;
import com.example.SmartRestaurant.entity.PermissionEntity;
import com.example.SmartRestaurant.entity.RoleEntity;
import com.example.SmartRestaurant.entity.UserEntity;
import com.example.SmartRestaurant.repository.PermissionRepository;
import com.example.SmartRestaurant.repository.RoleRepository;
import com.example.SmartRestaurant.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InitConfig {

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    List<String> permissions = List.of(
            //SHOP
            "PERM_SHOP_SET_LOCATION",

            //AREA
            "PERM_AREA_VIEW",
            "PERM_AREA_CREATE",
            "PERM_AREA_UPDATE",
            "PERM_AREA_DELETE",
            "PERM_AREA_ACTIVATE",

            //TABLE
            "PERM_TABLE_VIEW",
            "PERM_TABLE_CREATE",
            "PERM_TABLE_UPDATE",
            "PERM_TABLE_DELETE",
            "PERM_TABLE_ACTIVATE",

            //CATEGORY
            "PERM_CATEGORY_VIEW",
            "PERM_CATEGORY_CREATE",
            "PERM_CATEGORY_UPDATE",
            "PERM_CATEGORY_DELETE",
            "PERM_CATEGORY_ACTIVATE",

            //PRODUCT
            "PERM_PRODUCT_VIEW",
            "PERM_PRODUCT_CREATE",
            "PERM_PRODUCT_UPDATE",
            "PERM_PRODUCT_DELETE",
            "PERM_PRODUCT_ACTIVATE",

            //RECIPE
            "PERM_RECIPE_INGREDIENT_VIEW",
            "PERM_RECIPE_INGREDIENT_CREATE",
            "PERM_RECIPE_INGREDIENT_UPDATE",
            "PERM_RECIPE_INGREDIENT_DELETE",

            //INGREDIENT
            "PERM_INGREDIENT_VIEW",
            "PERM_INGREDIENT_CREATE",
            "PERM_INGREDIENT_UPDATE",
            "PERM_INGREDIENT_DELETE",
            "PERM_INGREDIENT_ACTIVATE",

            //EMPLOYMENT
            "PERM_EMPLOYMENT_VIEW",
            "PERM_EMPLOYMENT_CREATE",
            "PERM_EMPLOYMENT_UPDATE",
            "PERM_EMPLOYMENT_TERMINATE",

            //INVENTORY
            "PERM_INVENTORY_VIEW",
            "PERM_INVENTORY_CREATE",
            "PERM_INVENTORY_APPROVE",
            "PERM_INVENTORY_REJECT",

            //SHIFT
            "PERM_SHIFT_VIEW",
            "PERM_SHIFT_CREATE",
            "PERM_SHIFT_APPROVE",
            "PERM_SHIFT_REJECT",
            "PERM_SHIFT_ACTIVATE",

            //WORKSCHEDULE
            "PERM_WORK_SCHEDULE_CREATE",
            "PERM_WORK_SCHEDULE_UPDATE",
            "PERM_WORK_SCHEDULE_DELETE",
            "PERM_WORK_SCHEDULE_VIEW",

            //ORDER
            "PERM_ORDER_CREATE",
            "PERM_ORDER_ITEM_DELETE",
            "PERM_ORDER_VIEW",
            "PERM_ORDER_TABLE_MERGE",
            "PERM_ORDER_TABLE_MOVE",
            "PERM_ORDER_PAYMENT",

            //HOST-DEVICE
            "PERM_HOST_DEVICE_REGISTER"
    );
    List<String> roles = List.of(
            "ROLE_ADMIN"
            , "ROLE_OWNER"
    );

    @Bean
    CommandLineRunner init(@Value("${admin.email}") String adminEmail,
                           @Value("${admin.password}") String adminPassword) {
        return args -> {
            try {
                initRoleAndPermission();
                initAdmin(adminEmail, adminPassword);
            } catch (Exception e) {
                log.error("Init role data failed: " + e.getMessage());
            }
            log.info("Init data finished");
        };
    }

    private void initRoleAndPermission() {
        //mặc định 2 role admin và owner
        //mặc định các permissions như trên
        //kiểm tra mỗi lần có role hoặc permission bị xóa

        for (String permissionName : permissions
        ) {
            if (!permissionRepository.existsByName(permissionName)) {
                PermissionEntity permission = new PermissionEntity();
                permission.setName(permissionName);
                permissionRepository.save(permission);
            }
        }
        List<PermissionEntity> permissionList = permissionRepository.findAll();
        for (String roleName : roles
        ) {
            if (roleRepository.findByName(roleName) == null) {
                RoleEntity role = new RoleEntity();
                role.setName(roleName);
                role.setStatus(RoleStatus.ACTIVE);
                //gán tất cả các permission có sẵn cho admin và owner
                role.setPermissions(new HashSet<>(permissionList));
                roleRepository.save(role);
            }
        }
    }

    private void initAdmin(String adminEmail
            , String adminPassword) {
        if (userRepository.findByEmail(adminEmail) == null) {
            UserEntity admin = new UserEntity();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setName("ADMIN");
            admin.setStatus(UserStatus.ACTIVE);
            admin.setType(AccountType.ADMIN);
            admin.setRoles(new HashSet<>(List.of(roleRepository.findByName("ROLE_ADMIN"))));
            userRepository.save(admin);
        }
    }

}
