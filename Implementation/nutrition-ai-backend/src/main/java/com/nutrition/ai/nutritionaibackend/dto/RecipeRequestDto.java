package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {
    // FoodItem fields
    private String name;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private String servingSize;

    // Recipe fields
    private String instructions;
    private Integer preparationTime;
}
