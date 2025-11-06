package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for DailyMenu entity.
 * Nguyên lý hoạt động: Đóng gói dữ liệu về một thực đơn dinh dưỡng cho một ngày cụ thể, bao gồm danh sách các bữa ăn.
 * Luồng hoạt động: Được sử dụng để lấy toàn bộ thực đơn trong ngày cho người dùng từ server hoặc gửi thực đơn đã tạo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMenuDto {
    private Long id; // ID duy nhất của thực đơn hàng ngày.
    private LocalDate date; // Ngày áp dụng thực đơn.
    private Long nutritionPlanId; // ID của kế hoạch dinh dưỡng mà thực đơn này thuộc về.
    private List<MealDto> meals; // Danh sách các bữa ăn (MealDto) trong ngày. Nguyên lý hoạt động: Thể hiện mối quan hệ 1-nhiều (một thực đơn có nhiều bữa ăn).
}