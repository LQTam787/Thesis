package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Activity entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private String name;
    private Double caloriesBurnedPerHour;
}
