package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EMealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * Data Transfer Object for Meal entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {
    private Long id;
    private EMealType mealType;
    private LocalTime time;
    private Long dailyMenuId;
    private List<MealFoodItemDto> mealFoodItems;
}
