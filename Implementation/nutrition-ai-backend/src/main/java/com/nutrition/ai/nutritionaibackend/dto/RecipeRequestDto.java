package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Recipe (and its associated FoodItem).
 * Nguyên lý hoạt động: DTO tổng hợp các trường cần thiết từ cả FoodItem và Recipe để tạo mới hoặc cập nhật cả hai thực thể cùng lúc qua một API call.
 * Luồng hoạt động: Được gửi từ client đến server khi người dùng muốn thêm một công thức nấu ăn mới vào hệ thống.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {
    // FoodItem fields
    private String name; // Tên món ăn/thực phẩm.
    private Double calories; // Calo.
    private Double protein; // Protein.
    private Double carbs; // Carb.
    private Double fats; // Chất béo.
    private String servingSize; // Kích thước khẩu phần.

    // Recipe fields
    private String instructions; // Các bước hướng dẫn nấu ăn.
    private Integer preparationTime; // Thời gian chuẩn bị.
}