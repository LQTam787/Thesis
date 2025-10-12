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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(adminService.updateUser(userId, updateUserRequestDto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // Recipe Management Endpoints

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        return ResponseEntity.ok(adminService.getAllRecipes());
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable("id") Long recipeId) {
        return ResponseEntity.ok(adminService.getRecipeById(recipeId));
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeRequestDto recipeRequestDto) {
        return ResponseEntity.ok(adminService.createRecipe(recipeRequestDto));
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable("id") Long recipeId, @RequestBody RecipeRequestDto recipeRequestDto) {
        return ResponseEntity.ok(adminService.updateRecipe(recipeId, recipeRequestDto));
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") Long recipeId) {
        adminService.deleteRecipe(recipeId);
        return ResponseEntity.ok("Recipe deleted successfully!");
    }

    // Goal Management Endpoints

    @GetMapping("/goals")
    public ResponseEntity<List<GoalDto>> getAllGoals() {
        return ResponseEntity.ok(adminService.getAllGoals());
    }

    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalDto> getGoalById(@PathVariable("id") Long goalId) {
        return ResponseEntity.ok(adminService.getGoalById(goalId));
    }

    @PostMapping("/goals")
    public ResponseEntity<GoalDto> createGoal(@RequestBody GoalDto goalDto) {
        return ResponseEntity.ok(adminService.createGoal(goalDto));
    }

    @PutMapping("/goals/{id}")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable("id") Long goalId, @RequestBody GoalDto goalDto) {
        return ResponseEntity.ok(adminService.updateGoal(goalId, goalDto));
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable("id") Long goalId) {
        adminService.deleteGoal(goalId);
        return ResponseEntity.ok("Goal deleted successfully!");
    }

    // Nutrition Plan Management Endpoints

    @GetMapping("/plans")
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans() {
        return ResponseEntity.ok(adminService.getAllNutritionPlans());
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<NutritionPlanDto> getNutritionPlanById(@PathVariable("id") Long planId) {
        return ResponseEntity.ok(adminService.getNutritionPlanById(planId));
    }

    @PostMapping("/plans")
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@RequestBody NutritionPlanDto nutritionPlanDto) {
        return ResponseEntity.ok(adminService.createNutritionPlan(nutritionPlanDto));
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<NutritionPlanDto> updateNutritionPlan(@PathVariable("id") Long planId, @RequestBody NutritionPlanDto nutritionPlanDto) {
        return ResponseEntity.ok(adminService.updateNutritionPlan(planId, nutritionPlanDto));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<String> deleteNutritionPlan(@PathVariable("id") Long planId) {
        adminService.deleteNutritionPlan(planId);
        return ResponseEntity.ok("Nutrition plan deleted successfully!");
    }
}

