package com.example.SmartRestaurant.dto.response;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalTime openTime;
    private LocalTime closeTime;
}
