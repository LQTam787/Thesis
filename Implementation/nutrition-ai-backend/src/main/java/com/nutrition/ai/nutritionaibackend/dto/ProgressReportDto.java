package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for representing a comprehensive progress report.
 * Nguyên lý hoạt động: Đóng gói một báo cáo tiến trình toàn diện, bao gồm các nhật ký hoạt động, nhật ký thực phẩm và gợi ý liên quan.
 * Luồng hoạt động: Được sử dụng để hiển thị một cái nhìn tổng quan về tiến trình của người dùng trong một khoảng thời gian nhất định (ví dụ: tuần, tháng).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReportDto {

    private Long id; // ID duy nhất của báo cáo (nếu được lưu trữ).

    private EReportType reportType; // Loại báo cáo (ví dụ: WEEKLY, MONTHLY).

    private LocalDateTime generatedDate; // Ngày và thời gian báo cáo được tạo.

    private String summary; // Tóm tắt tổng thể về tiến trình.

    private List<FoodLogDto> foodLogs; // Danh sách chi tiết các nhật ký thực phẩm trong kỳ báo cáo. Nguyên lý hoạt động: Kết hợp dữ liệu từ nhiều nguồn (FoodLog) vào một DTO duy nhất.

    private List<ActivityLogDto> activityLogs; // Danh sách chi tiết các nhật ký hoạt động trong kỳ báo cáo.

    private List<SuggestionDto> suggestions; // Danh sách các gợi ý được tạo ra dựa trên dữ liệu này.
}