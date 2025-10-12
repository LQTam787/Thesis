package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;
import com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException;
import com.nutrition.ai.nutritionaibackend.model.domain.Role;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.model.domain.Recipe;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.model.domain.Goal;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.repository.GoalRepository;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.RecipeRepository;
import com.nutrition.ai.nutritionaibackend.repository.RoleRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecipeRepository recipeRepository;
    private final GoalRepository goalRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setUsername(updateUserRequestDto.getUsername());
        user.setEmail(updateUserRequestDto.getEmail());

        Set<Role> roles = updateUserRequestDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
    }

    // Recipe Management Implementation

    @Override
    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));
        return modelMapper.map(recipe, RecipeDto.class);
    }

    @Override
    public RecipeDto createRecipe(RecipeRequestDto recipeRequestDto) {
        FoodItem foodItem = new FoodItem();
        foodItem.setName(recipeRequestDto.getName());
        foodItem.setCalories(recipeRequestDto.getCalories());
        foodItem.setProtein(recipeRequestDto.getProtein());
        foodItem.setCarbs(recipeRequestDto.getCarbs());
        foodItem.setFats(recipeRequestDto.getFats());
        foodItem.setServingSize(recipeRequestDto.getServingSize());

        Recipe recipe = new Recipe();
        recipe.setFoodItem(foodItem);
        recipe.setInstructions(recipeRequestDto.getInstructions());
        recipe.setPreparationTime(recipeRequestDto.getPreparationTime());

        Recipe savedRecipe = recipeRepository.save(recipe);

        return modelMapper.map(savedRecipe, RecipeDto.class);
    }

    @Override
    public RecipeDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        FoodItem foodItem = recipe.getFoodItem();
        foodItem.setName(recipeRequestDto.getName());
        foodItem.setCalories(recipeRequestDto.getCalories());
        foodItem.setProtein(recipeRequestDto.getProtein());
        foodItem.setCarbs(recipeRequestDto.getCarbs());
        foodItem.setFats(recipeRequestDto.getFats());
        foodItem.setServingSize(recipeRequestDto.getServingSize());

        recipe.setInstructions(recipeRequestDto.getInstructions());
        recipe.setPreparationTime(recipeRequestDto.getPreparationTime());

        Recipe updatedRecipe = recipeRepository.save(recipe);
        return modelMapper.map(updatedRecipe, RecipeDto.class);
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe", "id", recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }

    // Goal Management Implementation

    @Override
    public List<GoalDto> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(goal -> modelMapper.map(goal, GoalDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GoalDto getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        return modelMapper.map(goal, GoalDto.class);
    }

    @Override
    public GoalDto createGoal(GoalDto goalDto) {
        User user = userRepository.findById(goalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", goalDto.getUserId()));
        Goal goal = modelMapper.map(goalDto, Goal.class);
        goal.setUser(user);
        Goal savedGoal = goalRepository.save(goal);
        return modelMapper.map(savedGoal, GoalDto.class);
    }

    @Override
    public GoalDto updateGoal(Long goalId, GoalDto goalDto) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        User user = userRepository.findById(goalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", goalDto.getUserId()));

        goal.setTargetWeight(goalDto.getTargetWeight());
        goal.setTargetDate(goalDto.getTargetDate());
        goal.setGoalType(goalDto.getGoalType());
        goal.setStatus(goalDto.getStatus());
        goal.setUser(user);

        Goal updatedGoal = goalRepository.save(goal);
        return modelMapper.map(updatedGoal, GoalDto.class);
    }

    @Override
    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new ResourceNotFoundException("Goal", "id", goalId);
        }
        goalRepository.deleteById(goalId);
    }

    // Nutrition Plan Management Implementation

    @Override
    public List<NutritionPlanDto> getAllNutritionPlans() {
        return nutritionPlanRepository.findAll().stream()
                .map(plan -> modelMapper.map(plan, NutritionPlanDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public NutritionPlanDto getNutritionPlanById(Long planId) {
        NutritionPlan plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", "id", planId));
        return modelMapper.map(plan, NutritionPlanDto.class);
    }

    @Override
    public NutritionPlanDto createNutritionPlan(NutritionPlanDto nutritionPlanDto) {
        User user = userRepository.findById(nutritionPlanDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", nutritionPlanDto.getUserId()));
        NutritionPlan plan = modelMapper.map(nutritionPlanDto, NutritionPlan.class);
        plan.setUser(user);
        NutritionPlan savedPlan = nutritionPlanRepository.save(plan);
        return modelMapper.map(savedPlan, NutritionPlanDto.class);
    }

    @Override
    public NutritionPlanDto updateNutritionPlan(Long planId, NutritionPlanDto nutritionPlanDto) {
        NutritionPlan plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", "id", planId));
        User user = userRepository.findById(nutritionPlanDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", nutritionPlanDto.getUserId()));

        plan.setPlanName(nutritionPlanDto.getPlanName());
        plan.setStartDate(nutritionPlanDto.getStartDate());
        plan.setEndDate(nutritionPlanDto.getEndDate());
        plan.setTotalCaloriesGoal(nutritionPlanDto.getTotalCaloriesGoal());
        plan.setUser(user);

        NutritionPlan updatedPlan = nutritionPlanRepository.save(plan);
        return modelMapper.map(updatedPlan, NutritionPlanDto.class);
    }

    @Override
    public void deleteNutritionPlan(Long planId) {
        if (!nutritionPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException("NutritionPlan", "id", planId);
        }
        nutritionPlanRepository.deleteById(planId);
    }
}

