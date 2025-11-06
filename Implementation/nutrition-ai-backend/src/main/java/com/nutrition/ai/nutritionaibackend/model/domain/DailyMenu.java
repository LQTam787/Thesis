package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Thể hiện thực đơn dinh dưỡng cho một ngày cụ thể trong một Kế hoạch Dinh dưỡng.
 * Ánh xạ tới bảng "daily_menus".
 */
@Entity
@Table(name = "daily_menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMenu {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ngày áp dụng thực đơn này.
     */
    private LocalDate date;

    /**
     * Mối quan hệ Many-to-One với NutritionPlan.
     * Một DailyMenu thuộc về một NutritionPlan.
     * @JoinColumn(name = "nutrition_plan_id"): Khóa ngoại liên kết tới NutritionPlan.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan;

    /**
     * Mối quan hệ One-to-Many với Meal.
     * Một DailyMenu có thể có nhiều Meal (Bữa ăn: Sáng, Trưa, Tối, v.v.).
     * cascade = CascadeType.ALL: Mọi thao tác (persist, merge, remove) trên DailyMenu sẽ áp dụng cho các Meal liên quan.
     * orphanRemoval = true: Nếu một Meal bị xóa khỏi danh sách này, nó sẽ bị xóa khỏi CSDL.
     */
    @OneToMany(mappedBy = "dailyMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;
}