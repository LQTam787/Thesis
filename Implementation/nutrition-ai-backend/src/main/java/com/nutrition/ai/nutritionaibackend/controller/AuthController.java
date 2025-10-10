package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.LoginDto;
import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController handles authentication-related requests such as user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user account.
     *
     * @param userDto DTO containing user registration information.
     * @return ResponseEntity with the created user or an error message.
     * @throws Exception 
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {
        try {
            UserDto registeredUser = userService.registerNewUserAccount(userDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) { // Catch more specific exceptions if possible
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Authenticates a user and returns a token upon successful login.
     * NOTE: This is a placeholder. In a real-world application, this should be secured with Spring Security
     * and return a JWT or similar session token.
     *
     * @param loginDto DTO containing login credentials.
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        boolean isAuthenticated = userService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        if (isAuthenticated) {
            // In a real app, generate and return a JWT token here.
            return ResponseEntity.ok("User authenticated successfully. JWT token would be here.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}
