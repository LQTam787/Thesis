package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for NutritionPlan entity.
 * Nguyên lý hoạt động: Đóng gói dữ liệu tổng thể về một kế hoạch dinh dưỡng của người dùng, bao gồm mục tiêu calo và các thực đơn hàng ngày.
 * Luồng hoạt động: Được sử dụng để gửi toàn bộ kế hoạch dinh dưỡng chi tiết đến người dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanDto {
    private Long id; // ID duy nhất của kế hoạch dinh dưỡng.
    private String planName; // Tên của kế hoạch (ví dụ: "Kế hoạch Giảm cân 4 tuần").
    private LocalDate startDate; // Ngày bắt đầu kế hoạch.
    private LocalDate endDate; // Ngày kết thúc kế hoạch.

    /**
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên cho kế hoạch này.
     */
    private String nutritionGoal;
    private Long userId; // ID của người dùng sở hữu kế hoạch.
    private List<DailyMenuDto> dailyMenus; // Danh sách các thực đơn hàng ngày (DailyMenuDto) thuộc về kế hoạch. Nguyên lý hoạt động: Thể hiện mối quan hệ 1-nhiều (một kế hoạch có nhiều thực đơn ngày).
}