package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.NutritionPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionPlanController.class)
@WithMockUser(username = "testuser")
class NutritionPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NutritionPlanService nutritionPlanService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private NutritionPlan plan1;
    private NutritionPlanDto planDto1;
    private NutritionPlan plan2;
    private NutritionPlanDto planDto2;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password", "test@example.com", Collections.emptySet());

        plan1 = new NutritionPlan();
        plan1.setId(1L);
        plan1.setPlanName("Plan A");
        plan1.setUser(testUser);
        plan1.setStartDate(LocalDate.now());
        plan1.setEndDate(LocalDate.now().plusDays(1));
        plan1.setTotalCaloriesGoal(2000);

        planDto1 = new NutritionPlanDto(1L, "Plan A", LocalDate.now(), LocalDate.now().plusDays(1), 2000, 1L, null);

        plan2 = new NutritionPlan();
        plan2.setId(2L);
        plan2.setPlanName("Plan B");
        plan2.setUser(testUser);
        plan2.setStartDate(LocalDate.now());
        plan2.setEndDate(LocalDate.now().plusDays(2));
        plan2.setTotalCaloriesGoal(2200);

        planDto2 = new NutritionPlanDto(2L, "Plan B", LocalDate.now(), LocalDate.now().plusDays(2), 2200, 1L, null);
    }

    @Test
    void testCreateNutritionPlan_Success() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(modelMapper.map(any(NutritionPlanDto.class), any())).thenReturn(plan1);
        when(nutritionPlanService.save(any(NutritionPlan.class))).thenReturn(plan1);
        when(modelMapper.map(any(NutritionPlan.class), any())).thenReturn(planDto1);

        mockMvc.perform(post("/api/users/testuser/nutrition-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Plan A"));
    }

    @Test
    void testCreateNutritionPlan_UserNotFound() throws Exception {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/nonexistentuser/nutrition-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planDto1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetNutritionPlan_Found() throws Exception {
        when(nutritionPlanService.findOne(1L)).thenReturn(Optional.of(plan1));
        when(modelMapper.map(plan1, NutritionPlanDto.class)).thenReturn(planDto1);

        mockMvc.perform(get("/api/users/testuser/nutrition-plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Plan A"));
    }

    @Test
    void testGetNutritionPlan_NotFound() throws Exception {
        when(nutritionPlanService.findOne(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/testuser/nutrition-plans/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllNutritionPlans_Success() throws Exception {
        List<NutritionPlan> plans = Arrays.asList(plan1, plan2);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(nutritionPlanService.findAllByUser(testUser)).thenReturn(plans);
        when(modelMapper.map(plan1, NutritionPlanDto.class)).thenReturn(planDto1);
        when(modelMapper.map(plan2, NutritionPlanDto.class)).thenReturn(planDto2);

        mockMvc.perform(get("/api/users/testuser/nutrition-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value("Plan A"))
                .andExpect(jsonPath("$[1].planName").value("Plan B"));
    }

    @Test
    void testGetAllNutritionPlans_UserNotFound() throws Exception {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/nonexistentuser/nutrition-plans"))
                .andExpect(status().isNotFound());
    }
}
