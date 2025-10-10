package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ActivityLog entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDto {
    private Long id;
    private LocalDateTime logDate;
    private Integer durationInMinutes;
    private Double caloriesBurned;
    private Long userId;
    private Long activityId;
}
