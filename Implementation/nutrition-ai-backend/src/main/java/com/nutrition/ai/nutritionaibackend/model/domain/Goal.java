package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Thể hiện mục tiêu của người dùng (ví dụ: giảm cân, tăng cơ).
 * Ánh xạ tới bảng "goals".
 *
 * Nguyên lý hoạt động:
 * - Là thực thể JPA (@Entity) ánh xạ tới bảng "goals".
 * - Lưu trữ chi tiết mục tiêu như cân nặng mục tiêu, ngày dự kiến, và loại mục tiêu (EGoalType).
 * - Liên kết với User (@ManyToOne) để biết mục tiêu thuộc về ai.
 */
@Entity
@Table(name = "goals")
@Data // Lombok: Tự động tạo getter, setter, toString, equals, và hashCode.
@NoArgsConstructor // Lombok: Tự động tạo constructor không đối số.
@AllArgsConstructor // Lombok: Tự động tạo constructor với tất cả các trường.
public class Goal {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cân nặng mục tiêu (nếu mục tiêu liên quan đến cân nặng).
     */
    private Double targetWeight;

    /**
     * Ngày dự kiến đạt được mục tiêu.
     */
    private LocalDate targetDate;

    /**
     * Loại mục tiêu (ví dụ: INCREASE_WEIGHT, DECREASE_WEIGHT).
     *
     * Luồng hoạt động:
     * - @Enumerated(EnumType.STRING): Chỉ thị cho JPA lưu giá trị enum dưới dạng chuỗi (tên) trong CSDL thay vì số thứ tự.
     */
    @Enumerated(EnumType.STRING)
    private EGoalType goalType;

    /**
     * Trạng thái của mục tiêu (ví dụ: "Active", "Completed").
     */
    private String status;

    /**
     * Mối quan hệ Many-to-One với User.
     * Một Goal thuộc về một User.
     *
     * Luồng hoạt động:
     * - @ManyToOne: Thiết lập mối quan hệ N:1.
     * - fetch = FetchType.LAZY: Tải đối tượng User chỉ khi truy cập (tối ưu hiệu suất).
     * - @JoinColumn(name = "user_id"): Chỉ định cột khóa ngoại "user_id" trong bảng "goals" liên kết tới bảng "users".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên.
     */
    private String nutritionGoal;
}