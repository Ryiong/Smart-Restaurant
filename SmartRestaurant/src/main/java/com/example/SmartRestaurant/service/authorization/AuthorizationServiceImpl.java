package com.example.SmartRestaurant.service.authorization;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.entity.PermissionEntity;
import com.example.SmartRestaurant.entity.RoleEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.repository.EmploymentRepository;
import com.example.SmartRestaurant.repository.PermissionRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class AuthorizationServiceImpl implements AuthorizationService {
    CurrentUserProvider currentUserProvider;

    EmploymentRepository employmentRepository;

    PermissionRepository permissionRepository;


    @Override
    public void checkOwnerOrPermissionInShop(ShopEntity shop, String permissionName) {
        boolean isOwner = shop.getUser().getId()
                .equals(currentUserProvider.getCurrentUserId());
        if (isOwner) {
            return;//nếu là chủ của shop thì cho qua
        }
        //KIỂM TRA NHÂN VIÊN CÓ CÒN ĐANG LÀM CHO SHOP NÀY KHÔNG
        if (!employmentRepository.existsByUserIdAndShopIdAndStatus(
                currentUserProvider.getCurrentUserId(),
                shop.getId(),
                EmploymentStatus.ACTIVE
        )) {
            throw new AccessDeniedException("Không có quyền thực hiện");
        }
        //kiểm tra user có role nào active và có permission có name cần thiết không
        PermissionEntity permission = permissionRepository.findByName(permissionName);
        Set<RoleEntity> roles = currentUserProvider.getCurrentUser().getUser().getRoles();
        boolean allowed =
                roles.stream()
                        .anyMatch(x -> x.getStatus() == RoleStatus.ACTIVE
                                && x.getShop() != null
                                && x.getShop().getId().equals(shop.getId())
                                && x.getPermissions().contains(permission));
        if (!allowed) {
            throw new AccessDeniedException("Không có quyền thực hiện");
        }
    }


}
