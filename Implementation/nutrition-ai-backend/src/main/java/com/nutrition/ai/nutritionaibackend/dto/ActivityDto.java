package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Activity entity.
 * Nguyên lý hoạt động: Đóng gói thông tin cơ bản về một loại hoạt động thể chất (ví dụ: Tên, Calo đốt cháy).
 * Luồng hoạt động: Được sử dụng để lấy/gửi danh sách các hoạt động có sẵn trong hệ thống.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Long id; // ID duy nhất của loại hoạt động.
    private String name; // Tên của hoạt động (ví dụ: "Chạy bộ", "Bơi lội").
    private Double caloriesBurnedPerHour; // Lượng calo ước tính đốt cháy mỗi giờ cho hoạt động này.
}