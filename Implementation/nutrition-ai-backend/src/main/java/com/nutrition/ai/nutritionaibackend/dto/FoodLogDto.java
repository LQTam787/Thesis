package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for FoodLog entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về việc người dùng đã ăn gì, ăn bao nhiêu và khi nào.
 * Luồng hoạt động: Được sử dụng để ghi lại (log) việc tiêu thụ thực phẩm của người dùng vào cơ sở dữ liệu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodLogDto {
    private Long id; // ID duy nhất của bản ghi FoodLog.
    private LocalDateTime logDate; // Ngày và thời gian tiêu thụ thực phẩm.
    private Double quantity; // Số lượng đã tiêu thụ.
    private String unit; // Đơn vị của số lượng (ví dụ: "g", "ml", "phần").
    private Long userId; // ID của người dùng đã ghi lại.
    private Long foodItemId; // ID của loại thực phẩm đã ăn (tham chiếu đến FoodItemDto).
}