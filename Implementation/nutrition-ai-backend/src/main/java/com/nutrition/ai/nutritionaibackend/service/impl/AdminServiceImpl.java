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
 * Nguyên lý hoạt động:
 * - Sử dụng ModelMapper để chuyển đổi giữa các DTO (Data Transfer Object) và Entity (Domain Model).
 * - Sử dụng các Repository (JPA/Spring Data JPA) để tương tác với cơ sở dữ liệu.
 * - Triển khai các phương thức CRUD cơ bản (Create, Read, Update, Delete) cho từng loại tài nguyên.
 * - Xử lý lỗi bằng cách ném ResourceNotFoundException khi không tìm thấy tài nguyên.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    // Inject các Repository và ModelMapper thông qua Lombok @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecipeRepository recipeRepository;
    private final GoalRepository goalRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final ModelMapper modelMapper;

    // --- User Management Implementation ---

    /**
     * Lấy danh sách tất cả người dùng.
     * Luồng hoạt động:
     * 1. Gọi `userRepository.findAll()` để lấy tất cả User Entity.
     * 2. Sử dụng Stream API và ModelMapper để ánh xạ từng User Entity sang UserResponseDto.
     * 3. Thu thập kết quả vào một List.
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
     * Luồng hoạt động:
     * 1. Gọi `userRepository.findById(userId)`.
     * 2. Nếu không tìm thấy, ném ResourceNotFoundException.
     * 3. Nếu tìm thấy, ánh xạ User Entity sang UserResponseDto và trả về.
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
     * Luồng hoạt động:
     * 1. **Tìm kiếm**: Tìm User hiện có bằng `userId`. Nếu không có, ném ResourceNotFoundException.
     * 2. **Cập nhật thông tin cơ bản**: Thiết lập `username` và `email` từ DTO (`updateUserRequestDto`).
     * 3. **Xử lý Roles**:
     * a. Lấy danh sách tên role (`String`) từ DTO.
     * b. Chuyển đổi từng tên role thành `ERole` (Enum).
     * c. Dùng `roleRepository.findByName()` để tìm Role Entity tương ứng.
     * d. Nếu Role không tồn tại (lỗi dữ liệu/logic), ném ResourceNotFoundException.
     * e. Thu thập các Role Entity thành một Set và thiết lập cho User.
     * 4. **Lưu**: Gọi `userRepository.save(user)` để lưu các thay đổi vào DB.
     * 5. **Trả về**: Ánh xạ User Entity đã lưu sang UserResponseDto và trả về.
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
                // Ánh xạ từng tên role (String) sang Role Entity
                .map(roleName -> {
                    try {
                        return roleRepository.findByName(ERole.valueOf(roleName)) // Tìm Role Entity
                                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
                    } catch (IllegalArgumentException e) {
                        throw new ResourceNotFoundException("Role", "name", roleName);
                    }
                })
                .collect(Collectors.toSet());
        user.setRoles(roles); // 4. Thiết lập Role mới

        // 5. Lưu và 6. Chuyển đổi/Trả về
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    /**
     * Xóa người dùng bằng ID.
     * Luồng hoạt động:
     * 1. Tìm User bằng `userId`. Nếu không có, ném ResourceNotFoundException.
     * 2. Gọi `userRepository.delete(user)` để xóa Entity.
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
     * Luồng hoạt động: Tương tự getAllUsers, chuyển đổi Recipe Entity sang RecipeDto.
     */
    @Override
    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy công thức bằng ID.
     * Luồng hoạt động: Tương tự getUserById, tìm Recipe và ánh xạ sang RecipeDto.
     */
    @Override
    public RecipeDto getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));
        return modelMapper.map(recipe, RecipeDto.class);
    }

    /**
     * Tạo công thức mới.
     * Luồng hoạt động:
     * 1. **Tạo FoodItem**: Khởi tạo FoodItem Entity và thiết lập các trường dinh dưỡng từ `RecipeRequestDto`.
     * 2. **Tạo Recipe**: Khởi tạo Recipe Entity, **liên kết** `FoodItem` vừa tạo, và thiết lập các trường Recipe khác.
     * 3. **Lưu**: Gọi `recipeRepository.save(recipe)`. Giả định `Recipe` có thiết lập CascadeType.ALL cho `FoodItem`,
     * nên FoodItem sẽ được tự động lưu cùng với Recipe.
     * 4. **Trả về**: Ánh xạ Recipe Entity đã lưu sang RecipeDto và trả về.
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
     * Luồng hoạt động:
     * 1. **Tìm kiếm**: Tìm Recipe hiện có bằng `recipeId`. Nếu không có, ném ResourceNotFoundException.
     * 2. **Cập nhật FoodItem**: Lấy `FoodItem` liên quan (đã được tải cùng với Recipe) và cập nhật tất cả thông tin dinh dưỡng từ DTO.
     * 3. **Cập nhật Recipe**: Cập nhật các trường Recipe khác (`instructions`, `preparationTime`).
     * 4. **Lưu**: Gọi `recipeRepository.save(recipe)`. Thao tác này sẽ cập nhật cả Recipe và FoodItem liên quan.
     * 5. **Trả về**: Ánh xạ Recipe Entity đã lưu sang RecipeDto và trả về.
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
     * Luồng hoạt động:
     * 1. Kiểm tra sự tồn tại của Recipe bằng `recipeRepository.existsById()`. Nếu không, ném ResourceNotFoundException.
     * 2. Gọi `recipeRepository.deleteById(recipeId)`.
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
     * Luồng hoạt động: Tương tự getAllUsers, chuyển đổi Goal Entity sang GoalDto.
     */
    @Override
    public List<GoalDto> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(goal -> modelMapper.map(goal, GoalDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy mục tiêu bằng ID.
     * Luồng hoạt động: Tương tự getUserById, tìm Goal và ánh xạ sang GoalDto.
     */
    @Override
    public GoalDto getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
        return modelMapper.map(goal, GoalDto.class);
    }

    /**
     * Tạo mục tiêu mới.
     * Luồng hoạt động:
     * 1. **Tìm User**: Dùng `goalDto.getUserId()` để tìm User Entity liên quan. Nếu không có, ném ResourceNotFoundException.
     * 2. **Chuyển đổi**: Ánh xạ `GoalDto` sang `Goal` Entity.
     * 3. **Liên kết**: Thiết lập User Entity đã tìm thấy cho Goal.
     * 4. **Lưu**: Gọi `goalRepository.save(goal)`.
     * 5. **Trả về**: Ánh xạ Goal Entity đã lưu sang GoalDto và trả về.
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
     * Luồng hoạt động:
     * 1. **Tìm Goal**: Tìm Goal hiện có bằng `goalId`. Nếu không có, ném ResourceNotFoundException.
     * 2. **Tìm User**: Dùng `goalDto.getUserId()` để tìm User Entity mới/hiện tại. Nếu không có, ném ResourceNotFoundException.
     * 3. **Cập nhật**: Cập nhật tất cả các trường Goal (targetWeight, targetDate, goalType, status) và **thiết lập lại** User Entity.
     * 4. **Lưu**: Gọi `goalRepository.save(goal)`.
     * 5. **Trả về**: Ánh xạ Goal Entity đã lưu sang GoalDto và trả về.
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
     * Luồng hoạt động: Kiểm tra sự tồn tại và gọi `goalRepository.deleteById()`.
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
     * Luồng hoạt động: Tương tự getAllUsers, chuyển đổi NutritionPlan Entity sang NutritionPlanDto.
     */
    @Override
    public List<NutritionPlanDto> getAllNutritionPlans() {
        return nutritionPlanRepository.findAll().stream()
                .map(plan -> modelMapper.map(plan, NutritionPlanDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy Kế hoạch Dinh dưỡng bằng ID.
     * Luồng hoạt động: Tương tự getUserById, tìm NutritionPlan và ánh xạ sang NutritionPlanDto.
     */
    @Override
    public NutritionPlanDto getNutritionPlanById(Long planId) {
        NutritionPlan plan = nutritionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", "id", planId));
        return modelMapper.map(plan, NutritionPlanDto.class);
    }

    /**
     * Tạo Kế hoạch Dinh dưỡng mới.
     * Luồng hoạt động: Tương tự `createGoal`.
     * 1. Tìm User bằng `nutritionPlanDto.getUserId()`.
     * 2. Ánh xạ DTO sang Entity.
     * 3. Thiết lập User Entity cho Plan.
     * 4. Lưu Plan.
     * 5. Ánh xạ và trả về PlanDto.
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
     * Luồng hoạt động: Tương tự `updateGoal`.
     * 1. Tìm Plan hiện có.
     * 2. Tìm User.
     * 3. Cập nhật các trường Plan (planName, startDate, endDate, nutritionGoal) và thiết lập User Entity.
     * 4. Lưu Plan.
     * 5. Ánh xạ và trả về PlanDto.
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
     * Luồng hoạt động: Kiểm tra sự tồn tại và gọi `nutritionPlanRepository.deleteById()`.
     */
    @Override
    public void deleteNutritionPlan(Long planId) {
        if (!nutritionPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException("NutritionPlan", "id", planId);
        }
        nutritionPlanRepository.deleteById(planId);
    }
}