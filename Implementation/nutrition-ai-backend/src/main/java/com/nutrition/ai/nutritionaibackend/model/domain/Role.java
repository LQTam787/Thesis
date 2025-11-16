package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp `Role` đại diện cho các vai trò của người dùng trong hệ thống (ví dụ: `ROLE_USER`, `ROLE_ADMIN`).
 * Nó là một Entity JPA, ánh xạ tới bảng "roles" trong cơ sở dữ liệu.
 * Lớp này là một phần thiết yếu của hệ thống xác thực và ủy quyền, định nghĩa các quyền hạn khác nhau cho người dùng.
 *
 * Logic hoạt động:
 * - Định nghĩa các vai trò:
 *   - Mỗi `Role` có một ID duy nhất (`id`) và `name` (loại `ERole` enum).
 *   - Mục đích là để phân loại người dùng và gán cho họ các quyền truy cập và chức năng khác nhau trong ứng dụng.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "roles")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `roles`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Xử lý tên vai trò:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `ERole` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu, giúp dễ đọc và linh hoạt hơn.
 *   - `@Column(length = 20)` giới hạn độ dài của cột `name` trong cơ sở dữ liệu để đảm bảo tính toàn vẹn và hiệu quả lưu trữ.
 * - Constructor:
 *   - Cung cấp một constructor tiện lợi để tạo `Role` chỉ với `ERole`.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - Các `Role` thường được định nghĩa trước và không thay đổi thường xuyên. Người dùng được gán `Role` khi đăng ký hoặc thông qua quản trị viên.
 * - Truy vấn:
 *   - Khi truy vấn một `Role`, `id` và `name` của vai trò được tải.
 *   - Các vai trò này được sử dụng bởi hệ thống bảo mật (ví dụ: Spring Security) để kiểm tra quyền của người dùng khi truy cập các tài nguyên hoặc chức năng nhất định.
 *   - Thông tin vai trò có thể được liên kết với `User` thông qua mối quan hệ Many-to-Many.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    /**
     * `id` là khóa chính duy nhất cho mỗi vai trò. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Role` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * `name` là tên của vai trò (ví dụ: `ROLE_USER`, `ROLE_ADMIN`).
     * @Enumerated(EnumType.STRING) đảm bảo rằng giá trị enum `ERole` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu,
     * giúp tăng khả năng đọc và bảo trì dữ liệu.
     * @Column(length = 20) giới hạn độ dài của cột trong cơ sở dữ liệu để đảm bảo tính toàn vẹn dữ liệu.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     * Constructor để tạo một đối tượng `Role` chỉ với tên vai trò.
     * @param name Loại vai trò (`ERole`) để gán cho đối tượng `Role` mới.
     */
    public Role(ERole name) {
        this.name = name;
    }
}