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

    /**
     * Luồng hoạt động: Khởi tạo service.
     * Nguyên lý hoạt động: Dependency Injection thông qua Constructor. Spring sẽ tự động cung cấp các bean
     * UserRepository và PasswordEncoder đã được cấu hình.
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // Luồng hoạt động: Tìm kiếm User theo username.
        // Nguyên lý hoạt động: Ủy quyền trực tiếp cho JpaRepository/UserRepository để truy vấn DB.
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        // Luồng hoạt động: Kiểm tra sự tồn tại của username.
        // Nguyên lý hoạt động: Tương tự findByUsername, ủy quyền cho Repository, thường dùng trong đăng ký.
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        // Luồng hoạt động: Kiểm tra sự tồn tại của email.
        // Nguyên lý hoạt động: Ủy quyền cho Repository.
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        // Luồng hoạt động: Lưu (hoặc cập nhật) User.
        // Nguyên lý hoạt động: Ủy quyền cho Repository. Nếu User có ID, nó sẽ là cập nhật (UPDATE); nếu không, nó sẽ là tạo mới (INSERT).
        return userRepository.save(user);
    }

    @Override
    public boolean authenticate(String username, String password) {
        // Luồng hoạt động: Xác thực người dùng.
        // Nguyên lý hoạt động:
        // 1. Tìm User theo username.
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 2. Nếu tìm thấy, sử dụng PasswordEncoder để so sánh mật khẩu thô (`password`) với mật khẩu đã mã hóa (`user.getPassword()`) trong DB.
            //    Hàm `matches` xử lý quá trình giải mã (hoặc so sánh hash) một cách an toàn.
            return passwordEncoder.matches(password, user.getPassword());
        }
        // 3. Nếu không tìm thấy username, xác thực thất bại.
        return false;
    }

    @Override
    public UserDto registerNewUserAccount(UserDto userDto) throws Exception {
        // Luồng hoạt động: Đăng ký tài khoản mới.
        // Nguyên lý hoạt động (Kiểm tra và Mã hóa):
        // 1. Kiểm tra username và email đã tồn tại chưa. Nếu có, ném ngoại lệ để ngăn đăng ký trùng lặp.
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("Email already exists");
        }

        // 2. Chuyển đổi DTO sang Entity.
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // 3. Mã hóa mật khẩu (`userDto.getPassword()`) bằng `passwordEncoder.encode()` trước khi lưu.
        //    Đây là bước bảo mật quan trọng nhất.
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 4. Lưu User mới vào DB.
        User savedUser = userRepository.save(user);

        // 5. Chuyển đổi User entity đã lưu sang DTO để trả về.
        UserDto returnedDto = new UserDto();
        returnedDto.setUsername(savedUser.getUsername());
        returnedDto.setEmail(savedUser.getEmail());
        // Nguyên lý hoạt động: KHÔNG trả về mật khẩu (dù đã mã hóa) trong DTO để đảm bảo an toàn thông tin.

        return returnedDto;
    }
}