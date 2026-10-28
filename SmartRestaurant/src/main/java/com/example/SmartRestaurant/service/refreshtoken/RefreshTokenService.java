package com.example.SmartRestaurant.service.refreshtoken;

import com.example.SmartRestaurant.entity.RefreshTokenEntity;

public interface RefreshTokenService {
    void create(RefreshTokenEntity refreshToken);

    void deleteByHashToken(String token);

    void deleteAllByUserId(Long userId);

    void deleteAllExpired();
}
