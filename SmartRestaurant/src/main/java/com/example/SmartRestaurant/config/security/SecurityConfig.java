package com.example.SmartRestaurant.config.security;

import com.example.SmartRestaurant.config.jwt.JwtFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.SmartRestaurant.dto.response.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter, ObjectMapper objectMapper) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**"
                                , "/swagger-ui/**"
                                , "/swagger-ui.html"
                                , "/smart-restaurant/v1/auth/register"
                                , "/smart-restaurant/v1/auth/login"
                                , "/smart-restaurant/v1/auth/activate-account"
                                , "/smart-restaurant/v1/auth/resend-otp"
                                , "/smart-restaurant/v1/auth/refresh-token"
                                , "/ws"
                                , "/send"
                                ,"/"
                                ,"/login"
                                ,"/auth/**"
                                ,"/admin/**"
                                ,"/css/**"
                                ,"/js/**"
                                ,"/images/**"
                                ,"/api/**"
                                ,"/favicon.icon"
                                ,"/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                //mỗi request đi vào ứng dụng đều phải qua lớp jwtfilter
                //sau đó nó sẽ setauthentication cho context
                //cái usernamepasswordauthenticationfilter kia là filter mặc định đăng nhập của security
                //đóng vai trò như mốc vị trí để đặt jwtfilter trước nó
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> writeJson(objectMapper, res, 401, "Chưa đăng nhập hoặc phiên đã hết hạn"))
                        .accessDeniedHandler((req, res, e) -> writeJson(objectMapper, res, 403, "Bạn không có quyền thực hiện thao tác này"))
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static void writeJson(ObjectMapper mapper, jakarta.servlet.http.HttpServletResponse res, int status, String message) throws java.io.IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        mapper.writeValue(res.getWriter(), new ApiResponse<>(status, message, null, LocalDateTime.now()));
    }
}