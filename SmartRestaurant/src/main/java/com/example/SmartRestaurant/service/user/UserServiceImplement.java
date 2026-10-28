package com.example.SmartRestaurant.service.user;

import com.example.SmartRestaurant.common.enums.AccountType;
import com.example.SmartRestaurant.common.enums.UserStatus;
import com.example.SmartRestaurant.config.jwt.JwtService;
import com.example.SmartRestaurant.config.userdetails.CustomUserDetails;
import com.example.SmartRestaurant.config.userdetails.CustomUserDetailsService;
import com.example.SmartRestaurant.dto.request.ActivateRequest;
import com.example.SmartRestaurant.dto.request.LoginRequest;
import com.example.SmartRestaurant.dto.request.RegisterRequest;
import com.example.SmartRestaurant.dto.response.LoginResponse;
import com.example.SmartRestaurant.dto.response.UserResponse;
import com.example.SmartRestaurant.entity.*;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.OTPResendLimitExceededException;
import com.example.SmartRestaurant.exception.UnauthorizedException;
import com.example.SmartRestaurant.mapper.UserMapper;
import com.example.SmartRestaurant.repository.RoleRepository;
import com.example.SmartRestaurant.repository.UserRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import com.example.SmartRestaurant.service.otp.OTPService;
import com.example.SmartRestaurant.service.refreshtoken.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.example.SmartRestaurant.validator.AuthValidator.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional(noRollbackFor = OTPResendLimitExceededException.class)
public class UserServiceImplement implements UserService {
    UserRepository repository;

    RoleRepository roleRepository;


    UserMapper mapper;

    OTPService otpService;

    PasswordEncoder passwordEncoder;

    JwtService jwtService;

    CustomUserDetailsService userDetailsService;

    CurrentUserProvider currentUserProvider;

    RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${refresh-token.expiration}")
    Long refreshExpiration;

    @Override
    public void register(RegisterRequest registerRequest) {
        validateRegister(registerRequest);
        UserEntity user = mapper.toEntity(registerRequest);
        user.setStatus(UserStatus.PENDING);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setType(AccountType.OWNER);
        user.setRoles(Set.of(roleRepository.findByName("ROLE_OWNER")));
        UserEntity savedUser = repository.save(user);
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setUser(savedUser);
        otpEntity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpService.save(otpEntity);
    }

    @Override
    public void resendOTP(String email) {
        validateResendOTP(email);
        UserEntity user = repository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Người dùng");
        }
        if (!user.getStatus().equals(UserStatus.PENDING)) {
            throw new InvalidStatusException("tài khoản");
        }
        otpService.resendOTP(user.getId());
    }

    @Override
    public void activateAccount(ActivateRequest activateRequest) {
        validateActivateAccount(activateRequest);
        UserEntity user = repository.findByEmail(activateRequest.getEmail());
        if (user == null) {
            throw new NotFoundException("Người dùng");
        }
        if (!user.getStatus().equals(UserStatus.PENDING)) {
            throw new InvalidStatusException("tài khoản");
        }
        user.setStatus(UserStatus.ACTIVE);
        repository.save(user);
        otpService.activate(user.getId(), activateRequest.getCode());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validateLogin(loginRequest);
        UserEntity user = repository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new NotFoundException("Người dùng");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException();
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidStatusException("tài khoản");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntity role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            for (PermissionEntity permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);
        String refreshToken = jwtService.generateRefreshToken(userDetails, expiryDate);
        refreshTokenService.create(RefreshTokenEntity.builder().hashToken(String.valueOf(sha256(refreshToken))).user(user).expiredDate(expiryDate).build());
        return LoginResponse.builder().userId(user.getId()).email(user.getEmail()).name(user.getName()).accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public UserResponse getCurrentUser() {
        return mapper.toResponse(currentUserProvider.getCurrentUser().getUser());
    }

    @Override
    public String refresh(String refreshToken) {
        String email = jwtService.extractRefreshUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        return newAccessToken;
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.deleteByHashToken(String.valueOf(sha256(refreshToken)));
    }
}
