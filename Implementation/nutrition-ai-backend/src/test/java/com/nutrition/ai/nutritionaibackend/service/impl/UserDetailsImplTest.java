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

@DisplayName("UserDetailsImpl Unit Tests")
class UserDetailsImplTest {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<GrantedAuthority> authorities;

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

    @Test
    @DisplayName("5. Test isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled methods")
    void testAccountStatusMethods() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("6. Test equals method - same object")
    void testEqualsSameObject() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        assertEquals(userDetails1, userDetails1);
    }

    @Test
    @DisplayName("7. Test equals method - equal objects")
    void testEqualsEqualObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertEquals(userDetails1, userDetails2);
    }

    @Test
    @DisplayName("8. Test equals method - different objects")
    void testEqualsDifferentObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(id, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(2L, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    @DisplayName("9. Test equals method - null ID")
    void testEqualsNullId() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(null, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(null, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertEquals(userDetails1, userDetails2);
    }

    @Test
    @DisplayName("10. Test equals method - null ID and non-null ID")
    void testEqualsNullIdAndNonNullId() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(null, username, email, password, authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(id, "anotherUser", "another@example.com", "anotherPassword", Collections.emptyList());
        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    @DisplayName("11. Test equals method - null object")
    void testEqualsNullObject() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);
        assertNotEquals(null, userDetails);
    }

    @Test
    @DisplayName("12. Test equals method - different class")
    void testEqualsDifferentClass() {
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, authorities);
        Object obj = new Object();
        assertNotEquals(userDetails, obj);
    }
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
