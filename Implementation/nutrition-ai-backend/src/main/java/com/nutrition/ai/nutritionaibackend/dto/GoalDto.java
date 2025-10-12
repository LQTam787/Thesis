package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Goal entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
    private Long id;
    private Double targetWeight;
    private LocalDate targetDate;
    private EGoalType goalType;
    private String status;
    private Long userId;
}
