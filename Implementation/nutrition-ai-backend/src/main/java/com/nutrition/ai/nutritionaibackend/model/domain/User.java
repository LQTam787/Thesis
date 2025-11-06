package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Thể hiện người dùng của ứng dụng.
 * Đây là thực thể cốt lõi cho việc xác thực (Authentication).
 * Ánh xạ tới bảng "users".
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                // Đảm bảo cột username là duy nhất.
                @UniqueConstraint(columnNames = "username"),
                // Đảm bảo cột email là duy nhất.
                @UniqueConstraint(columnNames = "email")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Khóa chính.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên đăng nhập (phải là duy nhất).
     */
    private String username;

    /**
     * Email (phải là duy nhất).
     */
    private String email;

    /**
     * Mật khẩu (thường được lưu trữ dưới dạng mã hóa).
     */
    private String password;

    /**
     * Mối quan hệ Many-to-Many với Role.
     * Một User có thể có nhiều Role, và một Role có thể có nhiều User.
     * fetch = FetchType.LAZY: Tải tập hợp Role chỉ khi cần thiết.
     * @JoinTable: Xác định bảng liên kết trung gian "user_roles" để quản lý mối quan hệ.
     * - joinColumns: Khóa ngoại trỏ từ bảng liên kết tới bảng User.
     * - inverseJoinColumns: Khóa ngoại trỏ từ bảng liên kết tới bảng Role.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor được sử dụng để tạo một User mới (thường là trong quá trình đăng ký).
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}