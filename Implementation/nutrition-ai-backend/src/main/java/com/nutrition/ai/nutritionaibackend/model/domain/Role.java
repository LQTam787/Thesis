package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thể hiện Vai trò của người dùng (ví dụ: ROLE_USER, ROLE_ADMIN).
 * Đây là một thực thể cơ sở cho hệ thống xác thực và ủy quyền.
 * Ánh xạ tới bảng "roles".
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Tên Vai trò.
     * @Enumerated(EnumType.STRING): Lưu giá trị enum dưới dạng chuỗi.
     * @Column(length = 20): Giới hạn độ dài của cột trong CSDL.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}