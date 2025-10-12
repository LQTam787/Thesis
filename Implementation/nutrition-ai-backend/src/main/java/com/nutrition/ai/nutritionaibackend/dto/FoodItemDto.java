package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for FoodItem entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemDto {
    private Long id;
    private String name;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private String servingSize;
    private Long recipeId;
}
