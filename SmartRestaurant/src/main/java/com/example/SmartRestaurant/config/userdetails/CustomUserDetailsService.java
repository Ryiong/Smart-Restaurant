package com.example.SmartRestaurant.config.userdetails;

import com.example.SmartRestaurant.common.enums.UserStatus;
import com.example.SmartRestaurant.entity.UserEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {
    //đây là lớp service để tìm userentity gán cho customuserdetails
    //để security sử dụng
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailHasRoleAndPermission(email);
        if (user == null) {
            throw new NotFoundException("Người dùng");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidStatusException("tài khoản");
        }
        return new CustomUserDetails(user);
    }
}