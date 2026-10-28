package com.example.SmartRestaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private LocalTime openTime;
    private LocalTime closeTime;
}
