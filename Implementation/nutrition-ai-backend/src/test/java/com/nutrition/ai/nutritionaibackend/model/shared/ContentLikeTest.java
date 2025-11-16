package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContentLike Unit Tests")
class ContentLikeTest {

    private SharedContent sharedContent;
    private User user;

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

    @Test
    @DisplayName("4. Test PrePersist onCreate method")
    void testOnCreateMethod() {
        ContentLike contentLike = new ContentLike();
        contentLike.setSharedContent(sharedContent);
        contentLike.setUser(user);

        assertNull(contentLike.getCreatedAt()); // Should be null before @PrePersist

        contentLike.onCreate(); // Manually call the @PrePersist method for testing

        assertNotNull(contentLike.getCreatedAt());
        // Verify that createdAt is set to a recent time (within a reasonable delta)
        assertTrue(contentLike.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
        assertTrue(contentLike.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

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
                .id(2L) // Different ID
                .sharedContent(sharedContent)
                .user(user)
                .createdAt(now)
                .build();

        assertEquals(like1, like2);
        assertEquals(like1.hashCode(), like2.hashCode());
        assertNotEquals(like1, like3);
        assertNotEquals(like1.hashCode(), like3.hashCode());
    }

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
