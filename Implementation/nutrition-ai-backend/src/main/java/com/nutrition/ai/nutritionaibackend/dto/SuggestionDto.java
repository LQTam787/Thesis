package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.ESuggestionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Suggestion entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDto {
    private Long id;
    private String suggestionText;
    private LocalDateTime createdDate;
    private ESuggestionCategory category;
    private Long userId;
}
