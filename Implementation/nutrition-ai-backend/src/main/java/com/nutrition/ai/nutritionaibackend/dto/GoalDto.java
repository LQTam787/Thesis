package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Goal entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về mục tiêu sức khỏe/thể chất của người dùng.
 * Luồng hoạt động: Được sử dụng để gửi/lấy mục tiêu của người dùng (ví dụ: giảm cân, tăng cân) đến/từ server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
    private Long id; // ID duy nhất của mục tiêu.
    private Double targetWeight; // Cân nặng mục tiêu.
    private LocalDate targetDate; // Ngày dự kiến đạt được mục tiêu.
    private EGoalType goalType; // Loại mục tiêu (ví dụ: GAIN_WEIGHT, LOSE_WEIGHT, MAINTAIN). Nguyên lý hoạt động: Sử dụng enum để đảm bảo tính nhất quán của dữ liệu.
    private String status; // Trạng thái của mục tiêu (ví dụ: ACTIVE, COMPLETED, ARCHIVED).
    private Long userId; // ID của người dùng sở hữu mục tiêu này.
}