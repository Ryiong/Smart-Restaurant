package com.example.SmartRestaurant.service.shift;

import com.example.SmartRestaurant.common.enums.ShiftStatus;
import com.example.SmartRestaurant.dto.request.ShiftRequest;
import com.example.SmartRestaurant.dto.response.ShiftResponse;
import com.example.SmartRestaurant.entity.ShiftEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.mapper.ShiftMapper;
import com.example.SmartRestaurant.repository.ShiftRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.SmartRestaurant.validator.ShiftValidator.validateRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ShiftServiceImplement implements ShiftService {
    ShiftRepository repository;
    ShiftMapper mapper;
    AuthorizationService authorizationService;
    ShopRepository shopRepository;

    @Override
    public ShiftResponse create(ShiftRequest shiftRequest, Long parentId) {
        ShopEntity shop = shopRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Cừa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_SHIFT_CREATE");
        validateRequest(shiftRequest);
        ShiftEntity shift = mapper.toEntity(shiftRequest);
        shift.setShop(shop);
        shift.setStatus(ShiftStatus.ACTIVE);
        return mapper.toResponse(repository.save(shift));
    }

    @Override
    public ShiftResponse update(Long id, ShiftRequest shiftRequest) {
        ShiftEntity shift = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ca làm"));
        authorizationService.checkOwnerOrPermissionInShop(shift.getShop(), "PERM_SHIFT_UPDATE");
        validateRequest(shiftRequest);
        shift.setStartTime(shiftRequest.getStartTime());
        shift.setEndTime(shiftRequest.getEndTime());
        shift.setName(shift.getName());
        return mapper.toResponse(repository.save(shift));
    }

    @Override
    public void delete(Long id) {
        ShiftEntity shift = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ca làm"));
        authorizationService.checkOwnerOrPermissionInShop(shift.getShop(), "PERM_SHIFT_DELETE");
        if (shift.getStatus() != ShiftStatus.ACTIVE) {
            throw new InvalidStatusException("ca làm");
        }
        shift.setStatus(ShiftStatus.INACTIVE);
        repository.save(shift);
    }

    @Override
    public ShiftResponse getById(Long id) {
        ShiftEntity shift = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ca làm"));
        authorizationService.checkOwnerOrPermissionInShop(shift.getShop(), "PERM_SHIFT_VIEW");
        return mapper.toResponse(shift);
    }

    @Override
    public Page<ShiftResponse> getAllByShopId(Long shopId, ShiftStatus status, Pageable pageable) {
        Page<ShiftEntity> shifts = status == null
                ? repository.findAllByShopId(shopId, pageable)
                : repository.findAllByShopIdAndStatus(shopId, status, pageable);
        return shifts.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long shiftId) {
        ShiftEntity shift = repository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Ca làm"));
        authorizationService.checkOwnerOrPermissionInShop(shift.getShop(), "PERM_SHIFT_VIEW");
        if (shift.getStatus() != ShiftStatus.INACTIVE) {
            throw new InvalidStatusException("ca làm");
        }
        shift.setStatus(ShiftStatus.ACTIVE);
        repository.save(shift);
    }
}
