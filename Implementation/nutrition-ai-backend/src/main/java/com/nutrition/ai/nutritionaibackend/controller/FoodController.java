package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final ModelMapper modelMapper;

    public FoodController(FoodService foodService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<FoodItemDto> createFoodItem(@RequestBody FoodItemDto foodItemDto) {
        FoodItem foodItem = modelMapper.map(foodItemDto, FoodItem.class);
        FoodItem savedFoodItem = foodService.save(foodItem);
        return ResponseEntity.ok(modelMapper.map(savedFoodItem, FoodItemDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDto> getFoodItem(@PathVariable Long id) {
        return foodService.findOne(id)
                .map(foodItem -> ResponseEntity.ok(modelMapper.map(foodItem, FoodItemDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FoodItemDto>> getAllFoodItems() {
        List<FoodItemDto> foodItemDtos = foodService.findAll().stream()
                .map(foodItem -> modelMapper.map(foodItem, FoodItemDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @RequestBody FoodItemDto foodItemDto) {
        return foodService.findOne(id)
                .map(existingFoodItem -> {
                    foodItemDto.setId(id);
                    FoodItem updatedFoodItem = foodService.save(modelMapper.map(foodItemDto, FoodItem.class));
                    return ResponseEntity.ok(modelMapper.map(updatedFoodItem, FoodItemDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
