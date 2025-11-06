package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EMealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * Data Transfer Object for Meal entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về một bữa ăn cụ thể trong thực đơn hàng ngày, bao gồm loại bữa ăn, thời gian và các món ăn.
 * Luồng hoạt động: Là thành phần của DailyMenuDto, chứa danh sách các món ăn/thực phẩm trong bữa ăn đó.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {
    private Long id; // ID duy nhất của bữa ăn.
    private EMealType mealType; // Loại bữa ăn (ví dụ: BREAKFAST, LUNCH, DINNER). Nguyên lý hoạt động: Sử dụng enum để phân loại.
    private LocalTime time; // Thời gian dự kiến ăn bữa này.
    private Long dailyMenuId; // ID của thực đơn hàng ngày mà bữa ăn này thuộc về.
    private List<MealFoodItemDto> mealFoodItems; // Danh sách các mục thực phẩm cụ thể trong bữa ăn. Nguyên lý hoạt động: Thể hiện thành phần của bữa ăn (cần có một DTO cho MealFoodItemDto).
}