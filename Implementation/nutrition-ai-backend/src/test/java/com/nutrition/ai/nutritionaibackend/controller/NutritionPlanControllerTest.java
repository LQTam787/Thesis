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

/**
 * Lớp kiểm thử `NutritionPlanControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến quản lý kế hoạch dinh dưỡng
 * cho người dùng cụ thể trong `NutritionPlanController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `NutritionPlanController`.
 * `@WithMockUser(username = "testuser")` giả lập một người dùng đã đăng nhập với tên người dùng "testuser",
 * cho phép kiểm thử các điểm cuối yêu cầu xác thực và ủy quyền.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `NutritionPlanService`, `UserRepository` và `ModelMapper`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc và cô lập logic của controller để kiểm thử.
 */
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

    /**
     * Phương thức thiết lập ban đầu, chạy trước mỗi bài kiểm thử.
     * Khởi tạo các đối tượng `User`, `NutritionPlan` và `NutritionPlanDto` giả lập để tái sử dụng trong các bài kiểm thử.
     */
    @BeforeEach
    void setUp() {
        // Chuẩn bị các đối tượng giả lập
        testUser = new User(1L, "testuser", "password", "test@example.com", Collections.emptySet());
        plan1 = new NutritionPlan();
        plan1.setId(1L);
        plan1.setPlanName("Plan A");
        plan1.setUser(testUser);

        planDto1 = new NutritionPlanDto();
        planDto1.setId(1L);
        planDto1.setPlanName("Plan A");

        plan2 = new NutritionPlan();
        plan2.setId(2L);
        plan2.setPlanName("Plan B");
        plan2.setUser(testUser);

        planDto2 = new NutritionPlanDto();
        planDto2.setId(2L);
        planDto2.setPlanName("Plan B");
    }

    /**
     * Kiểm thử kịch bản thành công khi tạo một kế hoạch dinh dưỡng mới cho một người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `userRepository.findByUsername()` để trả về `Optional.of(testUser)`.
     * 2. Giả lập `modelMapper` để chuyển đổi từ DTO sang Entity và ngược lại.
     * 3. Giả lập `nutritionPlanService.save()` để trả về `NutritionPlan` đã lưu.
     * 4. Thực hiện yêu cầu POST đến `/api/users/{username}/nutrition-plans` với `NutritionPlanDto` dưới dạng JSON và token CSRF.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `planName` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
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

    /**
     * Kiểm thử kịch bản không tìm thấy người dùng khi tạo kế hoạch dinh dưỡng.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `userRepository.findByUsername()` để trả về `Optional.empty()`.
     * 2. Thực hiện yêu cầu POST đến `/api/users/{nonexistentUsername}/nutrition-plans` với dữ liệu kế hoạch và token CSRF.
     * 3. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi lấy tất cả các kế hoạch dinh dưỡng của một người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `userRepository.findByUsername()` để trả về `Optional.of(testUser)`.
     * 2. Giả lập `nutritionPlanService.findAllByUser()` để trả về danh sách `NutritionPlan` của người dùng đó.
     * 3. Giả lập `modelMapper` để chuyển đổi từng `NutritionPlan` sang `NutritionPlanDto`.
     * 4. Thực hiện yêu cầu GET đến `/api/users/{username}/nutrition-plans`.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và nội dung JSON chứa tên của các kế hoạch dinh dưỡng mong muốn.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi lấy một kế hoạch dinh dưỡng cụ thể theo ID.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `nutritionPlanService.findOne()` để trả về `Optional.of(plan1)`.
     * 2. Giả lập `modelMapper` để chuyển đổi `NutritionPlan` sang `NutritionPlanDto`.
     * 3. Thực hiện yêu cầu GET đến `/api/users/{username}/nutrition-plans/{id}`.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `planName` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
    @Test
    void testGetNutritionPlan_Success() throws Exception {
        // 1. Mocking Service: Giả lập tìm thấy kế hoạch theo ID
        when(nutritionPlanService.findOne(1L)).thenReturn(Optional.of(plan1));
        // 2. Mocking ModelMapper: Chuyển đổi Entity -> DTO
        when(modelMapper.map(eq(plan1), eq(NutritionPlanDto.class))).thenReturn(planDto1);

        // 3. Thực hiện yêu cầu GET /api/users/testuser/nutrition-plans/1
        mockMvc.perform(get("/api/users/testuser/nutrition-plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planName").value("Plan A"));
    }

    /**
     * Kiểm thử kịch bản không tìm thấy kế hoạch dinh dưỡng theo ID.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `nutritionPlanService.findOne()` để trả về `Optional.empty()`.
     * 2. Thực hiện yêu cầu GET đến `/api/users/{username}/nutrition-plans/{id}` với một ID không tồn tại.
     * 3. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
    @Test
    void testGetNutritionPlan_NotFound() throws Exception {
        // 1. Mocking Service: Giả lập không tìm thấy kế hoạch
        when(nutritionPlanService.findOne(99L)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET với ID kế hoạch không tồn tại
        mockMvc.perform(get("/api/users/testuser/nutrition-plans/99"))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    /**
     * Kiểm thử kịch bản không tìm thấy người dùng khi lấy tất cả kế hoạch dinh dưỡng.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `userRepository.findByUsername()` để trả về `Optional.empty()`.
     * 2. Thực hiện yêu cầu GET với username không tồn tại
     * 3. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
    @Test
    void testGetAllNutritionPlans_UserNotFound() throws Exception {
        // 1. Mocking UserRepository: Giả lập không tìm thấy User
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET với username không tồn tại
        mockMvc.perform(get("/api/users/nonexistentuser/nutrition-plans"))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }
}