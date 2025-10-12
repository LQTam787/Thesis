package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ProfileDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(modelMapper.map(user, ProfileDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

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
