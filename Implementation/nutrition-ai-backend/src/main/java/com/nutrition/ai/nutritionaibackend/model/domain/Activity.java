package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Thể hiện một hoạt động thể chất chung (ví dụ: Chạy bộ, Yoga).
 * Đây là một Entity (thực thể) được ánh xạ tới bảng "activities" trong CSDL.
 */
@Entity
@Table(name = "activities")
@Data // Lombok: Tự động tạo getters, setters, equals, hashCode, và toString.
@NoArgsConstructor // Lombok: Tự động tạo constructor không tham số.
@AllArgsConstructor // Lombok: Tự động tạo constructor có tất cả tham số.
public class Activity {
    /**
     * Khóa chính của thực thể Activity.
     * @Id: Đánh dấu là khóa chính.
     * @GeneratedValue: Chỉ định chiến lược tạo giá trị (IDENTITY - tự động tăng).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên của hoạt động (ví dụ: "Chạy bộ").
     */
    private String name;

    /**
     * Lượng calo đốt cháy mỗi giờ cho hoạt động này (giá trị trung bình).
     */
    private Double caloriesBurnedPerHour;

    /**
     * Mối quan hệ One-to-Many với ActivityLog.
     * Một Activity có thể có nhiều ActivityLog (nhiều lần người dùng thực hiện hoạt động này).
     * mappedBy="activity": Cho biết trường 'activity' trong class ActivityLog quản lý mối quan hệ.
     */
    @OneToMany(mappedBy = "activity")
    private List<ActivityLog> activityLogs;
}