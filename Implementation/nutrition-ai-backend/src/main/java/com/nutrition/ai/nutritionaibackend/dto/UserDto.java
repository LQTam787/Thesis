package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for User entity.
 * Nguyên lý hoạt động: Đóng gói tất cả thông tin người dùng, bao gồm cả mật khẩu (thường được dùng nội bộ trong tầng service/repository hoặc cho đăng ký).
 * Luồng hoạt động: Được sử dụng để tạo người dùng mới (đăng ký) hoặc chuyển dữ liệu người dùng đầy đủ giữa các tầng back-end.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id; // ID duy nhất của người dùng.
    private String username; // Tên đăng nhập.
    private String email; // Email.
    private String password; // Mật khẩu (nên được băm trước khi lưu trữ).
    private Set<String> roles; // Tập hợp các vai trò của người dùng.
}