package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutritionPlanServiceImplTest {

    @Mock
    private NutritionPlanRepository nutritionPlanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NutritionPlanServiceImpl nutritionPlanService;

    private User user;
    private NutritionPlan nutritionPlan;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        nutritionPlan = new NutritionPlan();
        nutritionPlan.setId(1L);
        nutritionPlan.setPlanName("Plan 1");
        nutritionPlan.setUser(user);
    }

    @Test
    void save_shouldSaveNutritionPlan_whenUserExists() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(nutritionPlan);

        // Act
        NutritionPlan savedNutritionPlan = nutritionPlanService.save(nutritionPlan);

        // Assert
        assertNotNull(savedNutritionPlan);
        assertEquals(nutritionPlan.getId(), savedNutritionPlan.getId());
        assertEquals(nutritionPlan.getPlanName(), savedNutritionPlan.getPlanName());
        verify(userRepository, times(1)).findById(user.getId());
        verify(nutritionPlanRepository, times(1)).save(nutritionPlan);
    }

    @Test
    void save_shouldThrowRuntimeException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> nutritionPlanService.save(nutritionPlan));
        assertEquals("User not found with ID: " + user.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    @Test
    void findAll_shouldReturnAllNutritionPlans() {
        // Arrange
        List<NutritionPlan> nutritionPlans = Arrays.asList(nutritionPlan, new NutritionPlan());
        when(nutritionPlanRepository.findAll()).thenReturn(nutritionPlans);

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(nutritionPlanRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoNutritionPlansExist() {
        // Arrange
        when(nutritionPlanRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nutritionPlanRepository, times(1)).findAll();
    }

    @Test
    void findOne_shouldReturnNutritionPlan_whenFound() {
        // Arrange
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.of(nutritionPlan));

        // Act
        Optional<NutritionPlan> result = nutritionPlanService.findOne(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(nutritionPlan.getId(), result.get().getId());
        verify(nutritionPlanRepository, times(1)).findById(1L);
    }

    @Test
    void findOne_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<NutritionPlan> result = nutritionPlanService.findOne(2L);

        // Assert
        assertFalse(result.isPresent());
        verify(nutritionPlanRepository, times(1)).findById(2L);
    }

    @Test
    void delete_shouldDeleteNutritionPlan() {
        // Act
        nutritionPlanService.delete(1L);

        // Assert
        verify(nutritionPlanRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAllByUser_shouldReturnNutritionPlans_whenUserHasPlans() {
        // Arrange
        List<NutritionPlan> userNutritionPlans = Arrays.asList(nutritionPlan);
        when(nutritionPlanRepository.findByUser(any(User.class))).thenReturn(userNutritionPlans);

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUser().getId());
        verify(nutritionPlanRepository, times(1)).findByUser(user);
    }

    @Test
    void findAllByUser_shouldReturnEmptyList_whenUserHasNoPlans() {
        // Arrange
        when(nutritionPlanRepository.findByUser(any(User.class))).thenReturn(Arrays.asList());

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nutritionPlanRepository, times(1)).findByUser(user);
    }
}
