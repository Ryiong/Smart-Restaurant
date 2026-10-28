package com.example.SmartRestaurant.service.inventorydocumentitem;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.dto.request.InventoryDocumentItemRequest;
import com.example.SmartRestaurant.entity.IngredientEntity;
import com.example.SmartRestaurant.entity.InventoryDocumentEntity;
import com.example.SmartRestaurant.entity.InventoryDocumentItemEntity;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.repository.IngredientRepository;
import com.example.SmartRestaurant.repository.InventoryDocumentItemRepository;
import com.example.SmartRestaurant.validator.InventoryDocumentItemValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryDocumentItemServiceImplement implements InventoryDocumentItemService {
    InventoryDocumentItemRepository repository;
    IngredientRepository ingredientRepository;

    @Override
    public List<InventoryDocumentItemEntity> createAll(InventoryDocumentEntity documentEntity, List<InventoryDocumentItemRequest> itemRequests) {
        itemRequests.forEach(x -> {
            InventoryDocumentItemValidator.validateRequest(x);
        });
        //lấy tất cả các id nguyên liệu trong request
        Set<Long> ingredientIds = itemRequests
                .stream()
                .map(x -> x.getIngredientId())
                .collect(Collectors.toSet());
        //kiểm tra có nguyên liệu nào bị lặp trong request không

        if (ingredientIds.size() != itemRequests.size()) {
            throw new ValidateException("Có nguyên liệu bị trùng trong phiếu");
        }
        //kiểm tra các nguyên liệu có tồn tại/ tồn tại trong shop không
        Set<IngredientEntity> ingredients =
                ingredientRepository.findAllByIdInAndShopIdAndStatus(ingredientIds
                        , documentEntity.getShop().getId()
                        , IngredientStatus.ACTIVE);
        if (ingredientIds.size() != ingredients.size()) {
            throw new NotFoundException("Nguyên liệu");
        }
        Map<Long, IngredientEntity> ingredientMap = new HashMap<>();
        ingredients.forEach(x -> {
            ingredientMap.put(x.getId(), x);
        });
        List<InventoryDocumentItemEntity> items = itemRequests
                .stream()
                .map(x -> {
                    InventoryDocumentItemEntity entity = InventoryDocumentItemEntity.builder()
                            .inventoryDocument(documentEntity)
                            .ingredient(ingredientMap.get(x.getIngredientId()))
                            .quantity(x.getQuantity())
                            .build();
                    return entity;
                })
                .collect(Collectors.toList());
        return repository.saveAll(items);
    }
}
