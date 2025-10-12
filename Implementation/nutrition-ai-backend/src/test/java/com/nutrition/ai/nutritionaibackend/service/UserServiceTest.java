package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService; // Assuming UserServiceImpl implements UserService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername_UserFound() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testExistsByUsername_UserExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertTrue(userService.existsByUsername("testuser"));
        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    @Test
    void testExistsByUsername_UserDoesNotExist() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertFalse(userService.existsByUsername("nonexistent"));
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    @Test
    void testExistsByEmail_UserExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertTrue(userService.existsByEmail("test@example.com"));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void testExistsByEmail_UserDoesNotExist() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertFalse(userService.existsByEmail("nonexistent@example.com"));
        verify(userRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    void testSave() {
        User user = new User();
        user.setUsername("newuser");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("newuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    // Since authenticate and registerNewUserAccount are likely in an Auth Service/Controller,
    // and UserServiceImpl focuses on basic user management, these tests might be moved or adapted.
    // For now, I'll add mock implementations based on common patterns.

    @Test
    void testAuthenticate_Success() {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("authuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        assertTrue(userService.authenticate("authuser", "rawPassword"));
        verify(userRepository, times(1)).findByUsername("authuser");
        verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertFalse(userService.authenticate("nonexistent", "rawPassword"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testAuthenticate_Failure_WrongPassword() {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("authuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertFalse(userService.authenticate("authuser", "wrongPassword"));
        verify(userRepository, times(1)).findByUsername("authuser");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
    }

    @Test
    void testRegisterNewUserAccount_Success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("newreguser");
        userDto.setEmail("newreg@example.com");
        userDto.setPassword("rawPassword");

        User user = new User();
        user.setUsername("newreguser");
        user.setEmail("newreg@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto registeredUserDto = userService.registerNewUserAccount(userDto);

        assertNotNull(registeredUserDto);
        assertEquals("newreguser", registeredUserDto.getUsername());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterNewUserAccount_UsernameExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("existinguser");
        userDto.setEmail("newreg@example.com");
        userDto.setPassword("rawPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerNewUserAccount(userDto);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterNewUserAccount_EmailExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newreguser");
        userDto.setEmail("existing@example.com");
        userDto.setPassword("rawPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerNewUserAccount(userDto);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
