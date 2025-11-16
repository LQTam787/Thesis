package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lớp kiểm thử đơn vị cho lớp User.
 * Lớp này kiểm tra các chức năng khác nhau của đối tượng User,
 * bao gồm hàm tạo, getter/setter, thao tác vai trò,
 * và các phương thức equals, hashCode, và toString.
 */
class UserTest {

    private User user;

    /**
     * Thiết lập đối tượng User kiểm thử trước mỗi lần kiểm thử.
     * Khởi tạo một đối tượng User mới với các giá trị mặc định.
     */
    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@example.com", "password123");
    }

    /**
     * Kiểm tra hàm tạo của User với username, email và password.
     * Đảm bảo rằng đối tượng được khởi tạo không null và các thuộc tính
     * được đặt chính xác, và danh sách vai trò (roles) là rỗng.
     */
    @Test
    void testUserConstructor() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNull(user.getId()); // ID phải là null trước khi lưu vào cơ sở dữ liệu
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    /**
     * Kiểm tra hàm tạo không đối số của User.
     * Đảm bảo rằng đối tượng được khởi tạo không null và tất cả các thuộc tính
     * đều null, ngoại trừ danh sách vai trò (roles) là rỗng.
     */
    @Test
    void testNoArgsConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getId());
        assertNull(emptyUser.getUsername());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getPassword());
        assertNotNull(emptyUser.getRoles());
        assertTrue(emptyUser.getRoles().isEmpty());
    }

    /**
     * Kiểm tra hàm tạo đầy đủ đối số của User.
     * Đảm bảo rằng đối tượng được khởi tạo không null và tất cả các thuộc tính
     * được đặt chính xác, bao gồm cả ID và danh sách vai trò (roles).
     */
    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String username = "allarguser";
        String email = "allarg@example.com";
        String password = "allargpassword";
        Set<Role> roles = new HashSet<>(Collections.singletonList(new Role(null, ERole.ROLE_USER)));

        User allArgUser = new User(id, username, email, password, roles);

        assertNotNull(allArgUser);
        assertEquals(id, allArgUser.getId());
        assertEquals(username, allArgUser.getUsername());
        assertEquals(email, allArgUser.getEmail());
        assertEquals(password, allArgUser.getPassword());
        assertEquals(roles, allArgUser.getRoles());
    }

    /**
     * Kiểm tra các phương thức getter và setter của User.
     * Đảm bảo rằng các thuộc tính có thể được đặt và lấy một cách chính xác.
     */
    @Test
    void testGettersAndSetters() {
        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());

        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());

        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());

        Role roleAdmin = new Role(null, ERole.ROLE_ADMIN);
        Set<Role> newRoles = new HashSet<>(Collections.singletonList(roleAdmin));
        user.setRoles(newRoles);
        assertEquals(newRoles, user.getRoles());
    }

    /**
     * Kiểm tra các thao tác thêm, xóa và xóa tất cả các vai trò của User.
     * Đảm bảo rằng danh sách vai trò (roles) hoạt động đúng như mong đợi.
     */
    @Test
    void testRolesManipulation() {
        Role roleUser = new Role(null, ERole.ROLE_USER);
        Role roleModerator = new Role(null, ERole.ROLE_ADMIN);

        user.getRoles().add(roleUser);
        assertFalse(user.getRoles().isEmpty());
        assertTrue(user.getRoles().contains(roleUser));
        assertEquals(1, user.getRoles().size());

        user.getRoles().add(roleModerator);
        assertTrue(user.getRoles().contains(roleModerator));
        assertEquals(2, user.getRoles().size());

        user.getRoles().remove(roleUser);
        assertFalse(user.getRoles().contains(roleUser));
        assertEquals(1, user.getRoles().size());

        user.getRoles().clear();
        assertTrue(user.getRoles().isEmpty());
    }

    /**
     * Kiểm tra các phương thức equals và hashCode của User.
     * Đảm bảo rằng hai đối tượng User có cùng thuộc tính được coi là bằng nhau
     * và có cùng mã băm.
     */
    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("testuser", "test@example.com", "password123");
        User user2 = new User("testuser", "test@example.com", "password123");
        User user3 = new User("anotheruser", "another@example.com", "anotherpassword");

        // Kiểm tra equals
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());

        // Kiểm tra hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    /**
     * Kiểm tra phương thức toString của User.
     * Đảm bảo rằng biểu diễn chuỗi của đối tượng User chứa
     * các thông tin cần thiết.
     */
    @Test
    void testToString() {
        // Vì roles là một HashSet, thứ tự có thể thay đổi, nên chúng ta sẽ kiểm tra một phần
        assertTrue(user.toString().contains("testuser"));
        assertTrue(user.toString().contains("test@example.com"));
    }
}
