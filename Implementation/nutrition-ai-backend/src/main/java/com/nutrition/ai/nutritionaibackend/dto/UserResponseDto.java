package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for User response.
 * Nguyên lý hoạt động: Đóng gói thông tin người dùng an toàn để trả về client (KHÔNG BAO GỒM MẬT KHẨU).
 * Luồng hoạt động: Được gửi từ server đến client sau khi đăng nhập, đăng ký thành công, hoặc khi client yêu cầu thông tin hồ sơ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id; // ID duy nhất của người dùng.
    private String username; // Tên đăng nhập.
    private String email; // Email.
    private Set<String> roles; // Tập hợp các vai trò của người dùng.
}