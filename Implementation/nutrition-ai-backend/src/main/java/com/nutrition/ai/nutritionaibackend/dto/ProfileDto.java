package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Profile entity.
 * Nguyên lý hoạt động: Đóng gói thông tin chi tiết về hồ sơ cá nhân và thể chất của người dùng.
 * Luồng hoạt động: Được sử dụng khi người dùng cập nhật hồ sơ hoặc khi hệ thống cần tính toán mục tiêu/nhu cầu dinh dưỡng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id; // ID duy nhất của hồ sơ.
    private String fullName; // Tên đầy đủ của người dùng.
    private LocalDate dateOfBirth; // Ngày sinh.
    private EGender gender; // Giới tính (ví dụ: MALE, FEMALE). Nguyên lý hoạt động: Sử dụng enum để chuẩn hóa.
    private Double height; // Chiều cao (ví dụ: cm).
    private Double weight; // Cân nặng hiện tại.
    private String activityLevel; // Mức độ hoạt động (ví dụ: "Ít vận động", "Vận động nhẹ").
    private String allergies; // Thông tin về các dị ứng thực phẩm.
    private String medicalConditions; // Các tình trạng sức khỏe/bệnh lý.
    private Long userId; // ID của người dùng mà hồ sơ này thuộc về.
}