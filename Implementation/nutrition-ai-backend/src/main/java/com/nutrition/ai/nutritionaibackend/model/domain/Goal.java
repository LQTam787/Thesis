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
 */
@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi trong CSDL.
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
     * @JoinColumn(name = "user_id"): Khóa ngoại liên kết tới User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mục tiêu dinh dưỡng bằng ngôn ngữ tự nhiên.
     */
    private String nutritionGoal;
}