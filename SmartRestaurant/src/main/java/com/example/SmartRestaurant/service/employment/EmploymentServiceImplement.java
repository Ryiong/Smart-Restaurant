package com.example.SmartRestaurant.service.employment;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import com.example.SmartRestaurant.common.enums.RoleStatus;
import com.example.SmartRestaurant.dto.request.EmploymentRehireRequest;
import com.example.SmartRestaurant.dto.request.EmploymentRequest;
import com.example.SmartRestaurant.dto.response.EmploymentResponse;
import com.example.SmartRestaurant.entity.EmploymentEntity;
import com.example.SmartRestaurant.entity.RoleEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.entity.UserEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.EmploymentMapper;
import com.example.SmartRestaurant.mapper.ShopMapper;
import com.example.SmartRestaurant.mapper.UserMapper;
import com.example.SmartRestaurant.repository.EmploymentRepository;
import com.example.SmartRestaurant.repository.RoleRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.repository.UserRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.example.SmartRestaurant.validator.EmploymentValidator.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class EmploymentServiceImplement implements EmploymentService {
    EmploymentRepository repository;
    ShopRepository shopRepository;

    RoleRepository roleRepository;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;
    EmploymentMapper mapper;

    UserMapper userMapper;

    ShopMapper shopMapper;

    AuthorizationService authorizationService;

    @Override
    public EmploymentResponse create(EmploymentRequest employmentRequest, Long parentId) {
        ShopEntity shop = shopRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_EMPLOYMENT_CREATE");
        validateRequest(employmentRequest);
        //kiểm tra có roleId nào bị trùng trong request không
        Set<Long> roleIds = new HashSet<>(employmentRequest.getRoleIds());
        if (roleIds.size() != employmentRequest.getRoleIds().size()) {
            throw new ValidateException("Có vai trò bị trùng");
        }
        //kiểm tra có role nào mà không thuộc shop không
        Set<RoleEntity> roles = roleRepository.findAllByIdInAndShopIdAndStatus(roleIds, parentId, RoleStatus.ACTIVE);
        if (roles.size() != roleIds.size()) {
            throw new NotFoundException("Vai trò");
        }
        //kiểm tra email trùng
        if (userRepository.findByEmail(employmentRequest.getRegisterRequest().getEmail()) != null) {
            throw new ValidateException("Email đã có người sử dụng");
        }
        EmploymentEntity employment = mapper.toEntity(employmentRequest);
        UserEntity user = userMapper.toEntity(employmentRequest.getRegisterRequest());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        UserEntity savedUser = userRepository.save(user);
        employment.setStatus(EmploymentStatus.ACTIVE);
        employment.setShop(shop);
        employment.setUser(savedUser);
        EmploymentResponse employmentResponse = mapper.toResponse(repository.save(employment));
        employmentResponse.setUser(userMapper.toResponse(savedUser));
        employmentResponse.setShop(shopMapper.toResponse(shop));
        return employmentResponse;
    }

    @Override
    public EmploymentResponse update(Long id, EmploymentRequest employmentRequest) {
        EmploymentEntity employment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quan hệ nhân viên-cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(employment.getShop(), "PERM_EMPLOYMENT_UPDATE");
        validateUpdateRequest(employmentRequest);
        if (employment.getStatus() == EmploymentStatus.TERMINATED) {
            throw new InvalidStatusException("quan hệ nhân viên-cửa hàng");
        }
        //kiểm tra có roleId nào bị trùng trong request không
        Set<Long> roleIds = new HashSet<>(employmentRequest.getRoleIds());
        if (roleIds.size() != employmentRequest.getRoleIds().size()) {
            throw new ValidateException("Có vai trò bị trùng");
        }
        //kiểm tra có role nào mà không thuộc shop không
        Set<RoleEntity> roles = roleRepository.findAllByIdInAndShopIdAndStatus(roleIds, employment.getShop().getId(), RoleStatus.ACTIVE);
        if (roles.size() != roleIds.size()) {
            throw new NotFoundException("Vai trò");
        }
        employment.setSalary(employmentRequest.getSalary());
        //xóa các role cũ của nhân viên này tại shop này
        employment.getUser().getRoles().removeIf(role ->
                role.getShop() != null
                        && role.getShop().getId().equals(employment.getShop().getId())
        );
        //thêm role mới
        employment.getUser().getRoles().addAll(roles);
        EmploymentResponse employmentResponse = mapper.toResponse(repository.save(employment));
        employmentResponse.setUser(userMapper.toResponse(employment.getUser()));
        employmentResponse.setShop(shopMapper.toResponse(employment.getShop()));
        return employmentResponse;
    }

    @Override
    public void delete(Long id) {
        EmploymentEntity employment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quan hệ nhân viên-cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(employment.getShop(), "PERM_EMPLOYMENT_TERMINATE");
        if (employment.getStatus() != EmploymentStatus.ACTIVE) {
            throw new ValidateException(
                    "Quan hệ làm việc đã kết thúc"
            );
        }
        employment.setStatus(EmploymentStatus.TERMINATED);
        employment.setEndedAt(LocalDate.now());
        UserEntity user = employment.getUser();
        //gỡ toàn bộ role theo shopId
        Long shopId = employment.getShop().getId();
        user.getRoles().removeIf(role ->
                role.getShop() != null
                        && role.getShop().getId().equals(shopId)
        );
        userRepository.save(user);
        repository.save(employment);
    }

    @Override
    public EmploymentResponse getById(Long id) {
        EmploymentEntity employment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quan hệ nhân viên-cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(employment.getShop(), "PERM_EMPLOYMENT_VIEW");
        EmploymentResponse employmentResponse = mapper.toResponse(employment);
        employmentResponse.setShop(shopMapper.toResponse(employment.getShop()));
        employmentResponse.setUser(userMapper.toResponse(employment.getUser()));
        return employmentResponse;
    }


    @Override
    public EmploymentResponse createFromExistingUser(Long shopId, Long userId, EmploymentRehireRequest request) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_EMPLOYMENT_CREATE");
        //kiểm tra xem đã có employment nào đang active của userId và shopId không
        if (repository.existsByUserIdAndShopIdAndStatus(userId, shopId, EmploymentStatus.ACTIVE)) {
            throw new ValidateException("Nhân viên này đang làm tại cửa hàng");
        }
        validateRehireRequest(request);
        //kiểm tra có roleId nào bị trùng trong request không
        Set<Long> roleIds = new HashSet<>(request.getRoleIds());
        if (roleIds.size() != request.getRoleIds().size()) {
            throw new ValidateException("Có vai trò bị trùng");
        }
        //kiểm tra có role nào mà không thuộc shop không
        Set<RoleEntity> roles = roleRepository.findAllByIdInAndShopIdAndStatus(roleIds, shop.getId(), RoleStatus.ACTIVE);
        if (roles.size() != roleIds.size()) {
            throw new NotFoundException("Vai trò");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Nhân viên"));
        //xóa các role cũ của nhân viên này tại shop này
        user.getRoles().removeIf(role ->
                role.getShop() != null
                        && role.getShop().getId().equals(shopId)
        );
        //thêm role mới
        user.getRoles().addAll(roles);
        userRepository.save(user);
        EmploymentEntity employment = mapper.toRehireEntity(request);
        employment.setShop(shop);
        employment.setUser(user);
        employment.setStatus(EmploymentStatus.ACTIVE);
        EmploymentResponse employmentResponse = mapper.toResponse(repository.save(employment));
        employmentResponse.setUser(userMapper.toResponse(user));
        employmentResponse.setShop(shopMapper.toResponse(shop));
        return employmentResponse;
    }
}
