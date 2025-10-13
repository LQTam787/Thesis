package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingController.class)
@WithMockUser
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
        FoodLogDto inputDto = new FoodLogDto(null, LocalDateTime.now(), 100.0, "g", 1L, 1L);
        FoodLogDto responseDto = new FoodLogDto(1L, LocalDateTime.now(), 100.0, "g", 1L, 1L);

        when(trackingService.logFood(eq(userId), any(FoodLogDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/tracking/food/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogActivity_Success() throws Exception {
        ActivityLogDto inputDto = new ActivityLogDto(null, LocalDateTime.now(), 60, 300.0, 1L, 1L);
        ActivityLogDto responseDto = new ActivityLogDto(1L, LocalDateTime.now(), 60, 300.0, 1L, 1L);

        when(trackingService.logActivity(eq(userId), any(ActivityLogDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/tracking/activity/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProgressReport_Success() throws Exception {
        ProgressReportDto responseDto = new ProgressReportDto(1L, EReportType.WEEKLY_PROGRESS, LocalDateTime.now(), "Report for user 123 for week X", null, null, null);

        when(trackingService.generateProgressReport(eq(userId), eq("weekly"))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tracking/report/{userId}", userId)
                        .param("type", "weekly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportType").value("WEEKLY_PROGRESS"))
                .andExpect(jsonPath("$.summary").value("Report for user 123 for week X"));
    }
}
