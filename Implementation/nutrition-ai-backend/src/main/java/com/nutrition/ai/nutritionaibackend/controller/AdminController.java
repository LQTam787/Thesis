package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;
import com.nutrition.ai.nutritionaibackend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Endpoints for administrative tasks")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all users. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their ID. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @Operation(summary = "Update a user", description = "Updates a user's details. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(adminService.updateUser(userId, updateUserRequestDto));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user from the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // Recipe Management Endpoints

    @Operation(summary = "Get all recipes", description = "Retrieves a list of all recipes. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of recipes")
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        return ResponseEntity.ok(adminService.getAllRecipes());
    }

    @Operation(summary = "Get recipe by ID", description = "Retrieves a single recipe by its ID. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Recipe found")
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable("id") Long recipeId) {
        return ResponseEntity.ok(adminService.getRecipeById(recipeId));
    }

    @Operation(summary = "Create a new recipe", description = "Adds a new recipe to the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Recipe created successfully")
    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeRequestDto recipeRequestDto) {
        return ResponseEntity.ok(adminService.createRecipe(recipeRequestDto));
    }

    @Operation(summary = "Update a recipe", description = "Updates a recipe's details. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Recipe updated successfully")
    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable("id") Long recipeId, @RequestBody RecipeRequestDto recipeRequestDto) {
        return ResponseEntity.ok(adminService.updateRecipe(recipeId, recipeRequestDto));
    }

    @Operation(summary = "Delete a recipe", description = "Deletes a recipe from the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Recipe deleted successfully")
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") Long recipeId) {
        adminService.deleteRecipe(recipeId);
        return ResponseEntity.ok("Recipe deleted successfully!");
    }

    // Goal Management Endpoints

    @Operation(summary = "Get all goals", description = "Retrieves a list of all goals. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of goals")
    @GetMapping("/goals")
    public ResponseEntity<List<GoalDto>> getAllGoals() {
        return ResponseEntity.ok(adminService.getAllGoals());
    }

    @Operation(summary = "Get goal by ID", description = "Retrieves a single goal by its ID. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Goal found")
    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalDto> getGoalById(@PathVariable("id") Long goalId) {
        return ResponseEntity.ok(adminService.getGoalById(goalId));
    }

    @Operation(summary = "Create a new goal", description = "Adds a new goal to the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Goal created successfully")
    @PostMapping("/goals")
    public ResponseEntity<GoalDto> createGoal(@RequestBody GoalDto goalDto) {
        return ResponseEntity.ok(adminService.createGoal(goalDto));
    }

    @Operation(summary = "Update a goal", description = "Updates a goal's details. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Goal updated successfully")
    @PutMapping("/goals/{id}")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable("id") Long goalId, @RequestBody GoalDto goalDto) {
        return ResponseEntity.ok(adminService.updateGoal(goalId, goalDto));
    }

    @Operation(summary = "Delete a goal", description = "Deletes a goal from the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Goal deleted successfully")
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable("id") Long goalId) {
        adminService.deleteGoal(goalId);
        return ResponseEntity.ok("Goal deleted successfully!");
    }

    // Nutrition Plan Management Endpoints

    @Operation(summary = "Get all nutrition plans", description = "Retrieves a list of all nutrition plans. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of nutrition plans")
    @GetMapping("/plans")
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans() {
        return ResponseEntity.ok(adminService.getAllNutritionPlans());
    }

    @Operation(summary = "Get nutrition plan by ID", description = "Retrieves a single nutrition plan by its ID. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Nutrition plan found")
    @GetMapping("/plans/{id}")
    public ResponseEntity<NutritionPlanDto> getNutritionPlanById(@PathVariable("id") Long planId) {
        return ResponseEntity.ok(adminService.getNutritionPlanById(planId));
    }

    @Operation(summary = "Create a new nutrition plan", description = "Adds a new nutrition plan to the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Nutrition plan created successfully")
    @PostMapping("/plans")
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@RequestBody NutritionPlanDto nutritionPlanDto) {
        return ResponseEntity.ok(adminService.createNutritionPlan(nutritionPlanDto));
    }

    @Operation(summary = "Update a nutrition plan", description = "Updates a nutrition plan's details. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Nutrition plan updated successfully")
    @PutMapping("/plans/{id}")
    public ResponseEntity<NutritionPlanDto> updateNutritionPlan(@PathVariable("id") Long planId, @RequestBody NutritionPlanDto nutritionPlanDto) {
        return ResponseEntity.ok(adminService.updateNutritionPlan(planId, nutritionPlanDto));
    }

    @Operation(summary = "Delete a nutrition plan", description = "Deletes a nutrition plan from the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Nutrition plan deleted successfully")
    @DeleteMapping("/plans/{id}")
    public ResponseEntity<String> deleteNutritionPlan(@PathVariable("id") Long planId) {
        adminService.deleteNutritionPlan(planId);
        return ResponseEntity.ok("Nutrition plan deleted successfully!");
    }
}

