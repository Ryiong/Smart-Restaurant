package com.example.SmartRestaurant.service.shop;

import com.example.SmartRestaurant.common.enums.ShopStatus;
import com.example.SmartRestaurant.dto.request.ReasonRequest;
import com.example.SmartRestaurant.dto.request.ShopLocationRequest;
import com.example.SmartRestaurant.dto.request.ShopRequest;
import com.example.SmartRestaurant.dto.response.ShopResponse;
import com.example.SmartRestaurant.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopService extends BaseService<ShopRequest, ShopResponse, Long> {

    List<ShopResponse> getAllShopOfCurrentUser();

    ShopResponse getShopByIdOfCurrentUser(Long id);

    void approve(Long shopId);

    void reject(Long shopId, ReasonRequest reason);

    void lock(Long shopId, ReasonRequest reason);

    void unlock(Long shopId);

    Page<ShopResponse> getAll(Pageable pageable, ShopStatus status);

    ShopResponse setLocation(Long shopId, ShopLocationRequest request);

}
