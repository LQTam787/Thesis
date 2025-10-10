package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.service.NutritionPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/nutrition-plans")
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;

    public NutritionPlanController(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @PostMapping
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@PathVariable String username, @RequestBody NutritionPlanDto nutritionPlanDto) {
        // Placeholder logic
        return ResponseEntity.ok(nutritionPlanDto);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<NutritionPlanDto> getNutritionPlan(@PathVariable String username, @PathVariable Long planId) {
        // Placeholder logic
        return ResponseEntity.ok(new NutritionPlanDto());
    }

    @GetMapping
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans(@PathVariable String username) {
        // Placeholder logic
        return ResponseEntity.ok(Collections.emptyList());
    }
}
