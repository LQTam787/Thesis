package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.ESuggestionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Suggestion entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về một gợi ý cá nhân hóa do AI/hệ thống tạo ra cho người dùng.
 * Luồng hoạt động: Được sử dụng để hiển thị các gợi ý dinh dưỡng, hoạt động, hoặc mục tiêu cho người dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDto {
    private Long id; // ID duy nhất của gợi ý.
    private String suggestionText; // Nội dung văn bản của gợi ý.
    private LocalDateTime createdDate; // Ngày tạo gợi ý.
    private ESuggestionCategory category; // Phân loại gợi ý (ví dụ: NUTRITION, ACTIVITY, GOAL).
    private Long userId; // ID của người dùng nhận gợi ý.
}