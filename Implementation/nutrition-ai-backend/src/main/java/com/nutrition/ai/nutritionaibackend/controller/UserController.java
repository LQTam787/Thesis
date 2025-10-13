package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ProfileDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing user profiles")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get user profile", description = "Retrieves the profile information for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(modelMapper.map(user, ProfileDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user profile", description = "Updates the profile information for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{username}")
    public ResponseEntity<ProfileDto> updateUserProfile(@PathVariable String username, @RequestBody ProfileDto profileDto) {
        return userService.findByUsername(username)
                .map(user -> {
                    modelMapper.map(profileDto, user);
                    User updatedUser = userService.save(user);
                    return ResponseEntity.ok(modelMapper.map(updatedUser, ProfileDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
