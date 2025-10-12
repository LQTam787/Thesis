package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/food/{userId}")
    public ResponseEntity<FoodLogDto> logFood(@PathVariable String userId, @RequestBody FoodLogDto foodLogDto) {
        return ResponseEntity.ok(trackingService.logFood(userId, foodLogDto));
    }

    @PostMapping("/activity/{userId}")
    public ResponseEntity<ActivityLogDto> logActivity(@PathVariable String userId, @RequestBody ActivityLogDto activityLogDto) {
        return ResponseEntity.ok(trackingService.logActivity(userId, activityLogDto));
    }

    @GetMapping("/report/{userId}")
    public ResponseEntity<ProgressReportDto> getProgressReport(@PathVariable String userId, @RequestParam(defaultValue = "weekly") String type) {
        return ResponseEntity.ok(trackingService.generateProgressReport(userId, type));
    }
}
