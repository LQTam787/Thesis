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

import com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * AdminController xử lý các yêu cầu dành cho quản trị viên đối với nhiều tài nguyên hệ thống như người dùng, công thức, mục tiêu và kế hoạch dinh dưỡng.
 * <p>
 * Nguyên lý hoạt động: Tất cả các endpoint trong controller này được bảo vệ bằng chú thích {@code @PreAuthorize("hasRole('ADMIN')")},
 * đảm bảo rằng chỉ những người dùng có vai trò ADMIN mới có quyền truy cập. Controller này phụ thuộc vào {@link com.nutrition.ai.nutritionaibackend.service.AdminService}
 * để thực hiện các hoạt động CRUD (Tạo, Đọc, Cập nhật, Xóa) tương ứng.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Controller nhận các yêu cầu HTTP (GET, POST, PUT, DELETE) từ client.</li>
 *     <li>Xác thực vai trò ADMIN được thực hiện tự động bởi Spring Security thông qua {@code @PreAuthorize}.</li>
 *     <li>Controller ủy quyền các yêu cầu này đến {@code AdminService} để xử lý logic nghiệp vụ và tương tác với cơ sở dữ liệu.</li>
 *     <li>Các phản hồi từ {@code AdminService} được chuyển đổi thành các DTO (Data Transfer Object) phù hợp và trả về cho client.</li>
 *     <li>Xử lý ngoại lệ {@link com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException} được triển khai để trả về trạng thái HTTP 404 khi một tài nguyên không tìm thấy.</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/admin") // Ánh xạ với đường dẫn cơ sở /api/admin
@Tag(name = "Admin Management", description = "Endpoints for administrative tasks") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
@RequiredArgsConstructor // Tạo constructor cho AdminService
@PreAuthorize("hasRole('ADMIN')") // **QUAN TRỌNG:** Chỉ cho phép người dùng có vai trò ADMIN truy cập controller này
public class AdminController {

    private final AdminService adminService; // Dependency Injection cho Service

    // --- User Management Endpoints ---

    /**
     * Lấy danh sách tất cả người dùng trong hệ thống.
     * <p>
     * Endpoint này yêu cầu quyền truy cập của quản trị viên và sẽ trả về một danh sách
     * {@link UserResponseDto} đại diện cho tất cả người dùng.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/admin/users}.</li>
     *     <li>Gọi phương thức {@code adminService.getAllUsers()} để lấy dữ liệu người dùng.</li>
     *     <li>Trả về danh sách {@link UserResponseDto} với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @return ResponseEntity chứa danh sách {@link UserResponseDto} của tất cả người dùng.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all users. Admin access required.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Lấy thông tin chi tiết của một người dùng cụ thể dựa trên ID.
     * <p>
     * Endpoint này yêu cầu quyền truy cập của quản trị viên và sẽ trả về một
     * {@link UserResponseDto} cho người dùng được chỉ định.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/admin/users/{id}}.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code adminService.getUserById(userId)}.</li>
     *     <li>Trả về {@link UserResponseDto} của người dùng với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng cần lấy thông tin.
     * @return ResponseEntity chứa {@link UserResponseDto} của người dùng.
     * @throws ResourceNotFoundException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their ID. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    /**
     * Cập nhật thông tin chi tiết của một người dùng hiện có.
     * <p>
     * Endpoint này yêu cầu quyền truy cập của quản trị viên và cho phép cập nhật thông tin
     * của một người dùng dựa trên ID và dữ liệu được cung cấp trong {@link UpdateUserRequestDto}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu PUT đến {@code /api/admin/users/{id}} với dữ liệu {@code UpdateUserRequestDto} trong body.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn và {@code updateUserRequestDto} từ body.</li>
     *     <li>Gọi phương thức {@code adminService.updateUser(userId, updateUserRequestDto)}.</li>
     *     <li>Trả về {@link UserResponseDto} đã cập nhật với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng cần cập nhật.
     * @param updateUserRequestDto DTO chứa thông tin cập nhật cho người dùng.
     * @return ResponseEntity chứa {@link UserResponseDto} của người dùng đã cập nhật.
     * @throws ResourceNotFoundException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Operation(summary = "Update a user", description = "Updates a user's details. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(adminService.updateUser(userId, updateUserRequestDto));
    }

    /**
     * Xóa một người dùng khỏi hệ thống dựa trên ID.
     * <p>
     * Endpoint này yêu cầu quyền truy cập của quản trị viên và sẽ xóa người dùng
     * được chỉ định bởi {@code userId}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu DELETE đến {@code /api/admin/users/{id}}.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code adminService.deleteUser(userId)}.</li>
     *     <li>Trả về thông báo thành công với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng cần xóa.
     * @return ResponseEntity chứa thông báo xác nhận xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Operation(summary = "Delete a user", description = "Deletes a user from the system. Admin access required.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // --- Recipe Management Endpoints --- (Luồng tương tự cho Công thức)

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

    // --- Goal Management Endpoints --- (Luồng tương tự cho Mục tiêu)

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

    // --- Nutrition Plan Management Endpoints --- (Luồng tương tự cho Kế hoạch Dinh dưỡng)

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

    // Exception Handler for ResourceNotFoundException
    /**
     * Xử lý ngoại lệ {@link ResourceNotFoundException} được ném ra khi một tài nguyên không được tìm thấy.
     * <p>
     * Phương thức này sẽ trả về một phản hồi HTTP với trạng thái 404 NOT FOUND
     * và thông báo lỗi từ ngoại lệ.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Khi một {@code ResourceNotFoundException} được ném ra trong bất kỳ phương thức nào của controller.</li>
     *     <li>Phương thức này sẽ chặn ngoại lệ đó.</li>
     *     <li>Tạo một {@code ResponseEntity} với thông báo lỗi và trạng thái HTTP 404.</li>
     *     <li>Trả về phản hồi lỗi cho client.</li>
     * </ol>
     * </p>
     * @param ex Ngoại lệ {@link ResourceNotFoundException} đã bị ném.
     * @return ResponseEntity chứa thông báo lỗi và trạng thái HTTP 404 NOT FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}