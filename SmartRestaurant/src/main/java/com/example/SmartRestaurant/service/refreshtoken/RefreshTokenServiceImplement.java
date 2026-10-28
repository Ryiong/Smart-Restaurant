package com.example.SmartRestaurant.service.refreshtoken;

import com.example.SmartRestaurant.entity.RefreshTokenEntity;
import com.example.SmartRestaurant.entity.UserEntity;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.repository.RefreshTokenRepository;
import com.example.SmartRestaurant.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class RefreshTokenServiceImplement implements RefreshTokenService {
    RefreshTokenRepository repository;

    UserRepository userRepository;

    @Override
    public void create(RefreshTokenEntity refreshToken) {
        repository.save(refreshToken);
    }

    @Override
    public void deleteByHashToken(String token) {
        RefreshTokenEntity refreshToken = repository.findByHashToken(token);
        if (refreshToken == null) {
            throw new NotFoundException("Refresh token");
        }
        repository.delete(refreshToken);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng"));
        repository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteAllExpired() {
        repository.deleteAllByExpiredDateBefore(new Date());
    }
}
