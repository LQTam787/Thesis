package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;

import java.util.List;

public interface AdminService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long userId);

    UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long userId);

    // Recipe Management
    List<RecipeDto> getAllRecipes();

    RecipeDto getRecipeById(Long recipeId);

    RecipeDto createRecipe(RecipeRequestDto recipeRequestDto);

    RecipeDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto);

    void deleteRecipe(Long recipeId);

    // Goal Management
    List<GoalDto> getAllGoals();

    GoalDto getGoalById(Long goalId);

    GoalDto createGoal(GoalDto goalDto);

    GoalDto updateGoal(Long goalId, GoalDto goalDto);

    void deleteGoal(Long goalId);

    // Nutrition Plan Management
    List<NutritionPlanDto> getAllNutritionPlans();

    NutritionPlanDto getNutritionPlanById(Long planId);

    NutritionPlanDto createNutritionPlan(NutritionPlanDto nutritionPlanDto);

    NutritionPlanDto updateNutritionPlan(Long planId, NutritionPlanDto nutritionPlanDto);

    void deleteNutritionPlan(Long planId);
}

