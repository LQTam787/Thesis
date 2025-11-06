package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository<User, Long>.
 * 2. Phương thức truy vấn: Các phương thức này được tự động tạo truy vấn dựa trên tên:
 * - findByUsername: SELECT * FROM User WHERE username = ? (Trả về Optional để xử lý null an toàn).
 * - existsByUsername: SELECT COUNT(*) FROM User WHERE username = ? (Trả về boolean).
 * - existsByEmail: SELECT COUNT(*) FROM User WHERE email = ? (Trả về boolean).
 * 3. Mục đích: Cung cấp các thao tác liên quan đến đăng nhập và đăng ký (kiểm tra sự tồn tại của người dùng/email).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check.
     * @return true if a user with the given username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check.
     * @return true if a user with the given email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}