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
 *
 * <p>Các luồng chính bao gồm:</p>
 * <ul>
 *     <li><b>Quản lý người dùng (User Management):</b> Admin có thể xem, tìm kiếm, cập nhật và xóa thông tin người dùng.</li>
 *     <li><b>Quản lý công thức (Recipe Management):</b> Admin quản lý các công thức nấu ăn, bao gồm xem, tạo, cập nhật và xóa.</li>
 *     <li><b>Quản lý mục tiêu (Goal Management):</b> Admin quản lý các mục tiêu dinh dưỡng có sẵn trong hệ thống (ví dụ: Tăng cơ, Giảm cân).</li>
 *     <li><b>Quản lý kế hoạch dinh dưỡng (Nutrition Plan Management):</b> Admin quản lý các kế hoạch dinh dưỡng trong hệ thống.</li>
 * </ul>
 */
public interface AdminService {
    /**
     * Lấy tất cả người dùng trong hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin để xem danh sách người dùng.
     * 2. Truy vấn cơ sở dữ liệu để lấy tất cả các bản ghi người dùng.
     * 3. Chuyển đổi các thực thể người dùng thành DTOs (Data Transfer Objects) để trả về.
     * @return Danh sách các UserResponseDto.
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Lấy thông tin một người dùng cụ thể bằng ID.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với userId.
     * 2. Truy vấn cơ sở dữ liệu để tìm người dùng.
     * 3. Nếu tìm thấy, chuyển đổi thực thể người dùng thành UserResponseDto.
     * 4. Trả về UserResponseDto hoặc ném ngoại lệ nếu không tìm thấy.
     * @param userId ID của người dùng.
     * @return UserResponseDto chứa thông tin người dùng.
     */
    UserResponseDto getUserById(Long userId);

    /**
     * Cập nhật thông tin của một người dùng.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với userId và UpdateUserRequestDto.
     * 2. Tìm người dùng trong cơ sở dữ liệu bằng userId.
     * 3. Cập nhật các trường thông tin của người dùng với dữ liệu từ DTO.
     * 4. Lưu người dùng đã cập nhật vào cơ sở dữ liệu.
     * 5. Chuyển đổi người dùng đã cập nhật thành UserResponseDto và trả về.
     * @param userId ID của người dùng cần cập nhật.
     * @param updateUserRequestDto DTO chứa thông tin cập nhật.
     * @return UserResponseDto của người dùng đã được cập nhật.
     */
    UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    /**
     * Xóa một người dùng khỏi hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với userId.
     * 2. Kiểm tra sự tồn tại của người dùng.
     * 3. Xóa người dùng khỏi cơ sở dữ liệu.
     * @param userId ID của người dùng cần xóa.
     */
    void deleteUser(Long userId);

    /**
     * Lấy tất cả các công thức nấu ăn trong hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin để xem danh sách công thức.
     * 2. Truy vấn cơ sở dữ liệu để lấy tất cả các bản ghi công thức.
     * 3. Chuyển đổi các thực thể công thức thành RecipeDto để trả về.
     * @return Danh sách các RecipeDto.
     */
    List<RecipeDto> getAllRecipes();

    /**
     * Lấy thông tin một công thức nấu ăn cụ thể bằng ID.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với recipeId.
     * 2. Truy vấn cơ sở dữ liệu để tìm công thức.
     * 3. Nếu tìm thấy, chuyển đổi thực thể công thức thành RecipeDto.
     * 4. Trả về RecipeDto hoặc ném ngoại lệ nếu không tìm thấy.
     * @param recipeId ID của công thức.
     * @return RecipeDto chứa thông tin công thức.
     */
    RecipeDto getRecipeById(Long recipeId);

    /**
     * Tạo một công thức nấu ăn mới.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với RecipeRequestDto.
     * 2. Chuyển đổi RecipeRequestDto thành thực thể Recipe.
     * 3. Lưu thực thể Recipe mới vào cơ sở dữ liệu.
     * 4. Chuyển đổi thực thể đã lưu thành RecipeDto và trả về.
     * @param recipeRequestDto DTO chứa thông tin công thức mới.
     * @return RecipeDto của công thức đã được tạo.
     */
    RecipeDto createRecipe(RecipeRequestDto recipeRequestDto);

