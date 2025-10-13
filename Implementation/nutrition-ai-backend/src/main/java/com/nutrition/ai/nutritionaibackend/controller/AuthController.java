package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.LoginDto;
import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController handles authentication-related requests such as user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
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
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
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
    @Operation(summary = "Authenticate a user", description = "Logs in a user and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
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
