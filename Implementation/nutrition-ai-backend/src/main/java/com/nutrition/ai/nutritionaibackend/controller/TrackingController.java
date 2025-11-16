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
 * TrackingController xử lý các yêu cầu HTTP liên quan đến việc theo dõi tiến độ sức khỏe và dinh dưỡng của người dùng.
 * <p>
 * Nguyên lý hoạt động: Controller này cung cấp các RESTful endpoint cho phép người dùng ghi nhật ký thực phẩm đã tiêu thụ,
 * các hoạt động thể chất đã hoàn thành, và tạo các báo cáo tiến độ. Nó ủy quyền logic nghiệp vụ cho
 * {@link com.nutrition.ai.nutritionaibackend.service.TrackingService}.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP (POST, GET) từ client tại đường dẫn cơ sở {@code /api/v1/tracking}.</li>
 *     <li>Trích xuất {@code userId} từ đường dẫn và dữ liệu liên quan từ {@code @RequestBody} hoặc {@code @RequestParam}.</li>
 *     <li>Ủy quyền các yêu cầu xử lý nghiệp vụ cho {@code TrackingService} (ví dụ: lưu nhật ký, tính toán báo cáo).</li>
 *     <li>Trả về {@code ResponseEntity} với dữ liệu {@link FoodLogDto}, {@link ActivityLogDto}, {@link ProgressReportDto},
 *         hoặc thông báo trạng thái HTTP thích hợp (ví dụ: 200 OK).</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/v1/tracking") // Ánh xạ với đường dẫn cơ sở /api/v1/tracking
@Tag(name = "Progress Tracking", description = "Endpoints for logging food, activities, and generating progress reports") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
@RequiredArgsConstructor // Tạo constructor cho các trường final (TrackingService)
public class TrackingController {

    private final TrackingService trackingService; // Dependency Injection

    /**
     * Ghi nhật ký một mục thực phẩm mà người dùng đã tiêu thụ.
     * <p>
     * Endpoint này nhận ID của người dùng và chi tiết mục thực phẩm đã ăn dưới dạng {@link FoodLogDto}.
     * Nó ghi lại thông tin này thông qua {@link TrackingService}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/v1/tracking/food/{userId}} với {@link FoodLogDto} trong body.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn và {@code foodLogDto} từ body.</li>
     *     <li>Gọi phương thức {@code trackingService.logFood(userId, foodLogDto)}.</li>
     *     <li>Trả về {@link FoodLogDto} đã lưu với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng đang ghi nhật ký thực phẩm.
     * @param foodLogDto DTO chứa thông tin chi tiết về mục thực phẩm đã tiêu thụ.
     * @return ResponseEntity chứa {@link FoodLogDto} của mục thực phẩm đã được ghi nhật ký.
     */
    @Operation(summary = "Log a food item", description = "Logs a food item that a user has consumed.")
    @ApiResponse(responseCode = "200", description = "Food logged successfully")
    @PostMapping("/food/{userId}") // Ánh xạ với POST /api/v1/tracking/food/{userId}
    public ResponseEntity<FoodLogDto> logFood(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody FoodLogDto foodLogDto) {
        return ResponseEntity.ok(trackingService.logFood(userId, foodLogDto));
    }

    /**
     * Ghi nhật ký một hoạt động thể chất mà người dùng đã hoàn thành.
     * <p>
     * Endpoint này nhận ID của người dùng và chi tiết hoạt động dưới dạng {@link ActivityLogDto}.
     * Nó ghi lại thông tin này thông qua {@link TrackingService}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/v1/tracking/activity/{userId}} với {@link ActivityLogDto} trong body.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn và {@code activityLogDto} từ body.</li>
     *     <li>Gọi phương thức {@code trackingService.logActivity(userId, activityLogDto)}.</li>
     *     <li>Trả về {@link ActivityLogDto} đã lưu với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng đang ghi nhật ký hoạt động.
     * @param activityLogDto DTO chứa thông tin chi tiết về hoạt động thể chất đã hoàn thành.
     * @return ResponseEntity chứa {@link ActivityLogDto} của hoạt động đã được ghi nhật ký.
     */
    @Operation(summary = "Log an activity", description = "Logs a physical activity that a user has completed.")
    @ApiResponse(responseCode = "200", description = "Activity logged successfully")
    @PostMapping("/activity/{userId}") // Ánh xạ với POST /api/v1/tracking/activity/{userId}
    public ResponseEntity<ActivityLogDto> logActivity(@Parameter(description = "ID of the user") @PathVariable String userId, @RequestBody ActivityLogDto activityLogDto) {
        return ResponseEntity.ok(trackingService.logActivity(userId, activityLogDto));
    }

    /**
     * Tạo và truy xuất báo cáo tiến độ cho một người dùng cụ thể.
     * <p>
     * Endpoint này cho phép người dùng xem báo cáo tiến độ của họ theo các loại khác nhau
     * (ví dụ: hàng tuần, hàng tháng). Nó sử dụng {@link TrackingService} để tổng hợp dữ liệu và tạo báo cáo.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/v1/tracking/report/{userId}} với {@code userId} từ đường dẫn và {@code type} từ request parameter.</li>
     *     <li>Trích xuất {@code userId} từ đường dẫn và {@code type} của báo cáo (mặc định là "weekly").</li>
     *     <li>Gọi phương thức {@code trackingService.generateProgressReport(userId, type)}.</li>
     *     <li>Trả về {@link ProgressReportDto} chứa báo cáo tiến độ với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param userId ID của người dùng cần tạo báo cáo tiến độ.
     * @param type Loại báo cáo tiến độ cần tạo (ví dụ: "weekly", "monthly"). Mặc định là "weekly".
     * @return ResponseEntity chứa {@link ProgressReportDto} của báo cáo tiến độ.
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