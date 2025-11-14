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
    private Long id; // ID duy nhất của kế hoạch dinh dưỡng. Nguyên lý hoạt động: Dùng để tham chiếu đến kế hoạch cụ thể trong cơ sở dữ liệu. Luồng hoạt động: Thường được sinh ra bởi server khi tạo mới.
    private String planName; // Tên của kế hoạch (ví dụ: "Kế hoạch Giảm cân 4 tuần"). Nguyên lý hoạt động: Cung cấp định danh dễ nhớ cho người dùng.
    private LocalDate startDate; // Ngày bắt đầu kế hoạch. Nguyên lý hoạt động: Đặt mốc thời gian bắt đầu tính toán hoặc triển khai kế hoạch.
    private LocalDate endDate; // Ngày kết thúc kế hoạch. Nguyên lý hoạt động: Đặt mốc thời gian kết thúc hoặc đánh giá lại kế hoạch.

    /**
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên cho kế hoạch này.
     * Nguyên lý hoạt động: Mô tả chi tiết mục tiêu (ví dụ: "Ăn 1800 calo/ngày, 40% Carb, 30% Protein, 30% Fat").
     */
    private String nutritionGoal;
    private Long userId; // ID của người dùng sở hữu kế hoạch. Nguyên lý hoạt động: Liên kết kế hoạch này với một người dùng cụ thể. Luồng hoạt động: Được sử dụng để truy vấn và bảo mật dữ liệu.
    private List<DailyMenuDto> dailyMenus; // Danh sách các thực đơn hàng ngày (DailyMenuDto) thuộc về kế hoạch. Nguyên lý hoạt động: Thể hiện mối quan hệ 1-nhiều (một kế hoạch có nhiều thực đơn ngày). Luồng hoạt động: Chứa dữ liệu chi tiết về bữa ăn hàng ngày.
}