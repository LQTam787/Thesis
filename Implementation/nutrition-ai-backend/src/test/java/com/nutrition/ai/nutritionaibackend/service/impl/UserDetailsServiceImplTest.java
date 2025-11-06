package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lớp kiểm thử đơn vị (Unit Test) cho UserDetailsServiceImpl.
 * Sử dụng Mockito để mô phỏng (mock) UserRepository.
 */
class UserDetailsServiceImplTest {

    // Nguyên lý: Dùng @Mock để tạo một đối tượng giả (mock) cho UserRepository.
    // Đối tượng giả này sẽ thay thế đối tượng thật trong quá trình kiểm thử,
    // cho phép chúng ta kiểm soát hành vi của nó (ví dụ: nó trả về gì).
    @Mock
    private UserRepository userRepository;

    // Nguyên lý: Dùng @InjectMocks để tạo một đối tượng UserDetailsServiceImpl
    // và tự động tiêm các đối tượng @Mock (ở đây là userRepository) vào đó.
    // Đây là đối tượng "dưới kiểm thử" (System Under Test - SUT).
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Phương thức thiết lập chạy trước mỗi phương thức @Test.
     * Luồng hoạt động: Gọi MockitoAnnotations.openMocks(this) để khởi tạo
     * tất cả các đối tượng được đánh dấu bằng @Mock và @InjectMocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Kiểm thử trường hợp người dùng được tìm thấy.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng User giả lập.
     * 2. Thiết lập hành vi của Mock: Khi userRepository.findByUsername("testuser") được gọi,
     * nó phải trả về Optional chứa đối tượng User giả lập.
     * 3. Gọi phương thức đang kiểm thử: userDetailsService.loadUserByUsername("testuser").
     * 4. Xác nhận kết quả:
     * - Đối tượng UserDetails không phải là null.
     * - Tên người dùng trong UserDetails khớp với tên đã cung cấp.
     * 5. Xác minh (Verification): Đảm bảo rằng phương thức findByUsername đã được gọi ĐÚNG 1 lần.
     */
    @Test
    void testLoadUserByUsername_UserFound() {
        // 1. Chuẩn bị dữ liệu (Arrange)
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(java.util.Collections.emptySet()); // Sửa lỗi: Cung cấp một tập hợp rỗng cho vai trò

        // 2. Thiết lập hành vi Mock (Arrange)
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // 3. Thực thi hành động (Act)
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // 4. Xác nhận kết quả (Assert)
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());

        // 5. Xác minh tương tác (Verify)
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    /**
     * Kiểm thử trường hợp người dùng KHÔNG được tìm thấy.
     * Nguyên lý: loadUserByUsername() theo chuẩn Spring Security phải ném
     * UsernameNotFoundException nếu người dùng không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi của Mock: Khi userRepository.findByUsername("nonexistent") được gọi,
     * nó phải trả về Optional.empty().
     * 2. Xác nhận ngoại lệ: Sử dụng assertThrows để kiểm tra xem khi gọi
     * userDetailsService.loadUserByUsername("nonexistent") thì ngoại lệ
     * UsernameNotFoundException có bị ném ra hay không.
     * 3. Xác minh: Đảm bảo rằng phương thức findByUsername đã được gọi ĐÚNG 1 lần.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // 1. Thiết lập hành vi Mock (Arrange)
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // 2. Thực thi và Xác nhận ngoại lệ (Act & Assert)
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        // 3. Xác minh tương tác (Verify)
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}