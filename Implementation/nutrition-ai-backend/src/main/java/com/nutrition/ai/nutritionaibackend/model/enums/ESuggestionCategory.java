package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The ESuggestionCategory enum categorizes the types of suggestions provided by the AI.
 *
 * Nguyên lý hoạt động:
 * Enum này phân loại các khuyến nghị được tạo ra bởi mô hình AI.
 * Nó giúp người dùng hoặc các module khác trong hệ thống lọc và xử lý các 
 * đề xuất dựa trên bản chất của chúng (ví dụ: đề xuất về chế độ ăn uống 
 * so với đề xuất về hoạt động thể chất).
 */
public enum ESuggestionCategory {
    /**
     * Đại diện cho các gợi ý liên quan đến việc thay đổi chế độ ăn uống (ví dụ: giảm lượng calo, tăng protein).
     * Vai trò: Phân loại gợi ý dinh dưỡng.
     */
    DIET_ADJUSTMENT,
    /**
     * Đại diện cho các gợi ý liên quan đến việc tăng cường hoạt động thể chất (ví dụ: tập thêm 30 phút đi bộ).
     * Vai trò: Phân loại gợi ý hoạt động thể chất.
     */
    ACTIVITY_INCREASE
}