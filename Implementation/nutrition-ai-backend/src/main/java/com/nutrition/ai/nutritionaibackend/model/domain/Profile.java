package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Thể hiện hồ sơ/thông tin chi tiết của người dùng.
 * Đây là mối quan hệ One-to-One với thực thể User.
 * Ánh xạ tới bảng "profiles".
 */
@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    /**
     * Khóa chính, chia sẻ khóa chính với User (Shared Primary Key).
     * @Id: Đánh dấu là khóa chính.
     */
    @Id
    private Long id;

    /**
     * Tên đầy đủ của người dùng.
     */
    private String fullName;

    /**
     * Ngày sinh.
     */
    private LocalDate dateOfBirth;

    /**
     * Giới tính.
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi.
     */
    @Enumerated(EnumType.STRING)
    private EGender gender;

    /**
     * Chiều cao (cm/inch).
     */
    private Double height;

    /**
     * Cân nặng hiện tại (kg/lbs).
     */
    private Double weight;

    /**
     * Mức độ hoạt động thể chất (ví dụ: "Ít vận động", "Vận động nhẹ").
     */
    private String activityLevel;

    /**
     * Thông tin dị ứng (có thể là chuỗi dài).
     * @Lob: Chỉ định trường này là LOB (Large Object - ví dụ: TEXT, CLOB trong CSDL).
     */
    @Lob
    private String allergies;

    /**
     * Thông tin về tình trạng y tế (có thể là chuỗi dài).
     * @Lob: Chỉ định trường này là LOB.
     */
    @Lob
    private String medicalConditions;

    /**
     * Mối quan hệ One-to-One với User.
     * @MapsId: Chỉ định rằng khóa chính của thực thể này (id) được lấy từ thực thể liên kết (User).
     * @JoinColumn(name = "id"): Khóa ngoại và khóa chính liên kết tới bảng User.
     * fetch = FetchType.LAZY: Tải User chỉ khi được truy cập.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}