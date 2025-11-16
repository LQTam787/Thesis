package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.model.domain.Role;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@code UserDetailsImplTest} là một lớp kiểm thử đơn vị cho {@link UserDetailsImpl}.
 * Lớp này tập trung vào việc kiểm tra các chức năng của lớp {@link UserDetailsImpl},
 * đảm bảo rằng nó triển khai đúng giao diện {@link org.springframework.security.core.userdetails.UserDetails}
 * và cung cấp các chi tiết người dùng cần thiết cho khung bảo mật Spring Security.
 * Các kiểm thử bao gồm khởi tạo, các phương thức getter, phương thức build từ đối tượng {@link User},
 * kiểm tra trạng thái tài khoản và các phương thức {@code equals} và {@code hashCode}.
 */
@DisplayName("UserDetailsImpl Unit Tests")
class UserDetailsImplTest {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<GrantedAuthority> authorities;

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (phương thức được chú thích bởi {@code @Test}).
     * Phương thức này khởi tạo các biến cần thiết cho các kiểm thử, bao gồm ID, tên người dùng, email,
     * mật khẩu, các vai trò ({@link Role}) và các quyền ({@link GrantedAuthority}).
     * Điều này đảm bảo rằng mỗi kiểm thử bắt đầu với một trạng thái sạch và nhất quán.
     */
    @BeforeEach
    void setUp() {
        id = 1L;
        username = "testuser";
        email = "test@example.com";
        password = "password123";

        Role userRole = new Role(ERole.ROLE_USER);
        userRole.setId(1);
        roles = new HashSet<>(Collections.singletonList(userRole));

        authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    /**
     * Kiểm thử constructor của {@link UserDetailsImpl} và các phương thức getter tương ứng.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl} bằng constructor với các giá trị giả định.
     * 2. Sử dụng các phương thức {@code getId()}, {@code getUsername()}, {@code getEmail()},
     *    {@code getPassword()} và {@code getAuthorities()} để truy xuất các giá trị.
     * 3. Xác minh kết quả: Đảm bảo rằng các giá trị được truy xuất khớp chính xác với các giá trị đã truyền vào.
     */
    @Test
    @DisplayName("1. Test constructor and getters")
    void testConstructorAndGetters() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);

        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    /**
     * Kiểm thử phương thức tĩnh {@code build} của {@link UserDetailsImpl} để tạo một đối tượng từ {@link User}.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} giả định với các thuộc tính cần thiết, bao gồm ID và vai trò.
     * 2. Gọi phương thức tĩnh {@code UserDetailsImpl.build(user)} để tạo một đối tượng {@link UserDetailsImpl}.
     * 3. Xác minh kết quả: Đảm bảo rằng các thuộc tính của đối tượng {@link UserDetailsImpl} được tạo ra
     *    khớp chính xác với các thuộc tính của đối tượng {@link User} ban đầu.
     */
    @Test
    @DisplayName("2. Test build method with user entity")
    void testBuildMethod() {
        User user = new User(username, email, password);
        user.setId(id);
        user.setRoles(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    /**
     * Kiểm thử phương thức tĩnh {@code build} của {@link UserDetailsImpl} khi người dùng có nhiều vai trò.
     * Luồng hoạt động:
     * 1. Thêm một vai trò ADMIN vào tập hợp vai trò của người dùng thử nghiệm.
     * 2. Tạo một đối tượng {@link User} giả định với các vai trò này.
     * 3. Gọi phương thức tĩnh {@code UserDetailsImpl.build(user)}.
     * 4. Xác minh kết quả: Đảm bảo rằng đối tượng {@link UserDetailsImpl} được tạo ra có cả hai quyền
     *    "ROLE_USER" và "ROLE_ADMIN", và tổng số quyền là 2.
     */
    @Test
    @DisplayName("3. Test build method with multiple roles")
    void testBuildMethodWithMultipleRoles() {
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        adminRole.setId(2);
        roles.add(adminRole);

        User user = new User(username, email, password);
        user.setId(id);
        user.setRoles(roles);

        authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertEquals(2, userDetails.getAuthorities().size());
    }

    /**
     * Kiểm thử phương thức tĩnh {@code build} của {@link UserDetailsImpl} khi người dùng không có vai trò nào.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} giả định với tập hợp vai trò trống.
     * 2. Gọi phương thức tĩnh {@code UserDetailsImpl.build(user)}.
     * 3. Xác minh kết quả: Đảm bảo rằng đối tượng {@link UserDetailsImpl} được tạo ra có tập hợp quyền trống.
     */
    @Test
    @DisplayName("4. Test build method with empty roles")
    void testBuildMethodWithEmptyRoles() {
        User user = new User(username, email, password);
        user.setId(id);
        user.setRoles(Collections.emptySet());

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    /**
     * Kiểm thử các phương thức trạng thái tài khoản của {@link UserDetailsImpl}.
     * Các phương thức được kiểm tra bao gồm {@code isAccountNonExpired()}, {@code isAccountNonLocked()},
     * {@code isCredentialsNonExpired()} và {@code isEnabled()}.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl}.
     * 2. Gọi từng phương thức trạng thái tài khoản.
     * 3. Xác minh kết quả: Đảm bảo rằng tất cả các phương thức đều trả về {@code true},
     *    cho thấy tài khoản không bị hết hạn, không bị khóa, thông tin đăng nhập không bị hết hạn và tài khoản được kích hoạt.
     */
    @Test
    @DisplayName("5. Test isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled methods")
    void testAccountStatusMethods() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi so sánh với chính nó.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl}.
     * 2. So sánh đối tượng này với chính nó bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code true}.
     */
    @Test
    @DisplayName("6. Test equals method - same object")
    void testEqualsSameObject() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        assertEquals(userDetails1, userDetails1);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi so sánh hai đối tượng bằng nhau (có cùng ID).
     * Luồng hoạt động:
     * 1. Khởi tạo hai đối tượng {@link UserDetailsImpl} với cùng ID nhưng các thuộc tính khác có thể khác nhau.
     * 2. So sánh hai đối tượng này bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code true}, vì việc so sánh bằng dựa trên ID.
     */
    @Test
    @DisplayName("7. Test equals method - equal objects")
    void testEqualsEqualObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertEquals(userDetails1, userDetails2);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi so sánh hai đối tượng khác nhau (có ID khác nhau).
     * Luồng hoạt động:
     * 1. Khởi tạo hai đối tượng {@link UserDetailsImpl} với ID khác nhau.
     * 2. So sánh hai đối tượng này bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code false}.
     */
    @Test
    @DisplayName("8. Test equals method - different objects")
    void testEqualsDifferentObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(2L, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertNotEquals(userDetails1, userDetails2);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi cả hai đối tượng có ID là null.
     * Luồng hoạt động:
     * 1. Khởi tạo hai đối tượng {@link UserDetailsImpl} với ID là null.
     * 2. So sánh hai đối tượng này bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code true}, vì cả hai ID đều là null.
     */
    @Test
    @DisplayName("9. Test equals method - null ID")
    void testEqualsNullId() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(null, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(null, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertEquals(userDetails1, userDetails2);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi một đối tượng có ID là null và đối tượng kia có ID không phải null.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl} với ID là null và một đối tượng khác với ID không phải null.
     * 2. So sánh hai đối tượng này bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code false}.
     */
    @Test
    @DisplayName("10. Test equals method - null ID and non-null ID")
    void testEqualsNullIdAndNonNullId() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(null, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertNotEquals(userDetails1, userDetails2);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi so sánh với đối tượng null.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl}.
     * 2. So sánh đối tượng này với {@code null} bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code false}.
     */
    @Test
    @DisplayName("11. Test equals method - null object")
    void testEqualsNullObject() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);
        assertNotEquals(null, userDetails);
    }

    /**
     * Kiểm thử phương thức {@code equals} của {@link UserDetailsImpl} khi so sánh với một đối tượng thuộc lớp khác.
     * Luồng hoạt động:
     * 1. Khởi tạo một đối tượng {@link UserDetailsImpl} và một đối tượng {@link Object} chung.
     * 2. So sánh hai đối tượng này bằng phương thức {@code equals}.
     * 3. Xác minh kết quả: Đảm bảo rằng phương thức trả về {@code false}.
     */
    @Test
    @DisplayName("12. Test equals method - different class")
    void testEqualsDifferentClass() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);
        Object obj = new Object();
        assertNotEquals(userDetails, obj);
    }

    /**
     * Kiểm thử phương thức {@code hashCode} của {@link UserDetailsImpl}.
     * Luồng hoạt động:
     * 1. Khởi tạo hai đối tượng {@link UserDetailsImpl} có cùng ID (dự kiến sẽ bằng nhau) và một đối tượng có ID khác.
     * 2. So sánh mã băm (hash code) của các đối tượng.
     * 3. Xác minh kết quả: Đảm bảo rằng các đối tượng bằng nhau có mã băm bằng nhau và các đối tượng khác nhau có mã băm khác nhau.
     */
    @Test
    @DisplayName("13. Test hashCode method")
    void testHashCode() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertEquals(userDetails1.hashCode(), userDetails2.hashCode());

        UserDetailsImpl userDetails3 = new UserDetailsImpl(2L, username, email, password, authorities);
        assertNotEquals(userDetails1.hashCode(), userDetails3.hashCode());
    }
}
