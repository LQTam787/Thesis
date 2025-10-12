package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.ProfileDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUserProfile_UserFound() throws Exception {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, ProfileDto.class)).thenReturn(profileDto);

        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void testGetUserProfile_UserNotFound() throws Exception {
        String username = "nonexistent";

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserProfile_Success() throws Exception {
        String username = "testuser";
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(username);
        profileDto.setFirstName("Updated");

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setFirstName("Old");

        User updatedUser = new User();
        updatedUser.setUsername(username);
        updatedUser.setFirstName("Updated");

        ProfileDto updatedProfileDto = new ProfileDto();
        updatedProfileDto.setUsername(username);
        updatedProfileDto.setFirstName("Updated");

        when(userService.findByUsername(username)).thenReturn(Optional.of(existingUser));
        // Mock the void method `modelMapper.map(source, destination)`
        doNothing().when(modelMapper).map(any(ProfileDto.class), any(User.class));
        when(userService.save(any(User.class))).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, ProfileDto.class)).thenReturn(updatedProfileDto);

        mockMvc.perform(put("/api/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void testUpdateUserProfile_UserNotFound() throws Exception {
        String username = "nonexistent";
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isNotFound());
    }
}
