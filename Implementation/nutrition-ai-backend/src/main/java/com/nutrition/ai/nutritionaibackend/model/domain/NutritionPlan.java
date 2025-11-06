package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Thể hiện Kế hoạch Dinh dưỡng tổng thể của người dùng.
 * Ánh xạ tới bảng "nutrition_plans".
 */
@Entity
@Table(name = "nutrition_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlan {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên của kế hoạch.
     */
    private String planName;

    /**
     * Ngày bắt đầu của kế hoạch.
     */
    private LocalDate startDate;

    /**
     * Ngày kết thúc của kế hoạch.
     */
    private LocalDate endDate;

    /**
     * Mục tiêu Calo tổng thể cho mỗi ngày theo kế hoạch.
     */
    private Integer totalCaloriesGoal;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một NutritionPlan thuộc về một User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mối quan hệ One-to-Many với DailyMenu.
     * Một NutritionPlan bao gồm nhiều DailyMenu (thực đơn hàng ngày).
     * cascade = CascadeType.ALL, orphanRemoval = true: Quản lý vòng đời của các DailyMenu.
     */
    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyMenu> dailyMenus;
}