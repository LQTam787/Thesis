package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for FoodLog entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodLogDto {
    private Long id;
    private LocalDateTime logDate;
    private Double quantity;
    private String unit;
    private Long userId;
    private Long foodItemId;
}
