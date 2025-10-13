package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Food Management", description = "Endpoints for managing food items")
@SecurityRequirement(name = "bearerAuth")
public class FoodController {

    private final FoodService foodService;
    private final ModelMapper modelMapper;

    public FoodController(FoodService foodService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Create a new food item", description = "Adds a new food item to the database. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<FoodItemDto> createFoodItem(@RequestBody FoodItemDto foodItemDto) {
        FoodItem foodItem = modelMapper.map(foodItemDto, FoodItem.class);
        FoodItem savedFoodItem = foodService.save(foodItem);
        return ResponseEntity.ok(modelMapper.map(savedFoodItem, FoodItemDto.class));
    }

    @Operation(summary = "Get a food item by ID", description = "Retrieves details of a specific food item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item found"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDto> getFoodItem(@PathVariable Long id) {
        return foodService.findOne(id)
                .map(foodItem -> ResponseEntity.ok(modelMapper.map(foodItem, FoodItemDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all food items", description = "Retrieves a list of all available food items.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<FoodItemDto>> getAllFoodItems() {
        List<FoodItemDto> foodItemDtos = foodService.findAll().stream()
                .map(foodItem -> modelMapper.map(foodItem, FoodItemDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemDtos);
    }

    @Operation(summary = "Update a food item", description = "Updates the details of an existing food item. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
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

    @Operation(summary = "Delete a food item", description = "Deletes a food item from the database. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Food item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
