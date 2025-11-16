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

/**
 * Lớp kiểm thử `TrackingControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến chức năng theo dõi
 * (ghi nhật ký thực phẩm, ghi nhật ký hoạt động và tạo báo cáo tiến độ) trong `TrackingController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `TrackingController`.
 * `@WithMockUser` giả lập một người dùng đã đăng nhập, cho phép kiểm thử các điểm cuối yêu cầu xác thực.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `TrackingService`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc và cô lập logic của controller để kiểm thử.
 */
@WebMvcTest(TrackingController.class)
@WithMockUser
class TrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackingService trackingService; // Service giả lập cho chức năng theo dõi

    @Autowired
    private ObjectMapper objectMapper;

    private final String userId = "123"; // ID người dùng giả lập trong đường dẫn

    /**
     * Kiểm thử kịch bản thành công khi ghi nhật ký thực phẩm cho người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `FoodLogDto` đầu vào và một đối tượng `FoodLogDto` phản hồi (có ID đã tạo).
     * 2. Giả lập `trackingService.logFood()` để trả về `FoodLogDto` đã lưu, mô phỏng việc ghi nhật ký thành công.
     * 3. Thực hiện yêu cầu POST đến `/api/v1/tracking/food/{userId}` với `FoodLogDto` dưới dạng JSON và token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK.
     */
    @Test
    void testLogFood_Success() throws Exception {
        // 1. Chuẩn bị DTO đầu vào và DTO phản hồi (có ID)
        FoodLogDto inputDto = new FoodLogDto(null, LocalDateTime.now(), 100.0, "g", 1L, 1L);
        FoodLogDto responseDto = new FoodLogDto(1L, LocalDateTime.now(), 100.0, "g", 1L, 1L);

        // 2. Mocking Service: Giả lập lưu thành công và trả về DTO đã lưu
        when(trackingService.logFood(eq(userId), any(FoodLogDto.class))).thenReturn(responseDto);

        // 3. Thực hiện yêu cầu POST /api/v1/tracking/food/{userId}
        mockMvc.perform(post("/api/v1/tracking/food/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk()); // 4. Kiểm tra: Trạng thái 200 OK
    }

    /**
     * Kiểm thử kịch bản thành công khi ghi nhật ký hoạt động cho người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `ActivityLogDto` đầu vào và một đối tượng `ActivityLogDto` phản hồi (có ID đã tạo).
     * 2. Giả lập `trackingService.logActivity()` để trả về `ActivityLogDto` đã lưu, mô phỏng việc ghi nhật ký thành công.
     * 3. Thực hiện yêu cầu POST đến `/api/v1/tracking/activity/{userId}` với `ActivityLogDto` dưới dạng JSON và token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK.
     */
    @Test
    void testLogActivity_Success() throws Exception {
        // Nguyên lý tương tự như testLogFood_Success, nhưng cho ActivityLog
        ActivityLogDto inputDto = new ActivityLogDto(null, LocalDateTime.now(), 60, 300.0, 1L, 1L);
        ActivityLogDto responseDto = new ActivityLogDto(1L, LocalDateTime.now(), 60, 300.0, 1L, 1L);

        when(trackingService.logActivity(eq(userId), any(ActivityLogDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/tracking/activity/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    /**
     * Kiểm thử kịch bản thành công khi tạo và lấy báo cáo tiến độ cho người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `ProgressReportDto` giả lập.
     * 2. Giả lập `trackingService.generateProgressReport()` để trả về báo cáo tiến độ giả lập.
     * 3. Thực hiện yêu cầu GET đến `/api/v1/tracking/report/{userId}` với tham số truy vấn `type` (ví dụ: "weekly").
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và các trường `reportType` và `summary` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
    @Test
    void testGetProgressReport_Success() throws Exception {
        // 1. Chuẩn bị DTO báo cáo giả lập
        ProgressReportDto responseDto = new ProgressReportDto(1L, EReportType.WEEKLY_PROGRESS, LocalDateTime.now(), "Report for user 123 for week X", null, null, null);

        // 2. Mocking Service: Giả lập tạo báo cáo thành công
        when(trackingService.generateProgressReport(eq(userId), eq("weekly"))).thenReturn(responseDto);

        // 3. Thực hiện yêu cầu GET /api/v1/tracking/report/{userId} với tham số 'type'
        mockMvc.perform(get("/api/v1/tracking/report/{userId}", userId)
                        .param("type", "weekly")) // Thêm tham số truy vấn
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportType").value("WEEKLY_PROGRESS"))
                .andExpect(jsonPath("$.summary").value("Report for user 123 for week X")); // 4. Kiểm tra nội dung
    }
}