package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for JWT response after successful login.
 * Nguyên lý hoạt động: Đóng gói JWT token và thông tin người dùng để gửi về client sau khi xác thực thành công.
 * Luồng hoạt động: Được gửi từ server đến ứng dụng khách (client) sau khi đăng nhập thành công.
 */
@Data // Tự động tạo getters và setters
@AllArgsConstructor // Tự động tạo constructor với tất cả fields
public class JwtResponseDto {
    private String token; // JWT token để xác thực các request tiếp theo
    private String type; // Loại token (thường là "Bearer")
    private Long id; // ID của người dùng
    private String username; // Tên đăng nhập
    private String email; // Email của người dùng

    /**
     * Constructor dự phòng chỉ với token (cho các trường hợp đơn giản).
     */
    public JwtResponseDto(String token) {
        this.token = token;
        this.type = "Bearer";
    }
}

