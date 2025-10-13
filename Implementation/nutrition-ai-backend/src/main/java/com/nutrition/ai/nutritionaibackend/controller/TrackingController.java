package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tracking")
@Tag(name = "Progress Tracking", description = "Endpoints for logging food, activities, and generating progress reports")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @Operation(summary = "Log a food item", description = "Logs a food item that a user has consumed.")
    @ApiResponse(responseCode = "200", description = "Food logged successfully")
    @PostMapping("/food/{userId}")
    public ResponseEntity<FoodLogDto> logFood(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody FoodLogDto foodLogDto) {
        return ResponseEntity.ok(trackingService.logFood(userId, foodLogDto));
    }

    @Operation(summary = "Log an activity", description = "Logs a physical activity that a user has completed.")
    @ApiResponse(responseCode = "200", description = "Activity logged successfully")
    @PostMapping("/activity/{userId}")
    public ResponseEntity<ActivityLogDto> logActivity(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody ActivityLogDto activityLogDto) {
        return ResponseEntity.ok(trackingService.logActivity(userId, activityLogDto));
    }

    @Operation(summary = "Generate a progress report", description = "Generates a progress report for a user, which can be of different types (e.g., weekly, monthly).")
    @ApiResponse(responseCode = "200", description = "Report generated successfully")
    @GetMapping("/report/{userId}")
    public ResponseEntity<ProgressReportDto> getProgressReport(
            @Parameter(description = "ID of the user") @PathVariable String userId, 
            @Parameter(description = "Type of the report (e.g., weekly, monthly)") @RequestParam(defaultValue = "weekly") String type) {
        return ResponseEntity.ok(trackingService.generateProgressReport(userId, type));
    }
}
