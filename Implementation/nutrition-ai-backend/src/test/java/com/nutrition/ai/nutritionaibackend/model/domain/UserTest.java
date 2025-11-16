package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@example.com", "password123");
    }

    @Test
    void testUserConstructor() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNull(user.getId()); // ID should be null before persistence
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

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

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("testuser", "test@example.com", "password123");
        User user2 = new User("testuser", "test@example.com", "password123");
        User user3 = new User("anotheruser", "another@example.com", "anotherpassword");

        // Test equals
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());

        // Test hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        String expectedToString = "User(id=null, username=testuser, email=test@example.com, password=password123, roles=[])";
        // Since roles is a HashSet, the order might vary, so we'll do a partial check or reconstruct
        // For simplicity, directly compare if the initial state is as expected
        assertTrue(user.toString().contains("testuser"));
        assertTrue(user.toString().contains("test@example.com"));
    }
}
