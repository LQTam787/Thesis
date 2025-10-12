package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for NutritionPlan entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanDto {
    private Long id;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalCaloriesGoal;
    private Long userId;
    private List<DailyMenuDto> dailyMenus;
}
