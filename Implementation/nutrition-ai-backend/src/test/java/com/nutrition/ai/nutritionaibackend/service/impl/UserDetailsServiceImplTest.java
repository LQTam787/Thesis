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
 * {@code UserDetailsServiceImplTest} là một lớp kiểm thử đơn vị cho {@link UserDetailsServiceImpl}.
 * Lớp này sử dụng Mockito để mô phỏng (mock) {@link UserRepository}
 * nhằm kiểm tra hành vi của {@link UserDetailsServiceImpl} một cách độc lập.
 * Các kiểm thử tập trung vào phương thức {@code loadUserByUsername},
 * đảm bảo nó trả về đúng {@link UserDetails} khi người dùng tồn tại
 * và ném ra {@link UsernameNotFoundException} khi người dùng không được tìm thấy.
 */
class UserDetailsServiceImplTest {

    /**
     * {@code userRepository} là một đối tượng giả lập (mock) của {@link UserRepository}.
     * Nó được sử dụng để kiểm soát hành vi truy cập cơ sở dữ liệu trong các bài kiểm thử,
     * cho phép chúng ta định nghĩa trước kết quả trả về khi các phương thức của nó được gọi.
     * Điều này giúp cô lập {@link UserDetailsServiceImpl} khỏi phụ thuộc thực tế vào cơ sở dữ liệu.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * {@code userDetailsService} là đối tượng đang được kiểm thử (System Under Test - SUT).
     * {@code @InjectMocks} tự động tiêm các đối tượng mock (như {@code userRepository})
     * vào các trường tương ứng của {@code userDetailsService} sau khi khởi tạo.
     */
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Phương thức thiết lập này chạy trước mỗi bài kiểm thử.
     * Nó khởi tạo tất cả các đối tượng được chú thích bằng {@code @Mock} và {@code @InjectMocks}
     * bằng cách gọi {@link MockitoAnnotations#openMocks(Object)}.
     * Điều này đảm bảo rằng mỗi bài kiểm thử bắt đầu với một môi trường sạch và các mock đã được cấu hình.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Kiểm thử phương thức {@code loadUserByUsername} khi người dùng được tìm thấy trong hệ thống.
     * Luồng hoạt động:
     * 1. Chuẩn bị (Arrange): Tạo một đối tượng {@link User} giả định với các thông tin cơ bản.
     * 2. Định nghĩa hành vi Mock (Arrange): Thiết lập {@code userRepository} để trả về {@link Optional#of(Object)}
     *    chứa đối tượng {@link User} giả định khi phương thức {@code findByUsername} được gọi với tên người dùng cụ thể.
     * 3. Thực thi (Act): Gọi phương thức {@code loadUserByUsername} của {@code userDetailsService} với tên người dùng.
     * 4. Xác nhận (Assert):
     *    - Đảm bảo rằng đối tượng {@link UserDetails} trả về không phải là {@code null}.
     *    - Kiểm tra xem tên người dùng trong {@link UserDetails} có khớp với tên người dùng đã cung cấp hay không.
     * 5. Xác minh (Verify): Đảm bảo rằng phương thức {@code userRepository.findByUsername} đã được gọi đúng một lần
     *    với tên người dùng chính xác.
     */
    @Test
    void testLoadUserByUsername_UserFound() {
        // Chuẩn bị dữ liệu (Arrange)
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(java.util.Collections.emptySet()); // Cung cấp một tập hợp rỗng cho vai trò

        // Thiết lập hành vi Mock (Arrange)
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Thực thi hành động (Act)
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Xác nhận kết quả (Assert)
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());

        // Xác minh tương tác (Verify)
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    /**
     * Kiểm thử phương thức {@code loadUserByUsername} khi người dùng KHÔNG được tìm thấy trong hệ thống.
     * Luồng hoạt động:
     * 1. Định nghĩa hành vi Mock (Arrange): Thiết lập {@code userRepository} để trả về {@link Optional#empty()}
     *    khi phương thức {@code findByUsername} được gọi với tên người dùng không tồn tại.
     * 2. Thực thi và Xác nhận ngoại lệ (Act & Assert):
     *    - Sử dụng {@link org.junit.jupiter.api.Assertions#assertThrows assertThrows} để kiểm tra xem
     *      khi gọi {@code userDetailsService.loadUserByUsername} thì một {@link UsernameNotFoundException}
     *      có được ném ra hay không.
     * 3. Xác minh (Verify): Đảm bảo rằng phương thức {@code userRepository.findByUsername} đã được gọi đúng một lần
     *    với tên người dùng đã cung cấp.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Thiết lập hành vi Mock (Arrange)
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Thực thi và Xác nhận ngoại lệ (Act & Assert)
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        // Xác minh tương tác (Verify)
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}