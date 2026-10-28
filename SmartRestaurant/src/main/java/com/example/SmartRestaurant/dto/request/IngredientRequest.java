package com.example.SmartRestaurant.dto.request;

import com.example.SmartRestaurant.common.enums.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientRequest {
    private String name;
    private IngredientType type;
    private String unit;
    private BigDecimal minStock;
    private BigDecimal yieldRate;
}
