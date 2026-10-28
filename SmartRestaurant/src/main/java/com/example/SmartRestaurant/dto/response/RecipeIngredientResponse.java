package com.example.SmartRestaurant.dto.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecipeIngredientResponse {
    private Long id;
    private BigDecimal quantity;
    private String ingredientName;
    private String ingredientUnit;
}
