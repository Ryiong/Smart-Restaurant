package com.example.SmartRestaurant.service.area;

import com.example.SmartRestaurant.common.enums.AreaStatus;
import com.example.SmartRestaurant.dto.request.AreaRequest;
import com.example.SmartRestaurant.dto.response.AreaResponse;
import com.example.SmartRestaurant.entity.AreaEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.AreaMapper;
import com.example.SmartRestaurant.repository.AreaRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.SmartRestaurant.validator.AreaValidator.validateAreaRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class AreaServiceImplement implements AreaService {
    AreaRepository repository;
    AuthorizationService authorizationService;
    AreaMapper mapper;
    ShopRepository shopRepository;

    //kiểm tra có phải chủ shop không
    //nếu là nhân viên thì sẽ kiểm tra có permission trong shop này không
    @Override
    public AreaResponse create(AreaRequest areaRequest, Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_AREA_CREATE");
        validateAreaRequest(areaRequest);
        AreaEntity area = mapper.toEntity(areaRequest);
        if (repository.existsByNameAndShopId(area.getName(), shopId)) {
            throw new ValidateException("Tên khu vực đã tồn tại trong shop");
        }
        area.setShop(shop);
        area.setStatus(AreaStatus.ACTIVE);
        return mapper.toResponse(repository.save(area));
    }

    @Override
    public AreaResponse update(Long id, AreaRequest areaRequest) {
        AreaEntity area = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_AREA_UPDATE");
        validateAreaRequest(areaRequest);
        AreaEntity newArea = mapper.toEntity(areaRequest);
        if (repository.existsByNameAndShopIdAndIdNot(
                newArea.getName()
                , area.getShop().getId()
                , area.getId())) {
            throw new ValidateException("Tên khu vực đã tồn tại trong shop");
        }
        area.setName(newArea.getName());
        area.setDescription(newArea.getDescription());
        return mapper.toResponse(repository.save(area));
    }

    @Override
    public void delete(Long id) {
        AreaEntity area = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_AREA_DELETE");
        if (area.getStatus() != AreaStatus.ACTIVE) {
            throw new InvalidStatusException("khu vực");
        }
        area.setStatus(AreaStatus.INACTIVE);
        repository.save(area);
    }

    @Override
    public AreaResponse getById(Long id) {
        AreaEntity area = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_AREA_VIEW");
        return mapper.toResponse(area);
    }

    @Override
    public Page<AreaResponse> getAllByShopId(Long shopId, AreaStatus status, Pageable pageable) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_AREA_VIEW");
        Page<AreaEntity> areas = status == null
                ? repository.findAllByShopId(shopId, pageable)
                : repository.findAllByShopIdAndStatus(shopId, status, pageable);
        return areas.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long areaId) {
        AreaEntity area = repository.findById(areaId)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_AREA_ACTIVATE");
        if (area.getStatus() != AreaStatus.INACTIVE) {
            throw new InvalidStatusException("khu vực");
        }
        area.setStatus(AreaStatus.ACTIVE);
        repository.save(area);
    }
}
