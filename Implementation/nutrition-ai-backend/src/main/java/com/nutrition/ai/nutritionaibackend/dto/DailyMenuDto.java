package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for DailyMenu entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMenuDto {
    private Long id;
    private LocalDate date;
    private Long nutritionPlanId;
    private List<MealDto> meals;
}
