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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionPlanController.class)
@WithMockUser(username = "testuser") // Giả lập người dùng hiện tại là "testuser"
class NutritionPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NutritionPlanService nutritionPlanService; // Service giả lập cho kế hoạch dinh dưỡng

    @MockBean
    private UserRepository userRepository; // Repository giả lập (cần thiết để tìm User theo username trong Controller)

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
        // Chuẩn bị các đối tượng giả lập
        testUser = new User(1L, "testuser", "password", "test@example.com", Collections.emptySet());
        // ... (Khởi tạo plan1, planDto1, plan2, planDto2)
    }

    @Test
    void testCreateNutritionPlan_Success() throws Exception {
        // 1. Mocking UserRepository: Giả lập tìm thấy User theo username từ đường dẫn/Security Context
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        // 2. Mocking ModelMapper: DTO -> Entity
        when(modelMapper.map(any(NutritionPlanDto.class), eq(NutritionPlan.class))).thenReturn(plan1);
        // 3. Mocking Service: Lưu Entity thành công
        when(nutritionPlanService.save(any(NutritionPlan.class))).thenReturn(plan1);
        // 4. Mocking ModelMapper: Entity -> DTO phản hồi
        when(modelMapper.map(eq(plan1), eq(NutritionPlanDto.class))).thenReturn(planDto1);

        // 5. Thực hiện yêu cầu POST /api/users/testuser/nutrition-plans
        mockMvc.perform(post("/api/users/testuser/nutrition-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planDto1)))
                .andExpect(status().isOk()) // 6. Kiểm tra: Trạng thái 200 OK
                .andExpect(jsonPath("$.planName").value("Plan A"));
    }

    @Test
    void testCreateNutritionPlan_UserNotFound() throws Exception {
        // 1. Mocking UserRepository: Giả lập không tìm thấy User
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu POST với username không tồn tại
        mockMvc.perform(post("/api/users/nonexistentuser/nutrition-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planDto1)))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    @Test
    void testGetAllNutritionPlans_Success() throws Exception {
        List<NutritionPlan> plans = Arrays.asList(plan1, plan2);
        // 1. Mocking UserRepository: Giả lập tìm thấy User
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        // 2. Mocking Service: Giả lập tìm tất cả kế hoạch của User đó
        when(nutritionPlanService.findAllByUser(testUser)).thenReturn(plans);
        // 3. Mocking ModelMapper: Chuyển đổi từng Entity -> DTO
        when(modelMapper.map(eq(plan1), eq(NutritionPlanDto.class))).thenReturn(planDto1);
        when(modelMapper.map(eq(plan2), eq(NutritionPlanDto.class))).thenReturn(planDto2);

        // 4. Thực hiện yêu cầu GET /api/users/testuser/nutrition-plans
        mockMvc.perform(get("/api/users/testuser/nutrition-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value("Plan A"))
                .andExpect(jsonPath("$[1].planName").value("Plan B"));
    }
    // ... (Các bài kiểm thử khác tuân theo nguyên lý tìm kiếm/không tìm thấy)
}