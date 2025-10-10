package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for MealFoodItem entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealFoodItemDto {
    private Long id;
    private Double quantity;
    private String unit;
    private Long mealId;
    private Long foodItemId;
}
