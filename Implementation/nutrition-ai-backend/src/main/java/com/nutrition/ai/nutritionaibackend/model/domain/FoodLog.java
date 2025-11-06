package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Thể hiện nhật ký ăn uống thực tế của người dùng.
 * Ánh xạ tới bảng "food_logs".
 */
@Entity
@Table(name = "food_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodLog {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Thời gian người dùng ăn món này.
     */
    private LocalDateTime logDate;

    /**
     * Số lượng đã tiêu thụ.
     */
    private Double quantity;

    /**
     * Đơn vị của số lượng (ví dụ: "g", "ml", "phần").
     */
    private String unit;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một FoodLog thuộc về một User.
     * @JoinColumn(name = "user_id"): Khóa ngoại liên kết tới User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mối quan hệ Many-to-One với FoodItem.
     * Một FoodLog ghi lại một FoodItem.
     * @JoinColumn(name = "food_item_id"): Khóa ngoại liên kết tới FoodItem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
}