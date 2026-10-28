package com.example.SmartRestaurant.service.product;

import com.example.SmartRestaurant.common.enums.ProductStatus;
import com.example.SmartRestaurant.dto.request.ProductRequest;
import com.example.SmartRestaurant.dto.response.ProductResponse;
import com.example.SmartRestaurant.service.base.ParentResourceBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends ParentResourceBaseService<ProductRequest, ProductResponse, Long> {
    Page<ProductResponse> getAllByCategoryId(Long categoryId, ProductStatus status, Pageable pageable);

    void activate(Long productId);

}
