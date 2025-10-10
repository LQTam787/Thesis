package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodItemDto> createFoodItem(@RequestBody FoodItemDto foodItemDto) {
        // Placeholder
        return ResponseEntity.ok(foodItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDto> getFoodItem(@PathVariable Long id) {
        // Placeholder
        return ResponseEntity.ok(new FoodItemDto());
    }

    @GetMapping
    public ResponseEntity<List<FoodItemDto>> getAllFoodItems() {
        // Placeholder
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @RequestBody FoodItemDto foodItemDto) {
        // Placeholder
        return ResponseEntity.ok(foodItemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        // Placeholder
        return ResponseEntity.noContent().build();
    }
}
