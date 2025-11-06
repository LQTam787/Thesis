package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for updating user details.
 * Nguyên lý hoạt động: Đóng gói các trường dữ liệu người dùng có thể cập nhật.
 * Luồng hoạt động: Được gửi từ client đến server khi người dùng hoặc quản trị viên cập nhật thông tin người dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    private String username; // Tên đăng nhập mới (nếu được phép cập nhật).
    private String email; // Email mới.
    private Set<String> roles; // Tập hợp các vai trò mới của người dùng (thường chỉ dành cho quản trị viên).
}