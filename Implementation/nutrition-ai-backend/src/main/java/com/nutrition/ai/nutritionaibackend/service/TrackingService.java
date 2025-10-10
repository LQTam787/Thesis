package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;

/**
 * Service interface for handling tracking-related operations such as logging food, activities,
 * and generating progress reports.
 */
public interface TrackingService {

    /**
     * Logs a food item for a specific user.
     *
     * @param userId The ID of the user.
     * @param foodLogDto The DTO containing food log details.
     * @return The created FoodLogDto.
     */
    FoodLogDto logFood(String userId, FoodLogDto foodLogDto);

    /**
     * Logs an activity for a specific user.
     *
     * @param userId The ID of the user.
     * @param activityLogDto The DTO containing activity log details.
     * @return The created ActivityLogDto.
     */
    ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto);

    /**
     * Generates a progress report for a specific user.
     *
     * @param userId The ID of the user.
     * @param reportType The type of report to generate (e.g., 'weekly', 'monthly').
     * @return A DTO containing the comprehensive progress report.
     */
    ProgressReportDto generateProgressReport(String userId, String reportType);

}
