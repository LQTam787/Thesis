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
 *
 * Nguyên lý hoạt động:
 * - Là thực thể JPA (@Entity) ánh xạ tới bảng "nutrition_plans".
 * - Lưu trữ thông tin chung của kế hoạch (tên, ngày, mục tiêu).
 * - Liên kết với User (@ManyToOne) và quản lý danh sách DailyMenu (@OneToMany).
 */
@Entity
@Table(name = "nutrition_plans")
@Data // Lombok: Tự động tạo getter, setter, toString, equals, và hashCode.
@NoArgsConstructor // Lombok: Tự động tạo constructor không đối số.
@AllArgsConstructor // Lombok: Tự động tạo constructor với tất cả các trường.
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
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên cho kế hoạch này.
     */
    private String nutritionGoal;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một NutritionPlan thuộc về một User.
     *
     * Luồng hoạt động:
     * - @ManyToOne: Thiết lập mối quan hệ N:1.
     * - fetch = FetchType.LAZY: Tải đối tượng User chỉ khi truy cập.
     * - @JoinColumn(name = "user_id"): Chỉ định cột khóa ngoại "user_id" liên kết tới User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mối quan hệ One-to-Many với DailyMenu.
     * Một NutritionPlan bao gồm nhiều DailyMenu (thực đơn hàng ngày).
     *
     * Luồng hoạt động:
     * - @OneToMany: Thiết lập mối quan hệ 1:N.
     * - mappedBy = "nutritionPlan": Cho biết trường "nutritionPlan" trong thực thể DailyMenu quản lý mối quan hệ (tránh tạo bảng liên kết trung gian).
     * - cascade = CascadeType.ALL: Bất kỳ thao tác nào (như persist, remove) trên NutritionPlan cũng được áp dụng cho DailyMenu liên quan (quản lý vòng đời).
     * - orphanRemoval = true: Nếu một DailyMenu bị loại bỏ khỏi danh sách `dailyMenus`, nó sẽ tự động bị xóa khỏi CSDL.
     */
    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyMenu> dailyMenus;
}