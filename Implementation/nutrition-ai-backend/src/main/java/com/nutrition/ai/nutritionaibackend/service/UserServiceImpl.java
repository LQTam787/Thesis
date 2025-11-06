package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Service Implementation for managing {@link User}.
 * Thực hiện logic nghiệp vụ cho các thao tác với User, đặc biệt là xác thực và đăng ký, có sử dụng PasswordEncoder để bảo mật.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Nguyên lý hoạt động: Dependency Injection thông qua Constructor
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // Luồng hoạt động: Tìm kiếm User theo username.
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        // Luồng hoạt động: Kiểm tra sự tồn tại của username.
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        // Luồng hoạt động: Kiểm tra sự tồn tại của email.
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        // Luồng hoạt động: Lưu (hoặc cập nhật) User.
        return userRepository.save(user);
    }

    @Override
    public boolean authenticate(String username, String password) {
        // Luồng hoạt động: Xác thực người dùng.
        // Nguyên lý hoạt động:
        // 1. Tìm User theo username.
        // 2. Nếu tìm thấy, sử dụng PasswordEncoder (thường là Bcrypt) để so sánh mật khẩu thô (`password`) với mật khẩu đã mã hóa (`user.getPassword()`) trong DB.
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Override
    public UserDto registerNewUserAccount(UserDto userDto) throws Exception {
        // Luồng hoạt động: Đăng ký tài khoản mới.
        // Nguyên lý hoạt động (Kiểm tra và Mã hóa):
        // 1. Kiểm tra username và email đã tồn tại chưa. Nếu có, ném ngoại lệ.
        // 2. Tạo User entity.
        // 3. Mã hóa mật khẩu (`userDto.getPassword()`) bằng `passwordEncoder.encode()` trước khi lưu.
        // 4. Lưu User vào DB.
        // 5. Chuyển đổi User entity đã lưu sang DTO để trả về (không bao gồm mật khẩu).
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        // Convert saved user back to DTO to return
        UserDto returnedDto = new UserDto();
        returnedDto.setUsername(savedUser.getUsername());
        returnedDto.setEmail(savedUser.getEmail());
        // Do not return the password in the DTO

        return returnedDto;
    }
}