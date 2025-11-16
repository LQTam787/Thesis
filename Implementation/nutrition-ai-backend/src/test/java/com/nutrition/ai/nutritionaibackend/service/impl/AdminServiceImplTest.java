package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.*;
import com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException;
import com.nutrition.ai.nutritionaibackend.model.domain.*;
import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import com.nutrition.ai.nutritionaibackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Admin Service Impl Unit Tests")
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private NutritionPlanRepository nutritionPlanRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private UserResponseDto userResponseDto;
    private Role adminRole;
    private Role userRole;
    private Recipe recipe;
    private RecipeDto recipeDto;
    private Goal goal;
    private GoalDto goalDto;
    private NutritionPlan nutritionPlan;
    private NutritionPlanDto nutritionPlanDto;

    @BeforeEach
    void setUp() {
        adminRole = new Role(ERole.ROLE_ADMIN);
        userRole = new Role(ERole.ROLE_USER);

        user = new User(1L, "testuser", "test@example.com", "password", new HashSet<>(Arrays.asList(userRole)));
        userResponseDto = new UserResponseDto(1L, "testuser", "test@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));

        FoodItem setupFoodItem = new FoodItem();
        setupFoodItem.setId(1L);
        setupFoodItem.setName("Apple");
        setupFoodItem.setCalories(52.0);
        setupFoodItem.setProtein(0.3);
        setupFoodItem.setCarbs(14.0);
        setupFoodItem.setFats(0.2);
        setupFoodItem.setServingSize("1 medium");

        recipe = new Recipe(1L, setupFoodItem, "Bake it", 60);
        recipeDto = new RecipeDto(1L, "Bake it", 60, 1L);

        goal = new Goal(1L, 70.0, LocalDate.now().plusMonths(3), EGoalType.WEIGHT_LOSS, "ACTIVE", user, "Maintain good health");
        goalDto = new GoalDto(1L, 70.0, LocalDate.now().plusMonths(3), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Maintain good health");

        nutritionPlan = new NutritionPlan(1L, "Weight Loss Plan", LocalDate.now(), LocalDate.now().plusMonths(1), "Reduce calories", user, new java.util.ArrayList<>());
        nutritionPlanDto = new NutritionPlanDto(1L, "Weight Loss Plan", LocalDate.now(), LocalDate.now().plusMonths(1), "Reduce calories", 1L, new java.util.ArrayList<>());
    }

    @Test
    @DisplayName("Test getAllUsers - Success")
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        List<UserResponseDto> result = adminService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponseDto, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }

    @Test
    @DisplayName("Test getUserById - Success")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }

    @Test
    @DisplayName("Test getUserById - Not Found")
    void getUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getUserById(99L));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test updateUser - Success")
    void updateUser_Success() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_ADMIN")));
        User updatedUserEntity = new User(1L, "updateduser", "updated@example.com", "password", new HashSet<>(Arrays.asList(adminRole)));
        UserResponseDto updatedUserResponseDto = new UserResponseDto(1L, "updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_ADMIN")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserEntity);
        when(modelMapper.map(updatedUserEntity, UserResponseDto.class)).thenReturn(updatedUserResponseDto);

        UserResponseDto result = adminService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedUserResponseDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName(ERole.ROLE_ADMIN);
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(updatedUserEntity, UserResponseDto.class);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("updateduser", capturedUser.getUsername());
        assertEquals("updated@example.com", capturedUser.getEmail());
        assertTrue(capturedUser.getRoles().contains(adminRole));
    }

    @Test
    @DisplayName("Test updateUser - User Not Found")
    void updateUser_UserNotFound() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateUser(99L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any(User.class));
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test updateUser - Role Not Found")
    void updateUser_RoleNotFound() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateUser(1L, updateRequest));

        assertEquals("Role not found with name : 'ROLE_USER'", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(userRepository, never()).save(any(User.class));
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test deleteUser - Success")
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        adminService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Test deleteUser - Not Found")
    void deleteUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteUser(99L));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Test getAllRecipes - Success")
    void getAllRecipes_Success() {
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe));
        when(modelMapper.map(recipe, RecipeDto.class)).thenReturn(recipeDto);

        List<RecipeDto> result = adminService.getAllRecipes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipeDto, result.get(0));
        verify(recipeRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(recipe, RecipeDto.class);
    }

    @Test
    @DisplayName("Test getRecipeById - Success")
    void getRecipeById_Success() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(modelMapper.map(recipe, RecipeDto.class)).thenReturn(recipeDto);

        RecipeDto result = adminService.getRecipeById(1L);

        assertNotNull(result);
        assertEquals(recipeDto, result);
        verify(recipeRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(recipe, RecipeDto.class);
    }

    @Test
    @DisplayName("Test getRecipeById - Not Found")
    void getRecipeById_NotFound() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getRecipeById(99L));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test createRecipe - Success")
    void createRecipe_Success() {
        RecipeRequestDto createRequest = new RecipeRequestDto("New Recipe", 100.0, 10.0, 20.0, 5.0, "1 serving", "New instructions", 30);
        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setName("New Recipe");
        newFoodItem.setCalories(100.0);
        newFoodItem.setProtein(10.0);
        newFoodItem.setCarbs(20.0);
        newFoodItem.setFats(5.0);
        newFoodItem.setServingSize("1 serving");

        Recipe savedRecipe = new Recipe(2L, newFoodItem, "New instructions", 30);
        RecipeDto savedRecipeDto = new RecipeDto(2L, "New instructions", 30, 2L);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
        when(modelMapper.map(any(Recipe.class), eq(RecipeDto.class))).thenReturn(savedRecipeDto);

        RecipeDto result = adminService.createRecipe(createRequest);

        assertNotNull(result);
        assertEquals(savedRecipeDto, result);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(modelMapper, times(1)).map(any(Recipe.class), eq(RecipeDto.class));

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();
        assertEquals("New Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals("New instructions", capturedRecipe.getInstructions());
        assertEquals(30, capturedRecipe.getPreparationTime());
        assertNotNull(capturedRecipe.getFoodItem());
        assertEquals("New Recipe", capturedRecipe.getFoodItem().getName());
    }

    @Test
    @DisplayName("Test updateRecipe - Success")
    void updateRecipe_Success() {
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Recipe", 150.0, 15.0, 25.0, 10.0, "2 servings", "Updated instructions", 45);
        FoodItem originalFoodItem = recipe.getFoodItem();
        Recipe updatedRecipeEntity = new Recipe(1L, originalFoodItem, "Updated instructions", 45);
        RecipeDto updatedRecipeDto = new RecipeDto(1L, "Updated instructions", 45, originalFoodItem.getId());

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updatedRecipeEntity);
        when(modelMapper.map(updatedRecipeEntity, RecipeDto.class)).thenReturn(updatedRecipeDto);

        RecipeDto result = adminService.updateRecipe(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedRecipeDto, result);
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(modelMapper, times(1)).map(updatedRecipeEntity, RecipeDto.class);

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();
        assertEquals("Updated Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals("Updated instructions", capturedRecipe.getInstructions());
        assertEquals(45, capturedRecipe.getPreparationTime());
        assertNotNull(capturedRecipe.getFoodItem());
        assertEquals("Updated Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals(150.0, capturedRecipe.getFoodItem().getCalories());
    }

    @Test
    @DisplayName("Test updateRecipe - Not Found")
    void updateRecipe_NotFound() {
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Recipe", 150.0, 15.0, 25.0, 10.0, "2 servings", "Updated instructions", 45);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateRecipe(99L, updateRequest));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).findById(99L);
        verify(recipeRepository, never()).save(any(Recipe.class));
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test deleteRecipe - Success")
    void deleteRecipe_Success() {
        when(recipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(1L);

        adminService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).existsById(1L);
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteRecipe - Not Found")
    void deleteRecipe_NotFound() {
        when(recipeRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteRecipe(99L));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).existsById(99L);
        verify(recipeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test getAllGoals - Success")
    void getAllGoals_Success() {
        when(goalRepository.findAll()).thenReturn(Arrays.asList(goal));
        when(modelMapper.map(goal, GoalDto.class)).thenReturn(goalDto);

        List<GoalDto> result = adminService.getAllGoals();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(goalDto, result.get(0));
        verify(goalRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(goal, GoalDto.class);
    }

    @Test
    @DisplayName("Test getGoalById - Success")
    void getGoalById_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(modelMapper.map(goal, GoalDto.class)).thenReturn(goalDto);

        GoalDto result = adminService.getGoalById(1L);

        assertNotNull(result);
        assertEquals(goalDto, result);
        verify(goalRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(goal, GoalDto.class);
    }

    @Test
    @DisplayName("Test getGoalById - Not Found")
    void getGoalById_NotFound() {
        when(goalRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getGoalById(99L));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test createGoal - Success")
    void createGoal_Success() {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 1L, "Gain more health");
        Goal newGoal = new Goal(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", user, "Gain more health");
        Goal savedGoal = new Goal(2L, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", user, "Gain more health");
        GoalDto savedGoalDto = new GoalDto(2L, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 1L, "Gain more health");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(createRequest, Goal.class)).thenReturn(newGoal);
        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);
        when(modelMapper.map(savedGoal, GoalDto.class)).thenReturn(savedGoalDto);

        GoalDto result = adminService.createGoal(createRequest);

        assertNotNull(result);
        assertEquals(savedGoalDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(createRequest, Goal.class);
        verify(goalRepository, times(1)).save(any(Goal.class));
        verify(modelMapper, times(1)).map(savedGoal, GoalDto.class);

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(goalCaptor.capture());
        Goal capturedGoal = goalCaptor.getValue();
        assertEquals(65.0, capturedGoal.getTargetWeight());
        assertEquals(EGoalType.WEIGHT_GAIN, capturedGoal.getGoalType());
        assertEquals("ACTIVE", capturedGoal.getStatus());
        assertEquals(user, capturedGoal.getUser());
    }

    @Test
    @DisplayName("Test createGoal - User Not Found")
    void createGoal_UserNotFound() {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 99L, "Gain more health");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.createGoal(createRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Test updateGoal - Success")
    void updateGoal_Success() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");
        Goal updatedGoalEntity = new Goal(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", user, "Maintain good health");
        GoalDto updatedGoalDto = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.save(any(Goal.class))).thenReturn(updatedGoalEntity);
        when(modelMapper.map(updatedGoalEntity, GoalDto.class)).thenReturn(updatedGoalDto);

        GoalDto result = adminService.updateGoal(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedGoalDto, result);
        verify(goalRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(goalRepository, times(1)).save(any(Goal.class));
        verify(modelMapper, times(1)).map(updatedGoalEntity, GoalDto.class);

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(goalCaptor.capture());
        Goal capturedGoal = goalCaptor.getValue();
        assertEquals(75.0, capturedGoal.getTargetWeight());
        assertEquals(EGoalType.MAINTAIN_WEIGHT, capturedGoal.getGoalType());
        assertEquals("COMPLETED", capturedGoal.getStatus());
        assertEquals(user, capturedGoal.getUser());
    }

    @Test
    @DisplayName("Test updateGoal - Goal Not Found")
    void updateGoal_GoalNotFound() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");
        when(goalRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateGoal(99L, updateRequest));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(99L);
        verify(userRepository, never()).findById(anyLong());
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Test updateGoal - User Not Found")
    void updateGoal_UserNotFound() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 99L, "Maintain good health");
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateGoal(1L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(99L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    @DisplayName("Test deleteGoal - Success")
    void deleteGoal_Success() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(goalRepository).deleteById(1L);

        adminService.deleteGoal(1L);

        verify(goalRepository, times(1)).existsById(1L);
        verify(goalRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteGoal - Not Found")
    void deleteGoal_NotFound() {
        when(goalRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteGoal(99L));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).existsById(99L);
        verify(goalRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test getAllNutritionPlans - Success")
    void getAllNutritionPlans_Success() {
        when(nutritionPlanRepository.findAll()).thenReturn(Arrays.asList(nutritionPlan));
        when(modelMapper.map(nutritionPlan, NutritionPlanDto.class)).thenReturn(nutritionPlanDto);

        List<NutritionPlanDto> result = adminService.getAllNutritionPlans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(nutritionPlanDto, result.get(0));
        verify(nutritionPlanRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(nutritionPlan, NutritionPlanDto.class);
    }

    @Test
    @DisplayName("Test getNutritionPlanById - Success")
    void getNutritionPlanById_Success() {
        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(modelMapper.map(nutritionPlan, NutritionPlanDto.class)).thenReturn(nutritionPlanDto);

        NutritionPlanDto result = adminService.getNutritionPlanById(1L);

        assertNotNull(result);
        assertEquals(nutritionPlanDto, result);
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(nutritionPlan, NutritionPlanDto.class);
    }

    @Test
    @DisplayName("Test getNutritionPlanById - Not Found")
    void getNutritionPlanById_NotFound() {
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getNutritionPlanById(99L));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Test createNutritionPlan - Success")
    void createNutritionPlan_Success() {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 1L, new java.util.ArrayList<>());
        NutritionPlan newPlan = new NutritionPlan(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", user, new java.util.ArrayList<>());
        NutritionPlan savedPlan = new NutritionPlan(2L, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", user, new java.util.ArrayList<>());
        NutritionPlanDto savedPlanDto = new NutritionPlanDto(2L, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 1L, new java.util.ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(createRequest, NutritionPlan.class)).thenReturn(newPlan);
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(savedPlan);
        when(modelMapper.map(savedPlan, NutritionPlanDto.class)).thenReturn(savedPlanDto);

        NutritionPlanDto result = adminService.createNutritionPlan(createRequest);

        assertNotNull(result);
        assertEquals(savedPlanDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(createRequest, NutritionPlan.class);
        verify(nutritionPlanRepository, times(1)).save(any(NutritionPlan.class));
        verify(modelMapper, times(1)).map(savedPlan, NutritionPlanDto.class);

        ArgumentCaptor<NutritionPlan> planCaptor = ArgumentCaptor.forClass(NutritionPlan.class);
        verify(nutritionPlanRepository).save(planCaptor.capture());
        NutritionPlan capturedPlan = planCaptor.getValue();
        assertEquals("Maintenance Plan", capturedPlan.getPlanName());
        assertEquals(user, capturedPlan.getUser());
    }

    @Test
    @DisplayName("Test createNutritionPlan - User Not Found")
    void createNutritionPlan_UserNotFound() {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 99L, new java.util.ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.createNutritionPlan(createRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    @Test
    @DisplayName("Test updateNutritionPlan - Success")
    void updateNutritionPlan_Success() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());
        NutritionPlan updatedPlanEntity = new NutritionPlan(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", user, new java.util.ArrayList<>());
        NutritionPlanDto updatedPlanDto = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());

        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(updatedPlanEntity);
        when(modelMapper.map(updatedPlanEntity, NutritionPlanDto.class)).thenReturn(updatedPlanDto);

        NutritionPlanDto result = adminService.updateNutritionPlan(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedPlanDto, result);
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(nutritionPlanRepository, times(1)).save(any(NutritionPlan.class));
        verify(modelMapper, times(1)).map(updatedPlanEntity, NutritionPlanDto.class);

        ArgumentCaptor<NutritionPlan> planCaptor = ArgumentCaptor.forClass(NutritionPlan.class);
        verify(nutritionPlanRepository).save(planCaptor.capture());
        NutritionPlan capturedPlan = planCaptor.getValue();
        assertEquals("Bulk Up Plan", capturedPlan.getPlanName());
        assertEquals("Gain muscle", capturedPlan.getNutritionGoal());
        assertEquals(user, capturedPlan.getUser());
    }

    @Test
    @DisplayName("Test updateNutritionPlan - NutritionPlan Not Found")
    void updateNutritionPlan_NutritionPlanNotFound() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateNutritionPlan(99L, updateRequest));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(99L);
        verify(userRepository, never()).findById(anyLong());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    @Test
    @DisplayName("Test updateNutritionPlan - User Not Found")
    void updateNutritionPlan_UserNotFound() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 99L, new java.util.ArrayList<>());
        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateNutritionPlan(1L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(99L);
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    @Test
    @DisplayName("Test deleteNutritionPlan - Success")
    void deleteNutritionPlan_Success() {
        when(nutritionPlanRepository.existsById(1L)).thenReturn(true);
        doNothing().when(nutritionPlanRepository).deleteById(1L);

        adminService.deleteNutritionPlan(1L);

        verify(nutritionPlanRepository, times(1)).existsById(1L);
        verify(nutritionPlanRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteNutritionPlan - Not Found")
    void deleteNutritionPlan_NotFound() {
        when(nutritionPlanRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteNutritionPlan(99L));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).existsById(99L);
        verify(nutritionPlanRepository, never()).deleteById(anyLong());
    }
}
