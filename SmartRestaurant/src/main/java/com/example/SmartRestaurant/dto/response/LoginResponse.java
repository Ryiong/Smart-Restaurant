package com.example.SmartRestaurant.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private Long userId;

    private String email;

    private String name;

    private String accessToken;

    private String refreshToken;
}
