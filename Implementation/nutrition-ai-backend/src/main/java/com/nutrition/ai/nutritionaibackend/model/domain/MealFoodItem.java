package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thể hiện mối quan hệ Many-to-Many giữa Meal và FoodItem,
 * bao gồm chi tiết số lượng. Ánh xạ tới bảng "meal_food_items".
 */
@Entity
@Table(name = "meal_food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealFoodItem {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Số lượng FoodItem trong bữa ăn.
     */
    private Double quantity;

    /**
     * Đơn vị của số lượng.
     */
    private String unit;

    /**
     * Mối quan hệ Many-to-One với Meal.
     * Liên kết thành phần này với một Meal cụ thể.
     * @JoinColumn(name = "meal_id"): Khóa ngoại liên kết tới Meal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    /**
     * Mối quan hệ Many-to-One với FoodItem.
     * Liên kết thành phần này với một FoodItem cụ thể.
     * @JoinColumn(name = "food_item_id"): Khóa ngoại liên kết tới FoodItem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
}