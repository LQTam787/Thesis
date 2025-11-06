package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Thể hiện một nhật ký ghi lại việc người dùng đã thực hiện một hoạt động cụ thể.
 * Ánh xạ tới bảng "activity_logs".
 */
@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Thời gian thực hiện hoạt động.
     */
    private LocalDateTime logDate;

    /**
     * Thời lượng hoạt động tính bằng phút.
     */
    private Integer durationInMinutes;

    /**
     * Tổng lượng calo đốt cháy ước tính cho lần thực hiện này.
     */
    private Double caloriesBurned;

    /**
     * Mối quan hệ Many-to-One với User.
     * Nhiều ActivityLog thuộc về một User.
     * @JoinColumn(name = "user_id"): Tạo khóa ngoại 'user_id' trong bảng "activity_logs".
     * fetch = FetchType.LAZY: Tải User chỉ khi được truy cập (tối ưu hiệu suất).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Mối quan hệ Many-to-One với Activity.
     * Nhiều ActivityLog tham chiếu đến một Activity.
     * @JoinColumn(name = "activity_id"): Tạo khóa ngoại 'activity_id'.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;
}