package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.Optional;

/**
 * Service Interface for managing {@link User}.
 * Định nghĩa các API nghiệp vụ cho User, bao gồm tìm kiếm, kiểm tra tồn tại, lưu, xác thực và đăng ký.
 */
public interface UserService {

    /**
     * Luồng hoạt động: Tìm kiếm người dùng bằng tên đăng nhập (username).
     * Nguyên lý hoạt động: Truy vấn DB, thường được sử dụng trong quá trình xác thực/đăng nhập.
     *
     * @param username the username of the user.
     * @return an Optional containing the user, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Luồng hoạt động: Kiểm tra sự tồn tại của người dùng bằng tên đăng nhập.
     * Nguyên lý hoạt động: Truy vấn DB, thường được sử dụng trong quá trình đăng ký để tránh trùng lặp.
     *
     * @param username the username to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Luồng hoạt động: Kiểm tra sự tồn tại của người dùng bằng email.
     * Nguyên lý hoạt động: Truy vấn DB, thường được sử dụng trong quá trình đăng ký.
     *
     * @param email the email to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Luồng hoạt động: Lưu (hoặc cập nhật) một người dùng.
     * Nguyên lý hoạt động: Uỷ quyền cho UserRepository.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    User save(User user);

    /**
     * Luồng hoạt động: Xác thực người dùng bằng tên đăng nhập và mật khẩu.
     * Nguyên lý hoạt động: Tìm User theo username, sau đó so sánh mật khẩu được cung cấp với mật khẩu đã mã hóa (hashed) trong DB.
     *
     * @param username the username.
     * @param password the password.
     * @return true if authentication is successful, false otherwise.
     */
    boolean authenticate(String username, String password);

    /**
     * Luồng hoạt động: Đăng ký một tài khoản người dùng mới.
     * Nguyên lý hoạt động:
     * 1. Kiểm tra username/email đã tồn tại chưa.
     * 2. Mã hóa mật khẩu.
     * 3. Chuyển đổi DTO sang Entity.
     * 4. Lưu User mới vào DB.
     *
     * @param userDto the user data transfer object.
     * @return the created user.
     */
    UserDto registerNewUserAccount(UserDto userDto) throws Exception;
}