package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Thể hiện người dùng của ứng dụng.
 * Đây là thực thể cốt lõi cho việc xác thực (Authentication).
 * Ánh xạ tới bảng "users".
 *
 * Nguyên lý hoạt động:
 * - Là thực thể JPA (@Entity) ánh xạ tới bảng "users".
 * - Đảm bảo tính duy nhất của username và email thông qua @UniqueConstraint.
 * - Quản lý mối quan hệ Many-to-Many với Role để xác định quyền hạn người dùng.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                // Đảm bảo cột username là duy nhất.
                @UniqueConstraint(columnNames = "username"),
                // Đảm bảo cột email là duy nhất.
                @UniqueConstraint(columnNames = "email")
        })
@Data // Lombok: Tự động tạo getter, setter, toString, equals, và hashCode.
@NoArgsConstructor // Lombok: Tự động tạo constructor không đối số.
@AllArgsConstructor // Lombok: Tự động tạo constructor với tất cả các trường.
@Builder // Lombok: Tự động tạo builder pattern.
public class User {
    /**
     * Khóa chính.
     * @Id: Đánh dấu là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Cấu hình chiến lược tạo giá trị ID tự động (thường là tự tăng).
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
     *
     * Luồng hoạt động:
     * - fetch = FetchType.LAZY: Tải tập hợp Role chỉ khi cần thiết để tối ưu hiệu suất truy vấn.
     * - @JoinTable: Xác định bảng liên kết trung gian "user_roles" để quản lý mối quan hệ Many-to-Many.
     * - joinColumns: Khóa ngoại trỏ từ bảng liên kết tới bảng User.
     * - inverseJoinColumns: Khóa ngoại trỏ từ bảng liên kết tới bảng Role.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor được sử dụng để tạo một User mới (thường là trong quá trình đăng ký).
     *
     * Luồng hoạt động:
     * - Được gọi khi tạo một đối tượng User mới với thông tin cơ bản: username, email, và mật khẩu.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(); // Khởi tạo roles để tránh NullPointerException
    }
}