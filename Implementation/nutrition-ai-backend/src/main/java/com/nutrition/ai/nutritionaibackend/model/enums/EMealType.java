package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The EMealType enum categorizes meals throughout the day.
 * * Nguyên lý hoạt động:
 * Enum này định nghĩa các loại bữa ăn cố định trong ngày. 
 * Nó được sử dụng để phân loại và truy vấn dữ liệu bữa ăn, 
 * đảm bảo rằng tất cả các bữa ăn đều được gán một loại hợp lệ.
 */
public enum EMealType {
    /**
     * Đại diện cho bữa sáng.
     * Vai trò: Phân loại các mục nhật ký thực phẩm được tiêu thụ vào buổi sáng.
     */
    BREAKFAST,
    /**
     * Đại diện cho bữa trưa.
     * Vai trò: Phân loại các mục nhật ký thực phẩm được tiêu thụ vào buổi trưa.
     */
    LUNCH,
    /**
     * Đại diện cho bữa tối.
     * Vai trò: Phân loại các mục nhật ký thực phẩm được tiêu thụ vào buổi tối.
     */
    DINNER,
    /**
     * Đại diện cho các bữa ăn nhẹ giữa các bữa chính.
     * Vai trò: Phân loại các mục nhật ký thực phẩm không phải là bữa chính.
     */
    SNACK
}