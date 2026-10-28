package com.example.SmartRestaurant.repository;

import com.example.SmartRestaurant.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    void deleteAllByUserId(Long userId);

    void deleteAllByExpiredDateBefore(Date now);

    RefreshTokenEntity findByHashToken(String token);
}
