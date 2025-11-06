package com.nutrition.ai.nutritionaibackend.model.shared;

/**
 * Enum representing the type of content that can be shared.
 *
 * NGUYÊN LÝ HOẠT ĐỘNG:
 * Định nghĩa tập hợp cố định các loại dữ liệu nghiệp vụ có thể được chia sẻ trong hệ thống.
 * SharedContent sử dụng Enum này để biết nó đang tham chiếu đến ID của loại nội dung nào.
 */
public enum ContentType {
    NUTRITION_PLAN, // Kế hoạch Dinh dưỡng
    ACTIVITY_LOG,   // Nhật ký Hoạt động
    RECIPE          // Công thức
}