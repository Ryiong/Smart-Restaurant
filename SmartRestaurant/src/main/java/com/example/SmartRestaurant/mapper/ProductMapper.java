package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.ProductRequest;
import com.example.SmartRestaurant.dto.response.ProductResponse;
import com.example.SmartRestaurant.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductEntity toEntity(ProductRequest request) {
        return ProductEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    public ProductResponse toResponse(ProductEntity product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .build();
    }
}
