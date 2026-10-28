package com.example.SmartRestaurant.service.category;

import com.example.SmartRestaurant.common.enums.CategoryStatus;
import com.example.SmartRestaurant.dto.request.CategoryRequest;
import com.example.SmartRestaurant.dto.response.CategoryResponse;
import com.example.SmartRestaurant.entity.CategoryEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.CategoryMapper;
import com.example.SmartRestaurant.repository.CategoryRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.SmartRestaurant.validator.CategoryValidator.validateCategoryRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;
    AuthorizationService authorizationService;
    CategoryMapper mapper;
    ShopRepository shopRepository;


    @Override
    public CategoryResponse create(CategoryRequest request, Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_CATEGORY_CREATE");
        validateCategoryRequest(request);

        CategoryEntity category = mapper.toEntity(request);
        if (repository.existsByNameAndShopId(category.getName(), shopId)) {
            throw new ValidateException("Tên danh mục đã tồn tại trong shop");
        }
        category.setShop(shop);
        category.setStatus(CategoryStatus.ACTIVE);
        return mapper.toResponse(repository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        CategoryEntity category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục"));

        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_CATEGORY_UPDATE");
        validateCategoryRequest(categoryRequest);
        CategoryEntity newCategory = mapper.toEntity(categoryRequest);
        if (repository.existsByNameAndShopIdAndIdNot(
                newCategory.getName()
                , category.getShop().getId()
                , category.getId())) {
            throw new ValidateException("Tên danh mục đã tồn tại trong shop");
        }
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        return mapper.toResponse(repository.save(category));
    }

    @Override
    public void delete(Long id) {
        CategoryEntity category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục"));

        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_CATEGORY_DELETE");
        if (category.getStatus() != CategoryStatus.ACTIVE) {
            throw new InvalidStatusException("danh mục");
        }
        category.setStatus(CategoryStatus.INACTIVE);
        repository.save(category);
    }

    @Override
    public CategoryResponse getById(Long id) {
        CategoryEntity category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục"));

        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_CATEGORY_VIEW");
        return mapper.toResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllByShopId(Long shopId, CategoryStatus status, Pageable pageable) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Shop"));

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_CATEGORY_VIEW");
        Page<CategoryEntity> categories = status == null
                ? repository.findAllByShopId(shopId, pageable)
                : repository.findAllByShopIdAndStatus(shopId, status, pageable);
        return categories.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long categoryId) {
        CategoryEntity category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Danh mục"));

        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_CATEGORY_ACTIVATE");
        if (category.getStatus() != CategoryStatus.INACTIVE) {
            throw new InvalidStatusException("Danh mục");
        }
        category.setStatus(CategoryStatus.ACTIVE);
        repository.save(category);
    }
}
