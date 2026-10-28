package com.example.SmartRestaurant.service.role;

import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.dto.request.RoleRequest;
import com.example.SmartRestaurant.dto.response.RoleResponse;
import com.example.SmartRestaurant.entity.PermissionEntity;
import com.example.SmartRestaurant.entity.RoleEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.PermissionMapper;
import com.example.SmartRestaurant.mapper.RoleMapper;
import com.example.SmartRestaurant.repository.PermissionRepository;
import com.example.SmartRestaurant.repository.RoleRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.example.SmartRestaurant.validator.RoleValidator.validateRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class RoleServiceImplement implements RoleService {
    RoleRepository repository;

    RoleMapper mapper;

    PermissionMapper permissionMapper;
    ShopRepository shopRepository;
    PermissionRepository permissionRepository;
    AuthorizationService authorizationService;

    //các thao tác này yêu cầu owner của shop mới được thực hiện
    @Override
    public RoleResponse create(RoleRequest roleRequest, Long parentId) {
        ShopEntity shop = shopRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        validateRequest(roleRequest);
        if (repository.existsByNameAndShopId(roleRequest.getName(), parentId)) {
            throw new ValidateException("Tên role đã tồn tại trong shop");
        }
        Set<Long> permissionIds = new HashSet<>(roleRequest.getPermissionIds());
        if (permissionIds.size() != roleRequest.getPermissionIds().size()) {
            throw new ValidateException("Có permisison bị trùng");
        }
        Set<PermissionEntity> permissions = permissionRepository.findAllByIdIn(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new NotFoundException("Permission");
        }
        RoleEntity role = mapper.toEntity(roleRequest);
        role.setShop(shop);
        role.setPermissions(permissions);
        role.setStatus(RoleStatus.ACTIVE);
        RoleResponse roleResponse = mapper.toResponse(repository.save(role));
        return roleResponse;
    }

    @Override
    public RoleResponse update(Long id, RoleRequest roleRequest) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role"));
        if (role.getShop() == null) {
            throw new AccessDeniedException("Không thể sửa role hệ thống");
        }

        validateRequest(roleRequest);
        if (repository.existsByNameAndShopIdAndIdNot(roleRequest.getName(), role.getShop().getId(), id)) {
            throw new ValidateException("Tên role đã tồn tại trong shop");
        }
        Set<Long> permissionIds = new HashSet<>(roleRequest.getPermissionIds());
        if (permissionIds.size() != roleRequest.getPermissionIds().size()) {
            throw new ValidateException("Có permission bị trùng");
        }
        Set<PermissionEntity> permissions = permissionRepository.findAllByIdIn(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new NotFoundException("Permission");
        }
        role.setName(roleRequest.getName());
        role.getPermissions().clear();
        role.getPermissions().addAll(permissions);
        RoleResponse roleResponse = mapper.toResponse(repository.save(role));
        return roleResponse;
    }

    @Override
    public void delete(Long id) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role"));
        if (role.getShop() == null) {
            throw new AccessDeniedException("Không thể xóa role hệ thống");
        }

        if (role.getStatus() != RoleStatus.ACTIVE) {
            throw new InvalidStatusException("role");
        }
        role.setStatus(RoleStatus.INACTIVE);
        repository.save(role);
    }

    @Override
    public RoleResponse getById(Long id) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role"));
        if (role.getShop() == null) {
            throw new AccessDeniedException("Không thể xem role hệ thống");
        }

        RoleResponse roleResponse = mapper.toResponse(role);
        return roleResponse;
    }

    @Override
    public Page<RoleResponse> getAllByShopId(Long shopId, RoleStatus status, Pageable pageable) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        Page<RoleEntity> roles = status == null
                ? repository.findAllByShopId(shopId, pageable)
                : repository.findAllByShopIdAndStatus(shopId, status, pageable);
        return roles.map(x -> mapper.toResponse(x));
    }
}
