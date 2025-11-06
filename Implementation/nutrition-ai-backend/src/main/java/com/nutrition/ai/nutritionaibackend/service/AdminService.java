package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;

import java.util.List;

/**
 * Interface Service quản lý các hoạt động dành cho quản trị viên (Admin).
 * Nó định nghĩa các phương thức CRUD (Create, Read, Update, Delete) cho
 * User, Recipe, Goal và Nutrition Plan, cho phép Admin kiểm soát dữ liệu hệ thống.
 */
public interface AdminService {
    // Luồng User Management:
    // Admin có thể xem, tìm, cập nhật và xóa thông tin người dùng.
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long userId);

    UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long userId);

    // Luồng Recipe Management:
    // Admin quản lý các công thức nấu ăn, bao gồm xem, tạo, cập nhật và xóa.
    List<RecipeDto> getAllRecipes();

    RecipeDto getRecipeById(Long recipeId);

    RecipeDto createRecipe(RecipeRequestDto recipeRequestDto);

    RecipeDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto);

    void deleteRecipe(Long recipeId);

    // Luồng Goal Management:
    // Admin quản lý các mục tiêu dinh dưỡng có sẵn trong hệ thống (ví dụ: Tăng cơ, Giảm cân).
    List<GoalDto> getAllGoals();

    GoalDto getGoalById(Long goalId);

    GoalDto createGoal(GoalDto goalDto);

    GoalDto updateGoal(Long goalId, GoalDto goalDto);

    void deleteGoal(Long goalId);

    // Luồng Nutrition Plan Management:
    // Admin quản lý các kế hoạch dinh dưỡng trong hệ thống.
    List<NutritionPlanDto> getAllNutritionPlans();

    NutritionPlanDto getNutritionPlanById(Long planId);

    NutritionPlanDto createNutritionPlan(NutritionPlanDto nutritionPlanDto);

    NutritionPlanDto updateNutritionPlan(Long planId, NutritionPlanDto nutritionPlanDto);

    void deleteNutritionPlan(Long planId);
}