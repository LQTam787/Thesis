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

/**
 * Implementation of the AdminService interface.
 * Handles administrative business logic for managing Users, Recipes, Goals, and Nutrition Plans.
 * Lớp này triển khai giao diện AdminService, cung cấp các chức năng quản trị viên
 * để quản lý Người dùng, Công thức (Recipe), Mục tiêu (Goal) và Kế hoạch Dinh dưỡng (Nutrition Plan).
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    // Inject các Repository và ModelMapper
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecipeRepository recipeRepository;
    private final GoalRepository goalRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final ModelMapper modelMapper;

    // --- User Management Implementation ---

    /**
     * Lấy danh sách tất cả người dùng.
     * 1. Lấy tất cả User Entity từ cơ sở dữ liệu.
     * 2. Chuyển đổi từng User Entity sang UserResponseDto.
     *
     * @return List<UserResponseDto> chứa tất cả người dùng.
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy người dùng bằng ID.
     * 1. Tìm User bằng userId. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Chuyển đổi User Entity sang UserResponseDto và trả về.
     *
     * @param userId ID của người dùng.
     * @return UserResponseDto của người dùng.
     */
    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return modelMapper.map(user, UserResponseDto.class);
    }

    /**
     * Cập nhật thông tin người dùng.
     * 1. Tìm User hiện có bằng userId. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Cập nhật username và email từ DTO.
     * 3. Chuyển đổi danh sách tên role từ DTO thành tập hợp Role Entity.
     * (Tìm từng Role bằng tên, ném ResourceNotFoundException nếu không tìm thấy Role).
     * 4. Thiết lập tập hợp Role mới cho User.
     * 5. Lưu User đã cập nhật.
     * 6. Chuyển đổi User Entity đã lưu thành UserResponseDto và trả về.
     *
     * @param userId ID của người dùng cần cập nhật.
     * @param updateUserRequestDto DTO chứa thông tin cập nhật.
     * @return UserResponseDto của người dùng đã cập nhật.
     */
    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        // 1. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // 2. Cập nhật thông tin cơ bản
        user.setUsername(updateUserRequestDto.getUsername());
        user.setEmail(updateUserRequestDto.getEmail());

        // 3. Xử lý và thiết lập Roles
        Set<Role> roles = updateUserRequestDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName)) // Tìm Role Entity
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles); // 4. Thiết lập Role mới

        // 5. Lưu và 6. Chuyển đổi/Trả về
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    /**
     * Xóa người dùng bằng ID.
     * 1. Tìm User bằng userId. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Xóa User khỏi cơ sở dữ liệu.
     *
     * @param userId ID của người dùng cần xóa.
     */
    @Override
    public void deleteUser(Long userId) {
        // 1. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        // 2. Xóa
        userRepository.delete(user);
    }

    // --- Recipe Management Implementation ---

    /**
     * Lấy danh sách tất cả công thức.
     */
    @Override
    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy công thức bằng ID.
     */
    @Override
    public RecipeDto getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));
        return modelMapper.map(recipe, RecipeDto.class);
    }

    /**
     * Tạo công thức mới.
     * 1. Tạo FoodItem Entity từ các trường dinh dưỡng trong DTO.
     * 2. Tạo Recipe Entity, liên kết FoodItem và thiết lập các trường khác (instructions, prepTime).
     * 3. Lưu Recipe (bao gồm cả FoodItem) vào cơ sở dữ liệu.
     * 4. Chuyển đổi và trả về RecipeDto.
     */
    @Override
    public RecipeDto createRecipe(RecipeRequestDto recipeRequestDto) {
        // 1. Tạo FoodItem
        FoodItem foodItem = new FoodItem();
        foodItem.setName(recipeRequestDto.getName());
        foodItem.setCalories(recipeRequestDto.getCalories());
        foodItem.setProtein(recipeRequestDto.getProtein());
        foodItem.setCarbs(recipeRequestDto.getCarbs());
        foodItem.setFats(recipeRequestDto.getFats());
        foodItem.setServingSize(recipeRequestDto.getServingSize());

        // 2. Tạo Recipe
        Recipe recipe = new Recipe();
        recipe.setFoodItem(foodItem); // Liên kết FoodItem
        recipe.setInstructions(recipeRequestDto.getInstructions());
        recipe.setPreparationTime(recipeRequestDto.getPreparationTime());

        // 3. Lưu (Giả định Recipe có cascade save cho FoodItem)
        Recipe savedRecipe = recipeRepository.save(recipe);

        // 4. Chuyển đổi và trả về
        return modelMapper.map(savedRecipe, RecipeDto.class);
    }

    /**
     * Cập nhật công thức hiện có.
     * 1. Tìm Recipe bằng recipeId. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Lấy FoodItem Entity liên quan và cập nhật tất cả thông tin dinh dưỡng.
     * 3. Cập nhật các trường Recipe khác (instructions, prepTime).
     * 4. Lưu Recipe đã cập nhật.
     * 5. Chuyển đổi và trả về RecipeDto.
     */
    @Override
    public RecipeDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto) {
        // 1. Tìm Recipe
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // 2. Cập nhật FoodItem
        FoodItem foodItem = recipe.getFoodItem();
        foodItem.setName(recipeRequestDto.getName());
        foodItem.setCalories(recipeRequestDto.getCalories());
        foodItem.setProtein(recipeRequestDto.getProtein());
        foodItem.setCarbs(recipeRequestDto.getCarbs());
        foodItem.setFats(recipeRequestDto.getFats());
        foodItem.setServingSize(recipeRequestDto.getServingSize());

        // 3. Cập nhật Recipe
        recipe.setInstructions(recipeRequestDto.getInstructions());
        recipe.setPreparationTime(recipeRequestDto.getPreparationTime());

        // 4. Lưu và 5. Chuyển đổi/Trả về
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return modelMapper.map(updatedRecipe, RecipeDto.class);
    }

    /**
     * Xóa công thức bằng ID.
     */
    @Override
    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe", "id", recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }

    // --- Goal Management Implementation ---

    /**
     * Lấy danh sách tất cả mục tiêu (Goal).
     */
    @Override
    public List<GoalDto> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(goal -> modelMapper.map(goal, GoalDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy mục tiêu bằng ID.
     */
    @Override
    public GoalDto getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        return modelMapper.map(goal, GoalDto.class);
    }

    /**
     * Tạo mục tiêu mới.
     * 1. Tìm User bằng userId từ GoalDto. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Chuyển GoalDto sang Goal Entity.
     * 3. Thiết lập User Entity cho Goal.
     * 4. Lưu Goal.
     * 5. Chuyển đổi và trả về GoalDto.
     */
    @Override
    public GoalDto createGoal(GoalDto goalDto) {
        // 1. Tìm User
        User user = userRepository.findById(goalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", goalDto.getUserId()));
        // 2. Chuyển đổi sang Entity và 3. Thiết lập User
        Goal goal = modelMapper.map(goalDto, Goal.class);
        goal.setUser(user);
        // 4. Lưu và 5. Trả về
        Goal savedGoal = goalRepository.save(goal);
        return modelMapper.map(savedGoal, GoalDto.class);
    }

    /**
     * Cập nhật mục tiêu hiện có.
     * 1. Tìm Goal hiện có. Ném ResourceNotFoundException nếu không tìm thấy.
     * 2. Tìm User mới (nếu userId bị thay đổi, mặc dù điều này không phổ biến).
     * 3. Cập nhật các trường Goal từ DTO.
     * 4. Thiết lập User Entity.
     * 5. Lưu Goal đã cập nhật.
     * 6. Chuyển đổi và trả về GoalDto.
     */
    @Override
    public GoalDto updateGoal(Long goalId, GoalDto goalDto) {
        // 1. Tìm Goal hiện có
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        // 2. Tìm User
        User user = userRepository.findById(goalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", goalDto.getUserId()));

        // 3. Cập nhật thông tin Goal
        goal.setTargetWeight(goalDto.getTargetWeight());
        goal.setTargetDate(goalDto.getTargetDate());
        goal.setGoalType(goalDto.getGoalType());
        goal.setStatus(goalDto.getStatus());
        goal.setUser(user); // 4. Thiết lập User (hoặc đảm bảo nó không thay đổi)

        // 5. Lưu và 6. Trả về
        Goal updatedGoal = goalRepository.save(goal);
        return modelMapper.map(updatedGoal, GoalDto.class);
    }

    /**
     * Xóa mục tiêu bằng ID.
     */
    @Override
    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new ResourceNotFoundException("Goal", "id", goalId);
        }
        goalRepository.deleteById(goalId);
    }

    // --- Nutrition Plan Management Implementation ---

    /**
     * Lấy danh sách tất cả Kế hoạch Dinh dưỡng.
     */
    @Override
    public List<NutritionPlanDto> getAllNutritionPlans() {
        return nutritionPlanRepository.findAll().stream()
                .map(plan -> modelMapper.map(plan, NutritionPlanDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy Kế hoạch Dinh dưỡng bằng ID.
     */
    @Override
    public NutritionPlanDto getNutritionPlanById(Long planId) {
        NutritionPlan plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", "id", planId));
        return modelMapper.map(plan, NutritionPlanDto.class);
    }

    /**
     * Tạo Kế hoạch Dinh dưỡng mới.
     * Logic tương tự như createGoal.
     */
    @Override
    public NutritionPlanDto createNutritionPlan(NutritionPlanDto nutritionPlanDto) {
        // 1. Tìm User
        User user = userRepository.findById(nutritionPlanDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", nutritionPlanDto.getUserId()));
        // 2. Chuyển đổi sang Entity và 3. Thiết lập User
        NutritionPlan plan = modelMapper.map(nutritionPlanDto, NutritionPlan.class);
        plan.setUser(user);
        // 4. Lưu và 5. Trả về
        NutritionPlan savedPlan = nutritionPlanRepository.save(plan);
        return modelMapper.map(savedPlan, NutritionPlanDto.class);
    }

    /**
     * Cập nhật Kế hoạch Dinh dưỡng hiện có.
     * Logic tương tự như updateGoal.
     */
    @Override
    public NutritionPlanDto updateNutritionPlan(Long planId, NutritionPlanDto nutritionPlanDto) {
        // 1. Tìm Plan hiện có
        NutritionPlan plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", "id", planId));
        // 2. Tìm User
        User user = userRepository.findById(nutritionPlanDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", nutritionPlanDto.getUserId()));

        // 3. Cập nhật thông tin Plan
        plan.setPlanName(nutritionPlanDto.getPlanName());
        plan.setStartDate(nutritionPlanDto.getStartDate());
        plan.setEndDate(nutritionPlanDto.getEndDate());
        plan.setNutritionGoal(nutritionPlanDto.getNutritionGoal());
        plan.setUser(user); // 4. Thiết lập User

        // 5. Lưu và 6. Trả về
        NutritionPlan updatedPlan = nutritionPlanRepository.save(plan);
        return modelMapper.map(updatedPlan, NutritionPlanDto.class);
    }

    /**
     * Xóa Kế hoạch Dinh dưỡng bằng ID.
     */
    @Override
    public void deleteNutritionPlan(Long planId) {
        if (!nutritionPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException("NutritionPlan", "id", planId);
        }
        nutritionPlanRepository.deleteById(planId);
    }
}