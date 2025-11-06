package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Recipe entity.
 * Nguyên lý hoạt động: Đóng gói chi tiết về công thức nấu ăn liên quan đến một mục thực phẩm (FoodItem).
 * Luồng hoạt động: Được sử dụng để hiển thị các bước và thời gian nấu ăn khi người dùng xem một món ăn.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id; // ID duy nhất của công thức.
    private String instructions; // Các bước hướng dẫn nấu ăn chi tiết.
    private Integer preparationTime; // Thời gian chuẩn bị và nấu nướng (ví dụ: phút).
    private Long foodItemId; // ID của mục thực phẩm mà công thức này mô tả (tham chiếu ngược đến FoodItemDto).
}