    /**
     * Cập nhật thông tin của một công thức nấu ăn.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với recipeId và RecipeRequestDto.
     * 2. Tìm công thức trong cơ sở dữ liệu bằng recipeId.
     * 3. Cập nhật các trường thông tin của công thức với dữ liệu từ DTO.
     * 4. Lưu công thức đã cập nhật vào cơ sở dữ liệu.
     * 5. Chuyển đổi công thức đã cập nhật thành RecipeDto và trả về.
     * @param recipeId ID của công thức cần cập nhật.
     * @param recipeRequestDto DTO chứa thông tin cập nhật.
     * @return RecipeDto của công thức đã được cập nhật.
     */
    RecipeDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto);

    /**
     * Xóa một công thức nấu ăn khỏi hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với recipeId.
     * 2. Kiểm tra sự tồn tại của công thức.
     * 3. Xóa công thức khỏi cơ sở dữ liệu.
     * @param recipeId ID của công thức cần xóa.
     */
    void deleteRecipe(Long recipeId);

    /**
     * Lấy tất cả các mục tiêu dinh dưỡng trong hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin để xem danh sách mục tiêu.
     * 2. Truy vấn cơ sở dữ liệu để lấy tất cả các bản ghi mục tiêu.
     * 3. Chuyển đổi các thực thể mục tiêu thành GoalDto để trả về.
     * @return Danh sách các GoalDto.
     */
    List<GoalDto> getAllGoals();

    /**
     * Lấy thông tin một mục tiêu dinh dưỡng cụ thể bằng ID.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với goalId.
     * 2. Truy vấn cơ sở dữ liệu để tìm mục tiêu.
     * 3. Nếu tìm thấy, chuyển đổi thực thể mục tiêu thành GoalDto.
     * 4. Trả về GoalDto hoặc ném ngoại lệ nếu không tìm thấy.
     * @param goalId ID của mục tiêu.
     * @return GoalDto chứa thông tin mục tiêu.
     */
    GoalDto getGoalById(Long goalId);

    /**
     * Tạo một mục tiêu dinh dưỡng mới.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với GoalDto.
     * 2. Chuyển đổi GoalDto thành thực thể Goal.
     * 3. Lưu thực thể Goal mới vào cơ sở dữ liệu.
     * 4. Chuyển đổi thực thể đã lưu thành GoalDto và trả về.
     * @param goalDto DTO chứa thông tin mục tiêu mới.
     * @return GoalDto của mục tiêu đã được tạo.
     */
    GoalDto createGoal(GoalDto goalDto);

    /**
     * Cập nhật thông tin của một mục tiêu dinh dưỡng.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với goalId và GoalDto.
     * 2. Tìm mục tiêu trong cơ sở dữ liệu bằng goalId.
     * 3. Cập nhật các trường thông tin của mục tiêu với dữ liệu từ DTO.
     * 4. Lưu mục tiêu đã cập nhật vào cơ sở dữ liệu.
     * 5. Chuyển đổi mục tiêu đã cập nhật thành GoalDto và trả về.
     * @param goalId ID của mục tiêu cần cập nhật.
     * @param goalDto DTO chứa thông tin cập nhật.
     * @return GoalDto của mục tiêu đã được cập nhật.
     */
    GoalDto updateGoal(Long goalId, GoalDto goalDto);

    /**
     * Xóa một mục tiêu dinh dưỡng khỏi hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với goalId.
     * 2. Kiểm tra sự tồn tại của mục tiêu.
     * 3. Xóa mục tiêu khỏi cơ sở dữ liệu.
     * @param goalId ID của mục tiêu cần xóa.
     */
    void deleteGoal(Long goalId);

    /**
     * Lấy tất cả các kế hoạch dinh dưỡng trong hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin để xem danh sách kế hoạch dinh dưỡng.
     * 2. Truy vấn cơ sở dữ liệu để lấy tất cả các bản ghi kế hoạch dinh dưỡng.
     * 3. Chuyển đổi các thực thể kế hoạch dinh dưỡng thành NutritionPlanDto để trả về.
     * @return Danh sách các NutritionPlanDto.
     */
    List<NutritionPlanDto> getAllNutritionPlans();

    /**
     * Lấy thông tin một kế hoạch dinh dưỡng cụ thể bằng ID.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với planId.
     * 2. Truy vấn cơ sở dữ liệu để tìm kế hoạch dinh dưỡng.
     * 3. Nếu tìm thấy, chuyển đổi thực thể kế hoạch dinh dưỡng thành NutritionPlanDto.
     * 4. Trả về NutritionPlanDto hoặc ném ngoại lệ nếu không tìm thấy.
     * @param planId ID của kế hoạch dinh dưỡng.
     * @return NutritionPlanDto chứa thông tin kế hoạch dinh dưỡng.
     */
    NutritionPlanDto getNutritionPlanById(Long planId);

    /**
     * Tạo một kế hoạch dinh dưỡng mới.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với NutritionPlanDto.
     * 2. Chuyển đổi NutritionPlanDto thành thực thể NutritionPlan.
     * 3. Lưu thực thể NutritionPlan mới vào cơ sở dữ liệu.
     * 4. Chuyển đổi thực thể đã lưu thành NutritionPlanDto và trả về.
     * @param nutritionPlanDto DTO chứa thông tin kế hoạch dinh dưỡng mới.
     * @return NutritionPlanDto của kế hoạch dinh dưỡng đã được tạo.
     */
    NutritionPlanDto createNutritionPlan(NutritionPlanDto nutritionPlanDto);

    /**
     * Cập nhật thông tin của một kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với planId và NutritionPlanDto.
     * 2. Tìm kế hoạch dinh dưỡng trong cơ sở dữ liệu bằng planId.
     * 3. Cập nhật các trường thông tin của kế hoạch dinh dưỡng với dữ liệu từ DTO.
     * 4. Lưu kế hoạch dinh dưỡng đã cập nhật vào cơ sở dữ liệu.
     * 5. Chuyển đổi kế hoạch dinh dưỡng đã cập nhật thành NutritionPlanDto và trả về.
     * @param planId ID của kế hoạch dinh dưỡng cần cập nhật.
     * @param nutritionPlanDto DTO chứa thông tin cập nhật.
     * @return NutritionPlanDto của kế hoạch dinh dưỡng đã được cập nhật.
     */
    NutritionPlanDto updateNutritionPlan(Long planId, NutritionPlanDto nutritionPlanDto);

    /**
     * Xóa một kế hoạch dinh dưỡng khỏi hệ thống.
     * Luồng hoạt động:
     * 1. Nhận yêu cầu từ Admin với planId.
     * 2. Kiểm tra sự tồn tại của kế hoạch dinh dưỡng.
     * 3. Xóa kế hoạch dinh dưỡng khỏi cơ sở dữ liệu.
     * @param planId ID của kế hoạch dinh dưỡng cần xóa.
     */
    void deleteNutritionPlan(Long planId);
}