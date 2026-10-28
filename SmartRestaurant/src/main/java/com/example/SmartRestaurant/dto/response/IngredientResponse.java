package com.example.SmartRestaurant.dto.response;

import com.example.SmartRestaurant.common.enums.IngredientStatus;
import com.example.SmartRestaurant.common.enums.IngredientType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IngredientResponse {
    private Long id;
    private String name;
    private IngredientType type;
    private String unit;
    private BigDecimal minStock;
    private BigDecimal yieldRate;
    private BigDecimal currentStock;
    private IngredientStatus status;
}
