package com.example.SmartRestaurant.service.shop;

import com.example.SmartRestaurant.common.enums.ShopStatus;
import com.example.SmartRestaurant.dto.request.ReasonRequest;
import com.example.SmartRestaurant.dto.request.ShopLocationRequest;
import com.example.SmartRestaurant.dto.request.ShopRequest;
import com.example.SmartRestaurant.dto.response.ShopResponse;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.ShopMapper;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.SmartRestaurant.validator.ShopValidator.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ShopServiceImplement implements ShopService {
    ShopRepository repository;

    ShopMapper mapper;
    CurrentUserProvider currentUserProvider;
    AuthorizationService authorizationService;

    //owner thực hiện, nên check owner có đúng là sở hữu shop không
    @Override
    public ShopResponse create(ShopRequest request) {
        validateShopRequest(request);
        ShopEntity shop = mapper.toEntity(request);
        if (repository.existsByNameAndUserId(shop.getName(), currentUserProvider.getCurrentUserId())) {
            throw new ValidateException("Tên shop đã tồn tại");
        }
        shop.setUser(currentUserProvider.getCurrentUser().getUser());
        shop.setStatus(ShopStatus.PENDING);
        return mapper.toResponse(repository.save(shop));
    }


    @Override
    public ShopResponse update(Long id, ShopRequest request) {
        ShopEntity shop = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        validateShopRequest(request);

        ShopEntity newShop = mapper.toEntity(request);
        if (repository.existsByNameAndUserIdAndIdNot(newShop.getName()
                , currentUserProvider.getCurrentUserId(), shop.getId())) {
            throw new ValidateException("Tên shop đã tồn tại");
        }
        shop.setName(newShop.getName());
        shop.setAddress(newShop.getAddress());
        shop.setPhoneNumber(newShop.getPhoneNumber());
        shop.setOpenTime(newShop.getOpenTime());
        shop.setCloseTime(newShop.getCloseTime());
        return mapper.toResponse(repository.save(shop));
    }

    @Override
    public List<ShopResponse> getAllShopOfCurrentUser() {
        return repository.findAllByUser_Id(currentUserProvider.getCurrentUserId())
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public ShopResponse getShopByIdOfCurrentUser(Long id) {
        ShopEntity shop = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));

        return mapper.toResponse(shop);
    }


    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException(
                "Cửa hàng không hỗ trợ thao tác xóa"
        );
    }

    //role admin mới được thực hiện, check bằng preauthorize ở api
    @Override
    public ShopResponse getById(Long id) {
        ShopEntity shop = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shop"));
        return mapper.toResponse(shop);
    }

    @Override
    public void approve(Long shopId) {
        ShopEntity shop = repository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));
        if (shop.getStatus() != ShopStatus.PENDING) {
            throw new InvalidStatusException("shop");
        }
        shop.setStatus(ShopStatus.ACTIVE);
        shop.setStatusReason(null);
        repository.save(shop);
    }

    @Override
    public void reject(Long shopId, ReasonRequest reason) {
        validateReject(reason);
        ShopEntity shop = repository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        if (shop.getStatus() != ShopStatus.PENDING) {
            throw new InvalidStatusException("shop");
        }
        shop.setStatusReason(reason.getReason());
        repository.save(shop);
    }

    @Override
    public void lock(Long shopId, ReasonRequest reason) {
        validateLock(reason);
        ShopEntity shop = repository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));
        if (shop.getStatus() != ShopStatus.ACTIVE) {
            throw new InvalidStatusException("shop");
        }
        shop.setStatus(ShopStatus.LOCKED);
        shop.setStatusReason(reason.getReason());
        repository.save(shop);
    }

    @Override
    public void unlock(Long shopId) {
        ShopEntity shop = repository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));
        if (shop.getStatus() != ShopStatus.LOCKED) {
            throw new InvalidStatusException("shop");
        }
        shop.setStatus(ShopStatus.ACTIVE);
        shop.setStatusReason(null);
        repository.save(shop);
    }

    @Override
    public Page<ShopResponse> getAll(Pageable pageable, ShopStatus status) {
        Page<ShopEntity> shops = status == null
                ? repository.findAll(pageable)
                : repository.findAllByStatus(status, pageable);
        return shops.map(mapper::toResponse);
    }

    @Override
    public ShopResponse setLocation(Long shopId, ShopLocationRequest request) {
        ShopEntity shop = repository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_SHOP_SET_LOCATION");
        validateShopLocationRequest(request);
        shop.setLongitude(request.getLongitude());
        shop.setLatitude(request.getLatitude());
        shop.setAttendanceRadius(request.getAttendanceRadius());
        return mapper.toResponse(repository.save(shop));
    }

}
