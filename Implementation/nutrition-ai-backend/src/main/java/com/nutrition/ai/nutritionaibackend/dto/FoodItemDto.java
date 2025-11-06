package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for FoodItem entity.
 * Nguyên lý hoạt động: Đóng gói thông tin dinh dưỡng chi tiết về một loại thực phẩm (hoặc một công thức nấu ăn).
 * Luồng hoạt động: Được sử dụng để hiển thị thông tin dinh dưỡng chi tiết khi người dùng tìm kiếm hoặc xem thực phẩm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemDto {
    private Long id; // ID duy nhất của mục thực phẩm.
    private String name; // Tên của thực phẩm (ví dụ: "Táo", "Ức gà").
    private Double calories; // Lượng calo.
    private Double protein; // Lượng protein.
    private Double carbs; // Lượng carbohydrate.
    private Double fats; // Lượng chất béo.
    private String servingSize; // Kích thước khẩu phần cơ bản (ví dụ: "100g", "1 quả").
    private Long recipeId; // ID công thức nấu ăn liên quan (nếu đây là một món ăn trong công thức).
}