package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * TrackingController xử lý các yêu cầu liên quan đến việc theo dõi tiến độ của người dùng (ghi nhật ký thực phẩm và hoạt động).
 * Nguyên lý hoạt động: Các endpoint giao tiếp với TrackingService để xử lý logic kinh doanh về việc lưu trữ và tổng hợp dữ liệu theo dõi.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/v1/tracking") // Ánh xạ với đường dẫn cơ sở /api/v1/tracking
@Tag(name = "Progress Tracking", description = "Endpoints for logging food, activities, and generating progress reports") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
@RequiredArgsConstructor // Tạo constructor cho các trường final (TrackingService)
public class TrackingController {

    private final TrackingService trackingService; // Dependency Injection

    /**
     * Ghi nhật ký một mục thực phẩm đã tiêu thụ.
     * Luồng hoạt động:
     * 1. Nhận 'userId' từ path variable và FoodLogDto từ body của request.
     * 2. Gọi trackingService.logFood(userId, foodLogDto) để xử lý logic lưu trữ.
     * 3. Trả về FoodLogDto đã lưu với HTTP status 200 (OK).
     *
     * @param userId ID của người dùng.
     * @param foodLogDto DTO chứa chi tiết mục thực phẩm đã ăn.
     * @return ResponseEntity chứa FoodLogDto đã lưu.
     */
    @Operation(summary = "Log a food item", description = "Logs a food item that a user has consumed.")
    @ApiResponse(responseCode = "200", description = "Food logged successfully")
    @PostMapping("/food/{userId}") // Ánh xạ với POST /api/v1/tracking/food/{userId}
    public ResponseEntity<FoodLogDto> logFood(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody FoodLogDto foodLogDto) {
        return ResponseEntity.ok(trackingService.logFood(userId, foodLogDto));
    }

    /**
     * Ghi nhật ký một hoạt động thể chất đã hoàn thành.
     * Luồng hoạt động:
     * 1. Nhận 'userId' từ path variable và ActivityLogDto từ body của request.
     * 2. Gọi trackingService.logActivity(userId, activityLogDto) để xử lý logic lưu trữ.
     * 3. Trả về ActivityLogDto đã lưu với HTTP status 200 (OK).
     *
     * @param userId ID của người dùng.
     * @param activityLogDto DTO chứa chi tiết hoạt động.
     * @return ResponseEntity chứa ActivityLogDto đã lưu.
     */
    @Operation(summary = "Log an activity", description = "Logs a physical activity that a user has completed.")
    @ApiResponse(responseCode = "200", description = "Activity logged successfully")
    @PostMapping("/activity/{userId}") // Ánh xạ với POST /api/v1/tracking/activity/{userId}
    public ResponseEntity<ActivityLogDto> logActivity(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody ActivityLogDto activityLogDto) {
        return ResponseEntity.ok(trackingService.logActivity(userId, activityLogDto));
    }

    /**
     * Tạo và truy xuất báo cáo tiến độ cho người dùng.
     * Luồng hoạt động:
     * 1. Nhận 'userId' từ path variable và 'type' (mặc định là "weekly") từ request param.
     * 2. Gọi trackingService.generateProgressReport(userId, type) để xử lý logic tính toán.
     * 3. Trả về ProgressReportDto với HTTP status 200 (OK).
     *
     * @param userId ID của người dùng.
     * @param type Loại báo cáo (ví dụ: weekly, monthly).
     * @return ResponseEntity chứa ProgressReportDto.
     */
    @Operation(summary = "Generate a progress report", description = "Generates a progress report for a user, which can be of different types (e.g., weekly, monthly).")
    @ApiResponse(responseCode = "200", description = "Report generated successfully")
    @GetMapping("/report/{userId}") // Ánh xạ với GET /api/v1/tracking/report/{userId}
    public ResponseEntity<ProgressReportDto> getProgressReport(
            @Parameter(description = "ID of the user") @PathVariable String userId,
            @Parameter(description = "Type of the report (e.g., weekly, monthly)") @RequestParam(defaultValue = "weekly") String type) {
        return ResponseEntity.ok(trackingService.generateProgressReport(userId, type));
    }
}