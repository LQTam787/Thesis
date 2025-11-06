package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Thể hiện một mặt hàng thực phẩm cơ bản (ví dụ: "Táo", "Ức gà").
 * Chứa thông tin dinh dưỡng cơ bản. Ánh xạ tới bảng "food_items".
 */
@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên của mặt hàng thực phẩm.
     */
    private String name;

    /**
     * Lượng calo trên một kích thước khẩu phần (serving size).
     */
    private Double calories;

    /**
     * Lượng Protein trên một kích thước khẩu phần.
     */
    private Double protein;

    /**
     * Lượng Carb trên một kích thước khẩu phần.
     */
    private Double carbs;

    /**
     * Lượng Chất béo (Fats) trên một kích thước khẩu phần.
     */
    private Double fats;

    /**
     * Mô tả kích thước khẩu phần cơ sở để tính toán dinh dưỡng (ví dụ: "100g", "1 quả").
     */
    private String servingSize;

    /**
     * Mối quan hệ One-to-One với Recipe.
     * Một FoodItem có thể là thành phần cơ sở cho một Recipe (Công thức nấu ăn).
     * mappedBy="foodItem": Cho biết trường 'foodItem' trong class Recipe quản lý mối quan hệ.
     */
    @OneToOne(mappedBy = "foodItem", fetch = FetchType.LAZY)
    private Recipe recipe;

    /**
     * Mối quan hệ One-to-Many với MealFoodItem.
     * Một FoodItem có thể xuất hiện trong nhiều Meal (Bữa ăn) thông qua bảng liên kết MealFoodItem.
     */
    @OneToMany(mappedBy = "foodItem")
    private List<MealFoodItem> mealFoodItems;

    /**
     * Mối quan hệ One-to-Many với FoodLog.
     * Một FoodItem có thể được ghi lại trong nhiều FoodLog (Nhật ký ăn uống) của người dùng.
     */
    @OneToMany(mappedBy = "foodItem")
    private List<FoodLog> foodLogs;
}