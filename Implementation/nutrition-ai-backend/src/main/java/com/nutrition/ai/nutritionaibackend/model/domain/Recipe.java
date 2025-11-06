package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thể hiện Công thức nấu ăn chi tiết.
 * Nó có mối quan hệ One-to-One với FoodItem, cho phép một món ăn cơ bản có thêm công thức.
 * Ánh xạ tới bảng "recipes".
 */
@Entity
@Table(name = "recipes")
@Data // Lombok: Tự động tạo getters, setters, equals, hashCode, và toString.
@NoArgsConstructor // Lombok: Tự động tạo constructor không tham số.
@AllArgsConstructor // Lombok: Tự động tạo constructor có tất cả tham số.
public class Recipe {
    /**
     * Khóa chính của thực thể Recipe.
     * @Id: Đánh dấu là khóa chính.
     * @GeneratedValue: Chỉ định chiến lược tạo giá trị (IDENTITY - tự động tăng).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mối quan hệ One-to-One với FoodItem.
     * Một Recipe liên kết với một FoodItem.
     * cascade = CascadeType.ALL: Áp dụng các thay đổi cho FoodItem liên quan.
     * @JoinColumn: Tạo khóa ngoại 'food_item_id' trong bảng "recipes", trỏ tới cột 'id' của bảng "food_items".
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", referencedColumnName = "id")
    private FoodItem foodItem;

    /**
     * Hướng dẫn chế biến chi tiết.
     * @Lob: Lưu trữ dữ liệu lớn (Large Object), thường được ánh xạ tới kiểu TEXT/CLOB.
     */
    @Lob
    private String instructions;

    /**
     * Thời gian chuẩn bị tính bằng phút.
     */
    private Integer preparationTime; // in minutes
}