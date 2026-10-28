package com.example.SmartRestaurant.mapper;

import com.example.SmartRestaurant.dto.request.ShopRequest;
import com.example.SmartRestaurant.dto.response.ShopResponse;
import com.example.SmartRestaurant.entity.ShopEntity;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    public ShopEntity toEntity(ShopRequest request) {
        return ShopEntity.builder()
                .name(request.getName().trim().toLowerCase())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .build();
    }

    public ShopResponse toResponse(ShopEntity shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .phoneNumber(shop.getPhoneNumber())
                .openTime(shop.getOpenTime())
                .closeTime(shop.getCloseTime())
                .build();
    }
}
