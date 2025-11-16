package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link SharedContent} entity.
 * This class ensures that the SharedContent model behaves as expected,
 * covering constructors, getters, setters, and lifecycle methods.
 */
class SharedContentTest {

    private SharedContent sharedContent;

    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");
    }

    /**
     * Test case for the all-args constructor and getter methods.
     * Ensures that a SharedContent object can be created with all its properties
     * and that the getter methods return the correct values.
     * Happy Path: Object creation with valid data.
     */
    @Test
    @DisplayName("Test all-args constructor and getters for SharedContent")
    void testAllArgsConstructorAndGetters() {
        Long id = 1L;
        Long contentId = 101L;
        ContentType contentType = ContentType.RECIPE;
        Visibility visibility = Visibility.PUBLIC;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);

        sharedContent = new SharedContent(id, mockUser, contentId, contentType, visibility, createdAt);

        assertEquals(id, sharedContent.getId(), "ID should match");
        assertEquals(mockUser, sharedContent.getUser(), "User should match");
        assertEquals(contentId, sharedContent.getContentId(), "Content ID should match");
        assertEquals(contentType, sharedContent.getContentType(), "Content Type should match");
        assertEquals(visibility, sharedContent.getVisibility(), "Visibility should match");
        assertEquals(createdAt, sharedContent.getCreatedAt(), "CreatedAt should match");
    }

    /**
     * Test case for the Builder pattern.
     * Ensures that a SharedContent object can be constructed using the builder
     * and that the getter methods return the correct values.
     * Happy Path: Object creation with valid data using builder.
     */
    @Test
    @DisplayName("Test Builder pattern for SharedContent")
    void testBuilderPattern() {
        Long id = 2L;
        Long contentId = 102L;
        ContentType contentType = ContentType.NUTRITION_PLAN;
        Visibility visibility = Visibility.FRIENDS;
        LocalDateTime createdAt = LocalDateTime.now().minusHours(2);

        sharedContent = SharedContent.builder()
                .id(id)
                .user(mockUser)
                .contentId(contentId)
                .contentType(contentType)
                .visibility(visibility)
                .createdAt(createdAt)
                .build();

        assertEquals(id, sharedContent.getId(), "ID should match");
        assertEquals(mockUser, sharedContent.getUser(), "User should match");
        assertEquals(contentId, sharedContent.getContentId(), "Content ID should match");
        assertEquals(contentType, sharedContent.getContentType(), "Content Type should match");
        assertEquals(visibility, sharedContent.getVisibility(), "Visibility should match");
        assertEquals(createdAt, sharedContent.getCreatedAt(), "CreatedAt should match");
    }

    /**
     * Test case for the no-args constructor and setter methods.
     * Ensures that a SharedContent object can be created without arguments
     * and that the setter methods correctly update the object's properties.
     * Happy Path: Default object creation and property modification.
     */
    @Test
    @DisplayName("Test no-args constructor and setters for SharedContent")
    void testNoArgsConstructorAndSetters() {
        sharedContent = new SharedContent();
        assertNotNull(sharedContent, "SharedContent object should not be null");

        Long newId = 3L;
        Long newContentId = 103L;
        ContentType newContentType = ContentType.ACTIVITY_LOG;
        Visibility newVisibility = Visibility.FRIENDS; // Changed from PRIVATE to FRIENDS
        LocalDateTime newCreatedAt = LocalDateTime.now();

        sharedContent.setId(newId);
        sharedContent.setUser(mockUser);
        sharedContent.setContentId(newContentId);
        sharedContent.setContentType(newContentType);
        sharedContent.setVisibility(newVisibility);
        sharedContent.setCreatedAt(newCreatedAt);

        assertEquals(newId, sharedContent.getId(), "ID should be updated");
        assertEquals(mockUser, sharedContent.getUser(), "User should be updated");
        assertEquals(newContentId, sharedContent.getContentId(), "Content ID should be updated");
        assertEquals(newContentType, sharedContent.getContentType(), "Content Type should be updated");
        assertEquals(newVisibility, sharedContent.getVisibility(), "Visibility should be updated");
        assertEquals(newCreatedAt, sharedContent.getCreatedAt(), "CreatedAt should be updated");
    }

    /**
     * Test case for the {@code @PrePersist} method.
     * Ensures that the {@code createdAt} timestamp is automatically set
     * when a new SharedContent object is persisted (simulated by calling onCreate()).
     * Happy Path: Automatic timestamp generation.
     */
    @Test
    @DisplayName("Test @PrePersist method sets createdAt timestamp")
    void testPrePersistCreatedAt() {
        sharedContent = new SharedContent();
        assertNull(sharedContent.getCreatedAt(), "CreatedAt should be null before PrePersist");

        sharedContent.onCreate();

        assertNotNull(sharedContent.getCreatedAt(), "CreatedAt should not be null after PrePersist");
        assertTrue(sharedContent.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)),
                "CreatedAt should be set to current time or very recent past");
        assertTrue(sharedContent.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)),
                "CreatedAt should be set to current time or very recent past");
    }

    /**
     * Test case for {@code equals()} and {@code hashCode()} methods.
     * Ensures that two SharedContent objects with the same ID are considered equal
     * and have the same hash code.
     * Edge Case: Comparison with null and different types.
     * Failure Case: Comparison with different IDs.
     */
    @Test
    @DisplayName("Test equals and hashCode methods for SharedContent")
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        SharedContent sc1 = SharedContent.builder()
                .id(1L)
                .user(mockUser)
                .contentId(101L)
                .contentType(ContentType.RECIPE)
                .visibility(Visibility.PUBLIC)
                .createdAt(now)
                .build();

        SharedContent sc2 = SharedContent.builder()
                .id(1L)
                .user(mockUser)
                .contentId(101L)
                .contentType(ContentType.RECIPE)
                .visibility(Visibility.PUBLIC)
                .createdAt(now)
                .build();

        SharedContent sc3 = SharedContent.builder()
                .id(2L) // Different ID
                .user(mockUser)
                .contentId(102L)
                .contentType(ContentType.NUTRITION_PLAN)
                .visibility(Visibility.FRIENDS)
                .createdAt(now)
                .build();

        // Happy Path: Equal objects
        assertEquals(sc1, sc2, "Two SharedContent objects with same ID should be equal");
        assertEquals(sc1.hashCode(), sc2.hashCode(), "Two equal SharedContent objects should have same hash code");

        // Failure Case: Different objects
        assertNotEquals(sc1, sc3, "Two SharedContent objects with different IDs should not be equal");
        assertNotEquals(sc1.hashCode(), sc3.hashCode(), "Two different SharedContent objects should have different hash codes");

        // Edge Case: Null comparison
        assertNotEquals(null, sc1, "SharedContent object should not be equal to null");

        // Edge Case: Different class comparison
        assertNotEquals("string", sc1, "SharedContent object should not be equal to object of different type");
    }

    /**
     * Test case for the {@code toString()} method.
     * Ensures that the toString() method returns a string representation
     * that includes the class name and all relevant fields.
     * Happy Path: Verifying string content.
     */
    @Test
    @DisplayName("Test toString method for SharedContent")
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        sharedContent = SharedContent.builder()
                .id(1L)
                .user(mockUser)
                .contentId(101L)
                .contentType(ContentType.RECIPE)
                .visibility(Visibility.PUBLIC)
                .createdAt(now)
                .build();

        String expectedToString = "SharedContent(id=1, user=" + mockUser + ", contentId=101, contentType=RECIPE, visibility=PUBLIC, createdAt=" + now + ")";
        assertEquals(expectedToString, sharedContent.toString(), "ToString should return expected string representation");
    }
}
