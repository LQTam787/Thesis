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
    private Long id; // ID duy nhất của mục tiêu. Nguyên lý hoạt động: Dùng để tham chiếu đến mục tiêu cụ thể.
    private Double targetWeight; // Cân nặng mục tiêu. Nguyên lý hoạt động: Chỉ số định lượng mà người dùng muốn đạt được. Luồng hoạt động: Được sử dụng để tính toán lượng calo cần thiết.
    private LocalDate targetDate; // Ngày dự kiến đạt được mục tiêu. Nguyên lý hoạt động: Thiết lập khung thời gian cho mục tiêu. Luồng hoạt động: Dùng để đánh giá tính khả thi và tốc độ thực hiện.
    private EGoalType goalType; // Loại mục tiêu (ví dụ: GAIN_WEIGHT, LOSE_WEIGHT, MAINTAIN). Nguyên lý hoạt động: Sử dụng enum để đảm bảo tính nhất quán của dữ liệu và định hướng cho việc tính toán dinh dưỡng.
    private String status; // Trạng thái của mục tiêu (ví dụ: ACTIVE, COMPLETED, ARCHIVED). Nguyên lý hoạt động: Quản lý vòng đời của mục tiêu. Luồng hoạt động: Giúp hệ thống và người dùng theo dõi tiến độ.
    private Long userId; // ID của người dùng sở hữu mục tiêu này. Nguyên lý hoạt động: Liên kết mục tiêu này với một người dùng cụ thể.

    /**
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên.
     * Nguyên lý hoạt động: Mô tả chi tiết mục tiêu calo/macro (ví dụ: "Thiết lập mức thâm hụt 500 calo mỗi ngày"). Luồng hoạt động: Dùng làm đầu vào cho mô hình AI hoặc công cụ tính toán.
     */
    private String nutritionGoal;
}