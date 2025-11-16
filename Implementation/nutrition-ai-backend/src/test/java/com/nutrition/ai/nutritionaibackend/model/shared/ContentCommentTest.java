package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class ContentCommentTest {

    private SharedContent sharedContent;
    private User user;

    @BeforeEach
    void setUp() {
        sharedContent = SharedContent.builder()
                .id(1L)
                .build();
        user = User.builder()
                .id(1L)
                .build();
    }

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
        assertNull(contentComment.getCreatedAt()); // Should be null before @PrePersist
    }

    @Test
    void testPrePersist() {
        ContentComment contentComment = ContentComment.builder()
                .sharedContent(sharedContent)
                .user(user)
                .text("Another test comment.")
                .build();

        contentComment.onCreate(); // Manually call PrePersist method

        assertNotNull(contentComment.getCreatedAt());
        assertTrue(contentComment.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(contentComment.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

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
