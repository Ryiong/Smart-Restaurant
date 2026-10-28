package com.example.SmartRestaurant.service.authorization;

import com.example.SmartRestaurant.entity.ShopEntity;

public interface AuthorizationService {


    void checkOwnerOrPermissionInShop(ShopEntity shop, String permisisonName);
}
