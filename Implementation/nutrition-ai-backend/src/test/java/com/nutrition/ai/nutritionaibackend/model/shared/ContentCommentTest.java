package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Lớp kiểm thử đơn vị cho {@link ContentComment}.
 * Lớp này kiểm tra việc tạo, thuộc tính và hành vi của đối tượng ContentComment,
 * đặc biệt là liên quan đến các hàm tạo và các phương thức getter/setter.
 */
public class ContentCommentTest {

    private SharedContent sharedContent;
    private User user;

    /**
     * Thiết lập các đối tượng kiểm thử dùng chung trước mỗi lần kiểm thử.
     * Khởi tạo các đối tượng SharedContent và User giả để sử dụng trong các bài kiểm thử.
     */
    @BeforeEach
    void setUp() {
        sharedContent = SharedContent.builder()
                .id(1L)
                .build();
        user = User.builder()
                .id(1L)
                .build();
    }

    /**
     * Kiểm tra việc tạo một đối tượng ContentComment mới bằng cách sử dụng builder.
     * Đảm bảo rằng tất cả các thuộc tính được đặt đúng cách và createdAt ban đầu là null.
     */
    @Test
    void testContentCommentCreation() {
        String commentText = "This is a test comment.";
        ContentComment contentComment = ContentComment.builder()
                .id(1L)
                .sharedContent(sharedContent)
                .user(user)
                .text(commentText)
                .build();

        assertNotNull(contentComment);
        assertEquals(1L, contentComment.getId());
        assertEquals(sharedContent, contentComment.getSharedContent());
        assertEquals(user, contentComment.getUser());
        assertEquals(commentText, contentComment.getText());
        assertNull(contentComment.getCreatedAt()); // Nên là null trước khi @PrePersist được gọi
    }

    /**
     * Kiểm tra phương thức onCreate() mô phỏng hành vi @PrePersist.
     * Đảm bảo rằng createdAt được đặt khi phương thức được gọi, và giá trị nằm trong khoảng thời gian chấp nhận được.
     */
    @Test
    void testPrePersist() {
        ContentComment contentComment = ContentComment.builder()
                .sharedContent(sharedContent)
                .user(user)
                .text("Another test comment.")
                .build();

        contentComment.onCreate(); // Gọi thủ công phương thức PrePersist

        assertNotNull(contentComment.getCreatedAt());
        assertTrue(contentComment.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(contentComment.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    /**
     * Kiểm tra các phương thức setter và getter cho ContentComment.
     * Đảm bảo rằng các thuộc tính có thể được đặt và truy xuất một cách chính xác.
     */
    @Test
    void testSettersAndGetters() {
        ContentComment contentComment = new ContentComment();
        contentComment.setId(2L);
        contentComment.setSharedContent(sharedContent);
        contentComment.setUser(user);
        contentComment.setText("Setter test.");

        assertEquals(2L, contentComment.getId());
        assertEquals(sharedContent, contentComment.getSharedContent());
        assertEquals(user, contentComment.getUser());
        assertEquals("Setter test.", contentComment.getText());

        LocalDateTime now = LocalDateTime.now();
        contentComment.setCreatedAt(now);
        assertEquals(now, contentComment.getCreatedAt());
    }
}
