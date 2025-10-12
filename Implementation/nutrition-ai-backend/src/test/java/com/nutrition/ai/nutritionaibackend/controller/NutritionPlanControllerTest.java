package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.NutritionPlanService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionPlanController.class)
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

    private final String username = "testuser";
    private final Long userId = 1L;
    private final User user = new User(userId, username, "password", "test@example.com", null, null, null, null, null);

    @Test
    void testCreateNutritionPlan_Success() throws Exception {
        NutritionPlanDto inputDto = new NutritionPlanDto(null, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7));
        NutritionPlan nutritionPlan = new NutritionPlan(null, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7), user);
        NutritionPlan savedPlan = new NutritionPlan(1L, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7), user);
        NutritionPlanDto responseDto = new NutritionPlanDto(1L, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(modelMapper.map(inputDto, NutritionPlan.class)).thenReturn(nutritionPlan);
        when(nutritionPlanService.save(any(NutritionPlan.class))).thenReturn(savedPlan);
        when(modelMapper.map(savedPlan, NutritionPlanDto.class)).thenReturn(responseDto);

        mockMvc.perform(post("/api/users/{username}/nutrition-plans", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Plan 1"));
    }

    @Test
    void testCreateNutritionPlan_UserNotFound() throws Exception {
        NutritionPlanDto inputDto = new NutritionPlanDto(null, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7));

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/{username}/nutrition-plans", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isInternalServerError()) // RuntimeException maps to 500 by default
                .andExpect(jsonPath("$.message").value("User not found with username: testuser"));
    }

    @Test
    void testGetNutritionPlan_Found() throws Exception {
        Long planId = 1L;
        NutritionPlan nutritionPlan = new NutritionPlan(planId, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7), user);
        NutritionPlanDto responseDto = new NutritionPlanDto(planId, "Plan 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(7));

        when(nutritionPlanService.findOne(planId)).thenReturn(Optional.of(nutritionPlan));
        when(modelMapper.map(nutritionPlan, NutritionPlanDto.class)).thenReturn(responseDto);

        mockMvc.perform(get("/api/users/{username}/nutrition-plans/{planId}", username, planId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(planId))
                .andExpect(jsonPath("$.name").value("Plan 1"));
    }

    @Test
    void testGetNutritionPlan_NotFound() throws Exception {
        Long planId = 99L;

        when(nutritionPlanService.findOne(planId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{username}/nutrition-plans/{planId}", username, planId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllNutritionPlans_Success() throws Exception {
        NutritionPlan plan1 = new NutritionPlan(1L, "Plan A", "Desc A", LocalDate.now(), LocalDate.now().plusDays(1), user);
        NutritionPlan plan2 = new NutritionPlan(2L, "Plan B", "Desc B", LocalDate.now(), LocalDate.now().plusDays(2), user);
        List<NutritionPlan> nutritionPlans = Arrays.asList(plan1, plan2);

        NutritionPlanDto dto1 = new NutritionPlanDto(1L, "Plan A", "Desc A", LocalDate.now(), LocalDate.now().plusDays(1));
        NutritionPlanDto dto2 = new NutritionPlanDto(2L, "Plan B", "Desc B", LocalDate.now(), LocalDate.now().plusDays(2));
        List<NutritionPlanDto> nutritionPlanDtos = Arrays.asList(dto1, dto2);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(nutritionPlanService.findAllByUser(user)).thenReturn(nutritionPlans);
        when(modelMapper.map(plan1, NutritionPlanDto.class)).thenReturn(dto1);
        when(modelMapper.map(plan2, NutritionPlanDto.class)).thenReturn(dto2);

        mockMvc.perform(get("/api/users/{username}/nutrition-plans", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Plan A"))
                .andExpect(jsonPath("$[1].name").value("Plan B"));
    }

    @Test
    void testGetAllNutritionPlans_UserNotFound() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{username}/nutrition-plans", username))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("User not found with username: testuser"));
    }
}
