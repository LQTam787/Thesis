package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Role entity.
 * Nguyên lý hoạt động: Đóng gói thông tin về vai trò/quyền hạn của người dùng trong hệ thống.
 * Luồng hoạt động: Được sử dụng trong quá trình xác thực và ủy quyền để truyền thông tin về vai trò.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Integer id; // ID duy nhất của vai trò.
    private ERole name; // Tên của vai trò (ví dụ: ROLE_USER, ROLE_ADMIN). Nguyên lý hoạt động: Sử dụng enum để chuẩn hóa vai trò.
}