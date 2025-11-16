package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Lớp `User` đại diện cho người dùng của ứng dụng. Đây là thực thể cốt lõi cho việc xác thực và quản lý tài khoản.
 * Nó là một Entity JPA, ánh xạ tới bảng "users" trong cơ sở dữ liệu.
 * `User` chứa thông tin đăng nhập (username, email, password) và quản lý các vai trò (`Role`) của người dùng.
 *
 * Logic hoạt động:
 * - Quản lý thông tin đăng nhập:
 *   - Mỗi `User` có một ID duy nhất (`id`), `username` và `email` (đều là duy nhất), và `password` (thường được mã hóa).
 *   - `@UniqueConstraint` trên `username` và `email` đảm bảo không có hai người dùng nào có cùng tên đăng nhập hoặc email.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "users")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `users`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` từ Lombok giúp giảm boilerplate code và cung cấp cách tạo đối tượng linh hoạt.
 * - Mối quan hệ với các vai trò:
 *   - `@ManyToMany` với `Role`: Một `User` có thể có nhiều `Role`, và một `Role` có thể được gán cho nhiều `User`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải tập hợp `Role` khi chúng thực sự được truy cập.
 *   - `@JoinTable` định nghĩa bảng liên kết trung gian "user_roles" để quản lý mối quan hệ Many-to-Many, với các khóa ngoại `user_id` và `role_id`.
 *   - `@Builder.Default` và khởi tạo `new HashSet<>()` đảm bảo `roles` luôn là một tập hợp không null.
 * - Constructor tiện lợi:
 *   - Cung cấp một constructor để tạo `User` với `username`, `email`, và `password`, đồng thời khởi tạo tập hợp `roles`.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `User` được tạo khi người dùng đăng ký tài khoản mới. `username`, `email` và `password` được cung cấp.
 *   - Các `Role` được gán cho người dùng, thường là `ROLE_USER` mặc định.
 *   - `password` được mã hóa trước khi lưu vào cơ sở dữ liệu.
 * - Truy vấn:
 *   - Khi truy vấn một `User`, các thông tin đăng nhập và các vai trò (`roles`) của người dùng được tải (hoặc tải `LAZY` cho `roles`).
 *   - Thông tin này được sử dụng để xác thực người dùng khi họ đăng nhập và để kiểm tra quyền hạn của họ khi truy cập các chức năng của ứng dụng.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * `id` là khóa chính duy nhất cho mỗi người dùng. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `User` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `username` là tên đăng nhập của người dùng. Trường này phải là duy nhất trên toàn hệ thống
     * (@UniqueConstraint) và được sử dụng để xác định người dùng trong quá trình đăng nhập.
     */
    private String username;

    /**
     * `email` là địa chỉ email của người dùng. Trường này cũng phải là duy nhất (@UniqueConstraint)
     * và thường được sử dụng cho việc khôi phục mật khẩu hoặc thông báo.
     */
    private String email;

    /**
     * `password` là mật khẩu của người dùng. Mật khẩu phải được mã hóa mạnh mẽ trước khi lưu vào cơ sở dữ liệu
     * để đảm bảo an toàn. Trường này được sử dụng để xác thực người dùng.
     */
    private String password;

    /**
     * `roles` là một tập hợp các `Role` được gán cho người dùng.
     * Mối quan hệ `@ManyToMany` chỉ ra rằng một `User` có thể có nhiều `Role`, và một `Role` có thể được gán cho nhiều `User`.
     * `fetch = FetchType.LAZY` đảm bảo rằng tập hợp các `Role` chỉ được tải khi nó được truy cập lần đầu, tối ưu hiệu suất.
     * `@JoinTable` định nghĩa bảng liên kết trung gian "user_roles" với các khóa ngoại `user_id` và `role_id`
     * để quản lý mối quan hệ này.
     * `@Builder.Default` và khởi tạo `new HashSet<>()` đảm bảo rằng `roles` luôn là một tập hợp không rỗng khi sử dụng Builder pattern.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor để tạo một đối tượng `User` mới với các thông tin cơ bản: tên đăng nhập, email và mật khẩu.
     * Tập hợp `roles` được khởi tạo rỗng và sẽ được gán các vai trò mặc định sau đó (ví dụ: `ROLE_USER`).
     * @param username Tên đăng nhập của người dùng.
     * @param email Địa chỉ email của người dùng.
     * @param password Mật khẩu của người dùng (thường đã được mã hóa).
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(); // Khởi tạo roles để tránh NullPointerException
    }
}