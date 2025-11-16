package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;
import com.nutrition.ai.nutritionaibackend.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import java.time.LocalDate;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import java.util.Collections;
import com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN") // Giả lập người dùng có vai trò ADMIN để kiểm tra bảo mật (authorization)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService; // Service giả lập cho các thao tác quản trị

    @Autowired
    private ObjectMapper objectMapper;

    // --- User Management Tests ---
    @Test
    void testGetAllUsers_Success() throws Exception {
        // 1. Chuẩn bị dữ liệu danh sách người dùng giả lập
        UserResponseDto user1 = new UserResponseDto(1L, "user1", "user1@example.com", null);
        UserResponseDto user2 = new UserResponseDto(2L, "user2", "user2@example.com", null);
        List<UserResponseDto> users = Arrays.asList(user1, user2);

        // 2. Mocking: Giả lập AdminService trả về danh sách này
        when(adminService.getAllUsers()).thenReturn(users);

        // 3. Thực hiện và kiểm tra yêu cầu GET /api/admin/users
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk()) // 4. Kiểm tra: Trạng thái 200 OK
                .andExpect(jsonPath("$[0].username").value("user1")) // Kiểm tra nội dung JSON của phần tử đầu tiên
                .andExpect(jsonPath("$[1].email").value("user2@example.com")); // Kiểm tra nội dung JSON của phần tử thứ hai
    }

    @Test
    void testGetUserById_Success() throws Exception {
        Long userId = 1L;
        UserResponseDto user = new UserResponseDto(userId, "testuser", "test@example.com", null);

        // Mocking: Giả lập AdminService tìm thấy người dùng
        when(adminService.getUserById(userId)).thenReturn(user);

        // Thực hiện yêu cầu GET /api/admin/users/{id}
        mockMvc.perform(get("/api/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser")); // Kiểm tra trường username trong phản hồi
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        Long userId = 99L; // ID của người dùng không tồn tại
        when(adminService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User", "id", userId)); // Giả lập không tìm thấy người dùng

        mockMvc.perform(get("/api/admin/users/{id}", userId))
                .andExpect(status().isNotFound()); // Kiểm tra: Trạng thái 404 Not Found
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Long userId = 1L;
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updatedUser", "updated@example.com", null);
        UserResponseDto updatedUser = new UserResponseDto(userId, "updatedUser", "updated@example.com", null);

        // Mocking: Giả lập AdminService cập nhật thành công
        when(adminService.updateUser(eq(userId), any(UpdateUserRequestDto.class))).thenReturn(updatedUser);

        // Thực hiện yêu cầu PUT /api/admin/users/{id}
        mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .with(csrf()) // Thêm token CSRF cho yêu cầu POST/PUT/DELETE
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        Long userId = 99L;
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("nonexistentUser", "nonexistent@example.com", null);

        when(adminService.updateUser(eq(userId), any(UpdateUserRequestDto.class))).thenThrow(new ResourceNotFoundException("User", "id", userId)); // Giả lập không tìm thấy người dùng

        mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Long userId = 1L;
        // Mocking: Giả lập Service thực hiện xóa thành công (không trả về gì)
        doNothing().when(adminService).deleteUser(userId);

        // Thực hiện yêu cầu DELETE /api/admin/users/{id}
        mockMvc.perform(delete("/api/admin/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully!")); // Kiểm tra thông báo thành công
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Long userId = 99L;
        doThrow(new ResourceNotFoundException("User", "id", userId)).when(adminService).deleteUser(userId);

        mockMvc.perform(delete("/api/admin/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Recipe Management Tests ---
    @Test
    void testGetAllRecipes_Success() throws Exception {
        RecipeDto recipe1 = new RecipeDto(1L, "Recipe 1 instructions", 30, 101L);
        RecipeDto recipe2 = new RecipeDto(2L, "Recipe 2 instructions", 45, 102L);
        List<RecipeDto> recipes = Arrays.asList(recipe1, recipe2);

        when(adminService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/api/admin/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].instructions").value("Recipe 1 instructions"))
                .andExpect(jsonPath("$[1].preparationTime").value(45));
    }

    @Test
    void testGetRecipeById_Success() throws Exception {
        Long recipeId = 1L;
        RecipeDto recipe = new RecipeDto(recipeId, "Test Recipe instructions", 25, 103L);

        when(adminService.getRecipeById(recipeId)).thenReturn(recipe);

        mockMvc.perform(get("/api/admin/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("Test Recipe instructions"));
    }

    @Test
    void testGetRecipeById_NotFound() throws Exception {
        Long recipeId = 99L;
        when(adminService.getRecipeById(recipeId)).thenThrow(new ResourceNotFoundException("Recipe", "id", recipeId));

        mockMvc.perform(get("/api/admin/recipes/{id}", recipeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRecipe_Success() throws Exception {
        RecipeRequestDto createRequest = new RecipeRequestDto("New Food Item", 500.0, 50.0, 60.0, 20.0, "1 serving", "New Recipe instructions", 20);
        RecipeDto createdRecipe = new RecipeDto(1L, "New Recipe instructions", 20, 104L);

        when(adminService.createRecipe(any(RecipeRequestDto.class))).thenReturn(createdRecipe);

        mockMvc.perform(post("/api/admin/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("New Recipe instructions"));
    }

    @Test
    void testUpdateRecipe_Success() throws Exception {
        Long recipeId = 1L;
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Food Item", 600.0, 60.0, 70.0, 25.0, "1 serving", "Updated Recipe instructions", 35);
        RecipeDto updatedRecipe = new RecipeDto(recipeId, "Updated Recipe instructions", 35, 105L);

        when(adminService.updateRecipe(eq(recipeId), any(RecipeRequestDto.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/admin/recipes/{id}", recipeId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("Updated Recipe instructions"));
    }

    @Test
    void testUpdateRecipe_NotFound() throws Exception {
        Long recipeId = 99L;
        RecipeRequestDto updateRequest = new RecipeRequestDto("NonExistent Food Item", 700.0, 70.0, 80.0, 30.0, "1 serving", "NonExistent Recipe instructions", 40);

        when(adminService.updateRecipe(eq(recipeId), any(RecipeRequestDto.class))).thenThrow(new ResourceNotFoundException("Recipe", "id", recipeId));

        mockMvc.perform(put("/api/admin/recipes/{id}", recipeId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRecipe_Success() throws Exception {
        Long recipeId = 1L;
        doNothing().when(adminService).deleteRecipe(recipeId);

        mockMvc.perform(delete("/api/admin/recipes/{id}", recipeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully!"));
    }

    @Test
    void testDeleteRecipe_NotFound() throws Exception {
        Long recipeId = 99L;
        doThrow(new ResourceNotFoundException("Recipe", "id", recipeId)).when(adminService).deleteRecipe(recipeId);

        mockMvc.perform(delete("/api/admin/recipes/{id}", recipeId)
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Vẫn trả về 200 OK theo logic hiện tại của controller
    }

    // --- Goal Management Tests ---
    @Test
    void testGetAllGoals_Success() throws Exception {
        GoalDto goal1 = new GoalDto(1L, 70.0, LocalDate.of(2026, 1, 1), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Lose 5kg by Jan 2026");
        GoalDto goal2 = new GoalDto(2L, 75.0, LocalDate.of(2026, 3, 1), EGoalType.MAINTAIN_WEIGHT, "ACTIVE", 2L, "Maintain current weight");
        List<GoalDto> goals = Arrays.asList(goal1, goal2);

        when(adminService.getAllGoals()).thenReturn(goals);

        mockMvc.perform(get("/api/admin/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].targetWeight").value(70.0))
                .andExpect(jsonPath("$[1].goalType").value("MAINTAIN_WEIGHT"));
    }

    @Test
    void testGetGoalById_Success() throws Exception {
        Long goalId = 1L;
        GoalDto goal = new GoalDto(goalId, 70.0, LocalDate.of(2026, 1, 1), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Lose 5kg by Jan 2026");

        when(adminService.getGoalById(goalId)).thenReturn(goal);

        mockMvc.perform(get("/api/admin/goals/{id}", goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetWeight").value(70.0));
    }

    @Test
    void testGetGoalById_NotFound() throws Exception {
        Long goalId = 99L;
        when(adminService.getGoalById(goalId)).thenThrow(new ResourceNotFoundException("Goal", "id", goalId));

        mockMvc.perform(get("/api/admin/goals/{id}", goalId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateGoal_Success() throws Exception {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.of(2026, 6, 1), EGoalType.WEIGHT_LOSS, "PENDING", 1L, "Reach 65kg by June 2026");
        GoalDto createdGoal = new GoalDto(3L, 65.0, LocalDate.of(2026, 6, 1), EGoalType.WEIGHT_LOSS, "PENDING", 1L, "Reach 65kg by June 2026");

        when(adminService.createGoal(any(GoalDto.class))).thenReturn(createdGoal);

        mockMvc.perform(post("/api/admin/goals")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetWeight").value(65.0));
    }

    @Test
    void testUpdateGoal_Success() throws Exception {
        Long goalId = 1L;
        GoalDto updateRequest = new GoalDto(null, 68.0, LocalDate.of(2026, 4, 1), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Adjusted goal");
        GoalDto updatedGoal = new GoalDto(goalId, 68.0, LocalDate.of(2026, 4, 1), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Adjusted goal");

        when(adminService.updateGoal(eq(goalId), any(GoalDto.class))).thenReturn(updatedGoal);

        mockMvc.perform(put("/api/admin/goals/{id}", goalId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetWeight").value(68.0));
    }

    @Test
    void testUpdateGoal_NotFound() throws Exception {
        Long goalId = 99L;
        GoalDto updateRequest = new GoalDto(null, 80.0, LocalDate.of(2026, 12, 31), EGoalType.WEIGHT_GAIN, "ACTIVE", 1L, "NonExistent Goal");

        when(adminService.updateGoal(eq(goalId), any(GoalDto.class))).thenThrow(new ResourceNotFoundException("Goal", "id", goalId));

        mockMvc.perform(put("/api/admin/goals/{id}", goalId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteGoal_Success() throws Exception {
        Long goalId = 1L;
        doNothing().when(adminService).deleteGoal(goalId);

        mockMvc.perform(delete("/api/admin/goals/{id}", goalId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Goal deleted successfully!"));
    }

    @Test
    void testDeleteGoal_NotFound() throws Exception {
        Long goalId = 99L;
        doThrow(new ResourceNotFoundException("Goal", "id", goalId)).when(adminService).deleteGoal(goalId);

        mockMvc.perform(delete("/api/admin/goals/{id}", goalId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Nutrition Plan Management Tests ---
    @Test
    void testGetAllNutritionPlans_Success() throws Exception {
        NutritionPlanDto plan1 = new NutritionPlanDto(1L, "Plan A", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31), "Weight Loss", 1L, Collections.emptyList());
        NutritionPlanDto plan2 = new NutritionPlanDto(2L, "Plan B", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28), "Muscle Gain", 2L, Collections.emptyList());
        List<NutritionPlanDto> plans = Arrays.asList(plan1, plan2);

        when(adminService.getAllNutritionPlans()).thenReturn(plans);

        mockMvc.perform(get("/api/admin/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value("Plan A"))
                .andExpect(jsonPath("$[1].nutritionGoal").value("Muscle Gain"));
    }

    @Test
    void testGetNutritionPlanById_Success() throws Exception {
        Long planId = 1L;
        NutritionPlanDto plan = new NutritionPlanDto(planId, "Test Plan", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 31), "Maintenance", 1L, Collections.emptyList());

        when(adminService.getNutritionPlanById(planId)).thenReturn(plan);

        mockMvc.perform(get("/api/admin/plans/{id}", planId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Test Plan"));
    }

    @Test
    void testGetNutritionPlanById_NotFound() throws Exception {
        Long planId = 99L;
        when(adminService.getNutritionPlanById(planId)).thenThrow(new ResourceNotFoundException("NutritionPlan", "id", planId));

        mockMvc.perform(get("/api/admin/plans/{id}", planId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNutritionPlan_Success() throws Exception {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "New Plan", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30), "Bulking", 1L, Collections.emptyList());
        NutritionPlanDto createdPlan = new NutritionPlanDto(3L, "New Plan", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30), "Bulking", 1L, Collections.emptyList());

        when(adminService.createNutritionPlan(any(NutritionPlanDto.class))).thenReturn(createdPlan);

        mockMvc.perform(post("/api/admin/plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("New Plan"));
    }

    @Test
    void testUpdateNutritionPlan_Success() throws Exception {
        Long planId = 1L;
        NutritionPlanDto updateRequest = new NutritionPlanDto(null, "Updated Plan", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 31), "Cutting", 1L, Collections.emptyList());
        NutritionPlanDto updatedPlan = new NutritionPlanDto(planId, "Updated Plan", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 31), "Cutting", 1L, Collections.emptyList());

        when(adminService.updateNutritionPlan(eq(planId), any(NutritionPlanDto.class))).thenReturn(updatedPlan);

        mockMvc.perform(put("/api/admin/plans/{id}", planId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Updated Plan"));
    }

    @Test
    void testUpdateNutritionPlan_NotFound() throws Exception {
        Long planId = 99L;
        NutritionPlanDto updateRequest = new NutritionPlanDto(null, "NonExistent Plan", LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30), "No Goal", 1L, Collections.emptyList());

        when(adminService.updateNutritionPlan(eq(planId), any(NutritionPlanDto.class))).thenThrow(new ResourceNotFoundException("NutritionPlan", "id", planId));

        mockMvc.perform(put("/api/admin/plans/{id}", planId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNutritionPlan_Success() throws Exception {
        Long planId = 1L;
        doNothing().when(adminService).deleteNutritionPlan(planId);

        mockMvc.perform(delete("/api/admin/plans/{id}", planId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Nutrition plan deleted successfully!"));
    }

    @Test
    void testDeleteNutritionPlan_NotFound() throws Exception {
        Long planId = 99L;
        doThrow(new ResourceNotFoundException("NutritionPlan", "id", planId)).when(adminService).deleteNutritionPlan(planId);

        mockMvc.perform(delete("/api/admin/plans/{id}", planId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- Các bài kiểm thử khác (Recipe, Goal, Nutrition Plan) tuân theo nguyên lý tương tự:
    // 1. Chuẩn bị DTO/Request.
    // 2. Giả lập hành vi của `adminService` (when/thenReturn hoặc doNothing).
    // 3. Thực hiện yêu cầu `mockMvc.perform` (GET, POST, PUT, DELETE).
    // 4. Kiểm tra trạng thái HTTP và nội dung phản hồi (`status().isOk()`, `jsonPath(...)`).
    // ...
}