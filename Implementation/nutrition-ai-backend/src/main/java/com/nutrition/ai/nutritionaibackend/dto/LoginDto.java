package com.nutrition.ai.nutritionaibackend.dto;

import lombok.Data;

/**
 * Data Transfer Object for user login.
 * Nguyên lý hoạt động: Đóng gói thông tin xác thực (username và password) cần thiết cho quá trình đăng nhập.
 * Luồng hoạt động: Được gửi từ ứng dụng khách (client) đến server để xác thực người dùng.
 */
@Data // Tự động tạo getters và setters.
public class LoginDto {
    private String username; // Tên đăng nhập của người dùng.
    private String password; // Mật khẩu của người dùng.
}