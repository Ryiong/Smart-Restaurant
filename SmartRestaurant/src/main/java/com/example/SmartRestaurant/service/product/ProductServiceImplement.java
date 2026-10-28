package com.example.SmartRestaurant.service.product;

import com.example.SmartRestaurant.common.enums.ProductStatus;
import com.example.SmartRestaurant.config.cloudinary.service.CloudinaryService;
import com.example.SmartRestaurant.dto.request.ProductRequest;
import com.example.SmartRestaurant.dto.response.ProductResponse;
import com.example.SmartRestaurant.entity.CategoryEntity;
import com.example.SmartRestaurant.entity.ProductEntity;
import com.example.SmartRestaurant.entity.RecipeEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.ProductMapper;
import com.example.SmartRestaurant.repository.CategoryRepository;
import com.example.SmartRestaurant.repository.ProductRepository;
import com.example.SmartRestaurant.repository.RecipeRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.SmartRestaurant.validator.ProductValidator.validateProductRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProductServiceImplement implements ProductService {
    ProductRepository repository;
    AuthorizationService authorizationService;
    ProductMapper mapper;
    CategoryRepository categoryRepository;

    RecipeRepository recipeRepository;

    CloudinaryService cloudinaryService;

    @Override
    public ProductResponse create(ProductRequest productRequest, Long parentId) {
        CategoryEntity category = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Danh mục"));
        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_PRODUCT_CREATE");
        validateProductRequest(productRequest);

        ProductEntity product = mapper.toEntity(productRequest);
        if (repository.existsByNameAndCategoryId(product.getName(), parentId)) {
            throw new ValidateException("Tên sản phẩm đã tồn tại trong danh mục");
        }
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);
        ProductEntity savedProduct = repository.save(product);
        if (productRequest.getImageFile() != null && !productRequest.getImageFile().isEmpty()) {
            savedProduct.setImageUrl(cloudinaryService.uploadProductImage(productRequest.getImageFile(), savedProduct.getId()));
        }
        RecipeEntity recipe = recipeRepository.save(RecipeEntity.builder()
                .product(savedProduct)
                .build());
        savedProduct.setRecipe(recipe);
        return mapper.toResponse(repository.save(savedProduct));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sản phẩm"));

        authorizationService.checkOwnerOrPermissionInShop(product.getCategory().getShop(), "PERM_PRODUCT_UPDATE");
        validateProductRequest(productRequest);
        ProductEntity newProduct = mapper.toEntity(productRequest);
        if (repository.existsByNameAndCategoryIdAndIdNot(
                newProduct.getName()
                , product.getCategory().getId()
                , product.getId())) {
            throw new ValidateException("Tên sản phẩm đã tồn tại trong danh mục");
        }
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setPrice(newProduct.getPrice());
        if (productRequest.getImageFile() != null && !productRequest.getImageFile().isEmpty()) {
            product.setImageUrl(cloudinaryService.uploadProductImage(productRequest.getImageFile(), product.getId()));
        }
        return mapper.toResponse(repository.save(product));
    }

    @Override
    public void delete(Long id) {
        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sản phẩm"));

        authorizationService.checkOwnerOrPermissionInShop(product.getCategory().getShop(), "PERM_PRODUCT_DELETE");
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new InvalidStatusException("sản phẩm");
        }
        product.setStatus(ProductStatus.INACTIVE);
        repository.save(product);
    }

    @Override
    public ProductResponse getById(Long id) {
        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sản phẩm"));

        authorizationService.checkOwnerOrPermissionInShop(product.getCategory().getShop(), "PERM_PRODUCT_VIEW");
        return mapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllByCategoryId(Long categoryId, ProductStatus status, Pageable pageable) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Danh mục"));

        authorizationService.checkOwnerOrPermissionInShop(category.getShop(), "PERM_PRODUCT_VIEW");
        Page<ProductEntity> products = status == null
                ? repository.findAllByCategoryId(categoryId, pageable)
                : repository.findAllByCategoryIdAndStatus(categoryId, status, pageable);
        return products.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long productId) {
        ProductEntity product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm"));

        authorizationService.checkOwnerOrPermissionInShop(product.getCategory().getShop(), "PERM_PRODUCT_ACTIVATE");
        if (product.getStatus() != ProductStatus.INACTIVE) {
            throw new InvalidStatusException("sản phẩm");
        }
        product.setStatus(ProductStatus.ACTIVE);
        repository.save(product);
    }
}
