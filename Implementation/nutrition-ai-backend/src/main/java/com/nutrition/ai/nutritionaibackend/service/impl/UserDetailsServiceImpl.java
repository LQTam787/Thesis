package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Spring Security's UserDetailsService interface.
 * Responsible for retrieving user authentication and authorization details from the database.
 * Lớp này triển khai giao diện UserDetailsService của Spring Security.
 * Nhiệm vụ của nó là tải thông tin chi tiết về người dùng (UserDetails) bằng tên người dùng
 * (username) để sử dụng trong quá trình xác thực (authentication).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inject UserRepository để truy cập dữ liệu người dùng
    private final UserRepository userRepository;

    /**
     * Tải thông tin người dùng (UserDetails) bằng tên người dùng (username).
     * 1. Tìm User Entity bằng username trong cơ sở dữ liệu.
     * 2. Ném UsernameNotFoundException nếu không tìm thấy User.
     * 3. Chuyển đổi User Entity thành UserDetailsImpl và trả về.
     *
     * @param username Tên người dùng để tìm kiếm.
     * @return UserDetails object chứa thông tin người dùng và quyền hạn.
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng.
     */
    @Override
    @Transactional // Đảm bảo toàn bộ quá trình tải được thực hiện trong một transaction
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm User bằng username
        User user = userRepository.findByUsername(username)
                // 2. Xử lý trường hợp không tìm thấy
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // 3. Xây dựng và trả về UserDetailsImpl
        return UserDetailsImpl.build(user);
    }
}