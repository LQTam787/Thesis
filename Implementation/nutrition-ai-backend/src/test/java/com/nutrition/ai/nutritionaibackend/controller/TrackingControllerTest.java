package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingController.class)
class TrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackingService trackingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userId = "123";

    @Test
    void testLogFood_Success() throws Exception {
        FoodLogDto inputDto = new FoodLogDto(null, 1L, LocalDate.now(), LocalTime.now(), 200);
        FoodLogDto responseDto = new FoodLogDto(1L, 1L, LocalDate.now(), LocalTime.now(), 200);

        when(trackingService.logFood(eq(userId), any(FoodLogDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/tracking/food/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.caloriesConsumed").value(200));
    }

    @Test
    void testLogActivity_Success() throws Exception {
        ActivityLogDto inputDto = new ActivityLogDto(null, 1L, LocalDate.now(), LocalTime.now(), 60, 300);
        ActivityLogDto responseDto = new ActivityLogDto(1L, 1L, LocalDate.now(), LocalTime.now(), 60, 300);

        when(trackingService.logActivity(eq(userId), any(ActivityLogDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/tracking/activity/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.caloriesBurned").value(300));
    }

    @Test
    void testGetProgressReport_Success() throws Exception {
        ProgressReportDto responseDto = new ProgressReportDto("weekly", "Report for user 123 for week X");

        when(trackingService.generateProgressReport(eq(userId), eq("weekly"))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tracking/report/{userId}", userId)
                        .param("type", "weekly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportType").value("weekly"))
                .andExpect(jsonPath("$.reportContent").value("Report for user 123 for week X"));
    }
}
