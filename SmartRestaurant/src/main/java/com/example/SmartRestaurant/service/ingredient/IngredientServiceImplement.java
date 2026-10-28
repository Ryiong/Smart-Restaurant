package com.example.SmartRestaurant.service.ingredient;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.common.enums.IngredientType;
import com.example.SmartRestaurant.dto.request.IngredientRequest;
import com.example.SmartRestaurant.dto.response.IngredientResponse;
import com.example.SmartRestaurant.entity.IngredientEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.IngredientMapper;
import com.example.SmartRestaurant.repository.IngredientRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.SmartRestaurant.validator.IngredientValidator.validateIngredientRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class IngredientServiceImplement implements IngredientService {
    IngredientRepository repository;
    AuthorizationService authorizationService;
    IngredientMapper mapper;
    ShopRepository shopRepository;

    @Override
    public IngredientResponse create(IngredientRequest request, Long parentId) {
        ShopEntity shop = shopRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_INGREDIENT_CREATE");
        validateIngredientRequest(request);

        IngredientEntity ingredient = mapper.toEntity(request);
        if (repository.existsByNameAndShopId(ingredient.getName(), parentId)) {
            throw new ValidateException("Tên nguyên liệu đã tồn tại trong cửa hàng");
        }
        if (request.getType() == IngredientType.DRY) {
            ingredient.setYieldRate(BigDecimal.valueOf(0));
        }
        ingredient.setShop(shop);
        ingredient.setStatus(IngredientStatus.ACTIVE);
        ingredient.setCurrentStock(BigDecimal.valueOf(0));
        return mapper.toResponse(repository.save(ingredient));
    }

    @Override
    public IngredientResponse update(Long id, IngredientRequest request) {
        IngredientEntity ingredient = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nguyên liệu"));

        authorizationService.checkOwnerOrPermissionInShop(ingredient.getShop(), "PERM_INGREDIENT_UPDATE");
        validateIngredientRequest(request);
        IngredientEntity newIngredient = mapper.toEntity(request);
        if (repository.existsByNameAndShopIdAndIdNot(
                newIngredient.getName()
                , ingredient.getShop().getId()
                , ingredient.getId())) {
            throw new ValidateException("Tên nguyên liệu đã tồn tại trong cửa hàng");
        }
        ingredient.setName(newIngredient.getName());
        ingredient.setType(newIngredient.getType());
        ingredient.setUnit(newIngredient.getUnit());
        ingredient.setMinStock(newIngredient.getMinStock());
        ingredient.setYieldRate(request.getType() == IngredientType.DRY
                ? BigDecimal.valueOf(0)
                : newIngredient.getYieldRate());
        return mapper.toResponse(repository.save(ingredient));
    }

    @Override
    public void delete(Long id) {
        IngredientEntity ingredient = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nguyên liệu"));

        authorizationService.checkOwnerOrPermissionInShop(ingredient.getShop(), "PERM_INGREDIENT_DELETE");
        if (ingredient.getStatus() != IngredientStatus.ACTIVE) {
            throw new InvalidStatusException("nguyên liệu");
        }
        ingredient.setStatus(IngredientStatus.INACTIVE);
        repository.save(ingredient);
    }

    @Override
    public IngredientResponse getById(Long id) {
        IngredientEntity ingredient = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nguyên liệu"));

        authorizationService.checkOwnerOrPermissionInShop(ingredient.getShop(), "PERM_INGREDIENT_VIEW");
        return mapper.toResponse(ingredient);
    }

    @Override
    public Page<IngredientResponse> getAllByShopId(Long shopId, IngredientStatus status, Pageable pageable) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_INGREDIENT_VIEW");
        Page<IngredientEntity> ingredients = status == null
                ? repository.findAllByShopId(shopId, pageable)
                : repository.findAllByShopIdAndStatus(shopId, status, pageable);
        return ingredients.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long ingredientId) {
        IngredientEntity ingredient = repository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Nguyên liệu"));

        authorizationService.checkOwnerOrPermissionInShop(ingredient.getShop(), "PERM_INGREDIENT_ACTIVATE");
        if (ingredient.getStatus() != IngredientStatus.INACTIVE) {
            throw new InvalidStatusException("nguyên liệu");
        }
        ingredient.setStatus(IngredientStatus.ACTIVE);
        repository.save(ingredient);
    }
}
