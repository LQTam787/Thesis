package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for MealFoodItem entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về một thực phẩm cụ thể (FoodItem) trong một bữa ăn (Meal).
 * Luồng hoạt động: Thể hiện mối quan hệ nhiều-nhiều giữa Bữa ăn và Thực phẩm, kèm theo chi tiết về số lượng/đơn vị của thực phẩm đó.
 */
@Data // Tự động tạo getters, setters, toString, equals, và hashCode.
@NoArgsConstructor // Tự động tạo constructor không tham số.
@AllArgsConstructor // Tự động tạo constructor với tất cả các trường.
public class MealFoodItemDto {
    private Long id; // ID duy nhất của bản ghi liên kết này.
    private Double quantity; // Số lượng thực phẩm đã được ăn/đề xuất.
    private String unit; // Đơn vị của số lượng (ví dụ: "g", "ml").
    private Long mealId; // ID của bữa ăn chứa thực phẩm này (tham chiếu đến MealDto).
    private Long foodItemId; // ID của thực phẩm đó (tham chiếu đến FoodItemDto).
}