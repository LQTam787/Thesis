package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for representing a comprehensive progress report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReportDto {

    private Long id;

    private EReportType reportType;

    private LocalDateTime generatedDate;

    private String summary;

    private List<FoodLogDto> foodLogs;

    private List<ActivityLogDto> activityLogs;

    private List<SuggestionDto> suggestions;
}
