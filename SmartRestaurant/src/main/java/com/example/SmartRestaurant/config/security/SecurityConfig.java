package com.example.SmartRestaurant.config.security;

import com.example.SmartRestaurant.config.jwt.JwtFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**"
                                , "/swagger-ui/**"
                                , "/swagger-ui.html"
                                , "smart-restaurant/v1/auth/register"
                                , "smart-restaurant/v1/auth/login"
                                , "smart-restaurant/v1/auth/activate-account"
                                , "smart-restaurant/v1/auth/resend-otp"
                                , "smart-restaurant/v1/auth/refresh-token"
                                , "/ws"
                                , "/send"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                //mỗi request đi vào ứng dụng đều phải qua lớp jwtfilter
                //sau đó nó sẽ setauthentication cho context
                //cái usernamepasswordauthenticationfilter kia là filter mặc định đăng nhập của security
                //đóng vai trò như mốc vị trí để đặt jwtfilter trước nó
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}