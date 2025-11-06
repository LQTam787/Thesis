package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EMealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * Thể hiện một Bữa ăn (ví dụ: Bữa sáng, Bữa trưa, Ăn nhẹ).
 * Ánh xạ tới bảng "meals".
 */
@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Loại bữa ăn (ví dụ: BREAKFAST, LUNCH).
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi.
     */
    @Enumerated(EnumType.STRING)
    private EMealType mealType;

    /**
     * Thời gian dự kiến của bữa ăn.
     */
    private LocalTime time;

    /**
     * Mối quan hệ Many-to-One với DailyMenu.
     * Một Meal thuộc về một DailyMenu.
     * @JoinColumn(name = "daily_menu_id"): Khóa ngoại liên kết tới DailyMenu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_menu_id")
    private DailyMenu dailyMenu;

    /**
     * Mối quan hệ One-to-Many với MealFoodItem.
     * Một Meal có thể có nhiều thành phần FoodItem thông qua bảng liên kết MealFoodItem.
     * cascade = CascadeType.ALL, orphanRemoval = true: Quản lý vòng đời của các MealFoodItem.
     */
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealFoodItem> mealFoodItems;
}