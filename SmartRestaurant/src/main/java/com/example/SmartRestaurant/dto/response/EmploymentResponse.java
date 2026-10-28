package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmploymentResponse {
    private Long id;
    private Long salary;
    private LocalDate workFrom;
    private LocalDate endedAt;
    private EmploymentStatus status;
    private UserResponse user;
    private ShopResponse shop;
}
