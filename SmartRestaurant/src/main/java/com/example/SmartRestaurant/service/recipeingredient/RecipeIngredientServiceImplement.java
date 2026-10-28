package com.example.SmartRestaurant.service.recipeingredient;

import com.example.SmartRestaurant.dto.request.RecipeIngredientCreateRequest;
import com.example.SmartRestaurant.dto.request.RecipeIngredientUpdateRequest;
import com.example.SmartRestaurant.dto.response.RecipeIngredientResponse;
import com.example.SmartRestaurant.entity.IngredientEntity;
import com.example.SmartRestaurant.entity.RecipeEntity;
import com.example.SmartRestaurant.entity.RecipeIngredientEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.RecipeIngredientMapper;
import com.example.SmartRestaurant.repository.IngredientRepository;
import com.example.SmartRestaurant.repository.RecipeIngredientRepository;
import com.example.SmartRestaurant.repository.RecipeRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.SmartRestaurant.validator.RecipeIngredientValidator.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class RecipeIngredientServiceImplement implements RecipeIngredientService {
    RecipeIngredientRepository repository;
    RecipeRepository recipeRepository;
    IngredientRepository ingredientRepository;
    AuthorizationService authorizationService;
    RecipeIngredientMapper mapper;

    @Override
    public List<RecipeIngredientResponse> create(Long recipeId, List<RecipeIngredientCreateRequest> requests) {
        //kiểm tra xem recipe có thuộc quyền sở hữu user không
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Công thức"));
        ShopEntity shop = recipe.getProduct().getCategory().getShop();

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_RECIPE_INGREDIENT_CREATE");
        //validate request
        validateRecipeIngredientCreate(recipeId, requests);
        //kiểm tra các id này có tồn tại trong shop không
        Set<Long> requestIngredientId = requests
                .stream()
                .map(x -> x.getIngredientId())
                .collect(Collectors.toSet());
        if (requestIngredientId.size() != requests.size()) {
            throw new ValidateException("Có nguyên liệu bị trùng trong request");
        }
        Set<IngredientEntity> foundIngredient =
                ingredientRepository.findAllByIdInAndShopId(requestIngredientId, shop.getId());
        Set<Long> foundIngredientId = foundIngredient
                .stream()
                .map(x -> x.getId())
                .collect(Collectors.toSet());
        Set<Long> invalidIngredientId = requestIngredientId
                .stream()
                .filter(x -> !foundIngredientId.contains(x))
                .collect(Collectors.toSet());//lấy các id không hợp lệ
        if (!invalidIngredientId.isEmpty()) {
            throw new NotFoundException("Nguyên liệu " + invalidIngredientId);
        }
        //kiểm tra các id này có tồn tại trong recipe chưa
        if (repository.existsByRecipeIdAndIngredientIdIn(recipeId, requestIngredientId)) {
            throw new ValidateException("Nguyên liệu bị trùng trong công thức");
        }
        //lưu các id
        Map<Long, IngredientEntity> ingredientMap = new HashMap<>();
        foundIngredient.forEach(x -> {
            ingredientMap.put(x.getId(), x);
        });
        List<RecipeIngredientEntity> entities = requests
                .stream()
                .map(x -> {
                    RecipeIngredientEntity entity = mapper.toEntity(x);
                    entity.setRecipe(recipe);
                    entity.setIngredient(ingredientMap.get(x.getIngredientId()));
                    return entity;
                })
                .collect(Collectors.toList());
        return repository.saveAll(entities)
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeIngredientResponse> update(Long recipeId, List<RecipeIngredientUpdateRequest> requests) {
        //kiểm tra xem recipe có thuộc quyền sở hữu user không
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Công thức"));
        ShopEntity shop = recipe.getProduct().getCategory().getShop();

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_RECIPE_INGREDIENT_UPDATE");
        //validate request
        validateRecipeIngredientUpdate(recipeId, requests);
        Set<Long> requestRecipeIngredientId = requests
                .stream()
                .map(x -> x.getId())
                .collect(Collectors.toSet());
        if (requests.size() != requestRecipeIngredientId.size()) {
            throw new ValidateException("Có nguyên liệu bị lặp trong danh sách cập nhật");
        }
        //kiểm tra tính hợp lệ của các recipe ingredient bằng id
        List<RecipeIngredientEntity> recipeIngredientEntities =
                repository.findAllByIdInAndRecipeId(requestRecipeIngredientId, recipeId);
        if (requests.size() != recipeIngredientEntities.size()) {
            throw new NotFoundException("RecipeIngredient không tồn tại hoặc không thuộc công thức");
        }
        Map<Long, BigDecimal> requestMap = new HashMap<>();
        requests.forEach(x -> {
            requestMap.put(x.getId(), x.getQuantity());
        });
        recipeIngredientEntities.forEach(x -> {
            x.setQuantity(requestMap.get(x.getId()));
        });
        return repository.saveAll(recipeIngredientEntities)
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long recipeId, List<Long> recipeIngredientIdList) {
        //kiểm tra xem recipe có thuộc quyền sở hữu user không
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Công thức"));
        ShopEntity shop = recipe.getProduct().getCategory().getShop();

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_RECIPE_INGREDIENT_DELETE");
        //validate request
        validateRecipeIngredientDelete(recipeId, recipeIngredientIdList);
        Set<Long> requestRecipeIngredientId = new HashSet<>(recipeIngredientIdList);
        if (recipeIngredientIdList.size() != requestRecipeIngredientId.size()) {
            throw new ValidateException("Có nguyên liệu bị lặp trong danh sách xóa");
        }
        //kiểm tra tính hợp lệ của các recipe ingredient bằng id
        List<RecipeIngredientEntity> recipeIngredientEntities =
                repository.findAllByIdInAndRecipeId(requestRecipeIngredientId, recipeId);
        if (recipeIngredientIdList.size() != recipeIngredientEntities.size()) {
            throw new NotFoundException("Định lượng không tồn tại hoặc không thuộc công thức");
        }
        repository.deleteAll(recipeIngredientEntities);
    }

    @Override
    public List<RecipeIngredientResponse> getByRecipeId(Long recipeId) {
        //kiểm tra xem recipe có thuộc quyền sở hữu user không
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Công thức"));
        ShopEntity shop = recipe.getProduct().getCategory().getShop();

        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_RECIPE_INGREDIENT_VIEW");
        return repository.findAllByRecipeId(recipeId)
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }
}
