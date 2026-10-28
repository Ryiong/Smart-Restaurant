package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.CategoryStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryStatus status;
}
