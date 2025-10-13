package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.GoalDto;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeDto;
import com.nutrition.ai.nutritionaibackend.dto.RecipeRequestDto;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    // User Management Tests
    @Test
    void testGetAllUsers_Success() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "user1", "user1@example.com", null);
        UserResponseDto user2 = new UserResponseDto(2L, "user2", "user2@example.com", null);
        List<UserResponseDto> users = Arrays.asList(user1, user2);

        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        Long userId = 1L;
        UserResponseDto user = new UserResponseDto(userId, "testuser", "test@example.com", null);

        when(adminService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Long userId = 1L;
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updatedUser", "updated@example.com", null);
        UserResponseDto updatedUser = new UserResponseDto(userId, "updatedUser", "updated@example.com", null);

        when(adminService.updateUser(eq(userId), any(UpdateUserRequestDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Long userId = 1L;
        doNothing().when(adminService).deleteUser(userId);

        mockMvc.perform(delete("/api/admin/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully!"));
    }

    // Recipe Management Tests
    @Test
    void testGetAllRecipes_Success() throws Exception {
        RecipeDto recipe1 = new RecipeDto(1L, "Instr 1", 2, 101L);
        RecipeDto recipe2 = new RecipeDto(2L, "Instr 2", 4, 102L);
        List<RecipeDto> recipes = Arrays.asList(recipe1, recipe2);

        when(adminService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/api/admin/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].instructions").value("Instr 1"));
    }

    @Test
    void testGetRecipeById_Success() throws Exception {
        Long recipeId = 1L;
        RecipeDto recipe = new RecipeDto(recipeId, "Instr 1", 2, 101L);

        when(adminService.getRecipeById(recipeId)).thenReturn(recipe);

        mockMvc.perform(get("/api/admin/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("Instr 1"));
    }

    @Test
    void testCreateRecipe_Success() throws Exception {
        RecipeRequestDto createRequest = new RecipeRequestDto("New Recipe", 200.0, 10.0, 30.0, 5.0, "1 serving", "New Instr", 3);
        RecipeDto createdRecipe = new RecipeDto(1L, "New Instr", 3, 101L);

        when(adminService.createRecipe(any(RecipeRequestDto.class))).thenReturn(createdRecipe);

        mockMvc.perform(post("/api/admin/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("New Instr"));
    }

    @Test
    void testUpdateRecipe_Success() throws Exception {
        Long recipeId = 1L;
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Recipe", 250.0, 12.0, 35.0, 7.0, "1 serving", "Updated Instr", 4);
        RecipeDto updatedRecipe = new RecipeDto(recipeId, "Updated Instr", 4, 101L);

        when(adminService.updateRecipe(eq(recipeId), any(RecipeRequestDto.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/admin/recipes/{id}", recipeId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructions").value("Updated Instr"));
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

    // Goal Management Tests
    @Test
    void testGetAllGoals_Success() throws Exception {
        GoalDto goal1 = new GoalDto(1L, 60.0, LocalDate.now().plusMonths(1), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "In Progress", 1L);
        GoalDto goal2 = new GoalDto(2L, 70.0, LocalDate.now().plusMonths(2), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_GAIN, "In Progress", 2L);
        List<GoalDto> goals = Arrays.asList(goal1, goal2);

        when(adminService.getAllGoals()).thenReturn(goals);

        mockMvc.perform(get("/api/admin/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].targetWeight").value(60.0));
    }

    @Test
    void testGetGoalById_Success() throws Exception {
        Long goalId = 1L;
        GoalDto goal = new GoalDto(goalId, 60.0, LocalDate.now().plusMonths(1), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "In Progress", 1L);

        when(adminService.getGoalById(goalId)).thenReturn(goal);

        mockMvc.perform(get("/api/admin/goals/{id}", goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetWeight").value(60.0));
    }

    @Test
    void testCreateGoal_Success() throws Exception {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.now().plusMonths(1), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "Not Started", 1L);
        GoalDto createdGoal = new GoalDto(1L, 65.0, LocalDate.now().plusMonths(1), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "Not Started", 1L);

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
        GoalDto updateRequest = new GoalDto(null, 62.0, LocalDate.now().plusMonths(2), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "In Progress", 1L);
        GoalDto updatedGoal = new GoalDto(goalId, 62.0, LocalDate.now().plusMonths(2), com.nutrition.ai.nutritionaibackend.model.enums.EGoalType.WEIGHT_LOSS, "In Progress", 1L);

        when(adminService.updateGoal(eq(goalId), any(GoalDto.class))).thenReturn(updatedGoal);

        mockMvc.perform(put("/api/admin/goals/{id}", goalId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetWeight").value(62.0));
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

    // Nutrition Plan Management Tests
    @Test
    void testGetAllNutritionPlans_Success() throws Exception {
        NutritionPlanDto plan1 = new NutritionPlanDto(1L, "Plan A", LocalDate.now(), LocalDate.now().plusDays(1), 2000, 1L, null);
        NutritionPlanDto plan2 = new NutritionPlanDto(2L, "Plan B", LocalDate.now(), LocalDate.now().plusDays(2), 2200, 2L, null);
        List<NutritionPlanDto> plans = Arrays.asList(plan1, plan2);

        when(adminService.getAllNutritionPlans()).thenReturn(plans);

        mockMvc.perform(get("/api/admin/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value("Plan A"));
    }

    @Test
    void testGetNutritionPlanById_Success() throws Exception {
        Long planId = 1L;
        NutritionPlanDto plan = new NutritionPlanDto(planId, "Plan A", LocalDate.now(), LocalDate.now().plusDays(1), 2000, 1L, null);

        when(adminService.getNutritionPlanById(planId)).thenReturn(plan);

        mockMvc.perform(get("/api/admin/plans/{id}", planId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Plan A"));
    }

    @Test
    void testCreateNutritionPlan_Success() throws Exception {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "New Plan", LocalDate.now(), LocalDate.now().plusMonths(1), 2100, 1L, null);
        NutritionPlanDto createdPlan = new NutritionPlanDto(1L, "New Plan", LocalDate.now(), LocalDate.now().plusMonths(1), 2100, 1L, null);

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
        NutritionPlanDto updateRequest = new NutritionPlanDto(null, "Updated Plan", LocalDate.now(), LocalDate.now().plusMonths(2), 2300, 1L, null);
        NutritionPlanDto updatedPlan = new NutritionPlanDto(planId, "Updated Plan", LocalDate.now(), LocalDate.now().plusMonths(2), 2300, 1L, null);

        when(adminService.updateNutritionPlan(eq(planId), any(NutritionPlanDto.class))).thenReturn(updatedPlan);

        mockMvc.perform(put("/api/admin/plans/{id}", planId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Updated Plan"));
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
}
