package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lớp kiểm thử đơn vị cho {@link ContentLike}.
 * Lớp này kiểm tra các chức năng khác nhau của đối tượng ContentLike,
 * bao gồm các hàm tạo, builder, setter/getter, phương thức onCreate (PrePersist),
 * và các phương thức equals, hashCode, toString.
 */
@DisplayName("ContentLike Unit Tests")
class ContentLikeTest {

    private SharedContent sharedContent;
    private User user;

    /**
     * Thiết lập các đối tượng kiểm thử dùng chung trước mỗi lần kiểm thử.
     * Khởi tạo các đối tượng User và SharedContent giả để sử dụng trong các bài kiểm thử.
     */
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        sharedContent = SharedContent.builder()
                .id(1L)
                .user(user)
                .contentId(100L)
                .contentType(ContentType.NUTRITION_PLAN)
                .visibility(Visibility.PUBLIC)
                .build();
    }

    /**
     * Kiểm tra hàm tạo không đối số và các phương thức setter của ContentLike.
     * Đảm bảo rằng một đối tượng ContentLike có thể được tạo rỗng và sau đó các thuộc tính của nó
     * có thể được đặt và truy xuất chính xác.
     */
    @Test
    @DisplayName("1. Test NoArgsConstructor and Setters")
    void testNoArgsConstructorAndSetters() {
        ContentLike contentLike = new ContentLike();
        assertNotNull(contentLike);

        contentLike.setId(1L);
        contentLike.setSharedContent(sharedContent);
        contentLike.setUser(user);
        contentLike.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, contentLike.getId());
        assertEquals(sharedContent, contentLike.getSharedContent());
        assertEquals(user, contentLike.getUser());
        assertNotNull(contentLike.getCreatedAt());
    }

    /**
     * Kiểm tra hàm tạo đầy đủ đối số của ContentLike.
     * Đảm bảo rằng một đối tượng ContentLike có thể được tạo với tất cả các thuộc tính
     * được cung cấp và các giá trị đó được đặt chính xác.
     */
    @Test
    @DisplayName("2. Test AllArgsConstructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ContentLike contentLike = new ContentLike(2L, sharedContent, user, now);

        assertEquals(2L, contentLike.getId());
        assertEquals(sharedContent, contentLike.getSharedContent());
        assertEquals(user, contentLike.getUser());
        assertEquals(now, contentLike.getCreatedAt());
    }

    /**
     * Kiểm tra mẫu Builder cho ContentLike.
     * Đảm bảo rằng đối tượng ContentLike có thể được xây dựng bằng cách sử dụng mẫu builder
     * và các thuộc tính được đặt đúng cách.
     */
    @Test
    @DisplayName("3. Test Builder Pattern")
    void testBuilderPattern() {
        LocalDateTime now = LocalDateTime.now();
        ContentLike contentLike = ContentLike.builder()
                .id(3L)
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        assertEquals(3L, contentLike.getId());
        assertEquals(sharedContent, contentLike.getSharedContent());
        assertEquals(user, contentLike.getUser());
        assertEquals(now, contentLike.getCreatedAt());
    }

    /**
     * Kiểm tra phương thức onCreate() mô phỏng hành vi @PrePersist.
     * Đảm bảo rằng thuộc tính createdAt được tự động đặt khi phương thức được gọi,
     * và giá trị nằm trong khoảng thời gian chấp nhận được.
     */
    @Test
    @DisplayName("4. Test PrePersist onCreate method")
    void testOnCreateMethod() {
        ContentLike contentLike = new ContentLike();
        contentLike.setSharedContent(sharedContent);
        contentLike.setUser(user);

        assertNull(contentLike.getCreatedAt()); // Nên là null trước khi @PrePersist được gọi

        contentLike.onCreate(); // Gọi thủ công phương thức @PrePersist để kiểm thử

        assertNotNull(contentLike.getCreatedAt());
        // Xác minh rằng createdAt được đặt thành một thời gian gần đây (trong một sai số hợp lý)
        assertTrue(contentLike.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
        assertTrue(contentLike.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    /**
     * Kiểm tra các phương thức equals và hashCode do Lombok tạo ra.
     * Đảm bảo rằng hai đối tượng ContentLike có cùng thuộc tính được coi là bằng nhau
     * và có cùng mã băm; các đối tượng khác nhau được coi là không bằng nhau và có mã băm khác nhau.
     */
    @Test
    @DisplayName("5. Test Lombok generated equals and hashCode")
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        ContentLike like1 = ContentLike.builder()
                .id(1L)
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        ContentLike like2 = ContentLike.builder()
                .id(1L)
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        ContentLike like3 = ContentLike.builder()
                .id(2L) // ID khác
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        assertEquals(like1, like2);
        assertEquals(like1.hashCode(), like2.hashCode());
        assertNotEquals(like1, like3);
        assertNotEquals(like1.hashCode(), like3.hashCode());
    }

    /**
     * Kiểm tra phương thức toString của ContentLike.
     * Đảm bảo rằng biểu diễn chuỗi của đối tượng ContentLike chứa
     * tất cả các thông tin quan trọng.
     */
    @Test
    @DisplayName("6. Test toString method")
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        ContentLike contentLike = ContentLike.builder()
                .id(1L)
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        String toString = contentLike.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("sharedContent=" + sharedContent.toString()));
        assertTrue(toString.contains("user=" + user.toString()));
        assertTrue(toString.contains("createdAt=" + now.toString()));
    }
}
