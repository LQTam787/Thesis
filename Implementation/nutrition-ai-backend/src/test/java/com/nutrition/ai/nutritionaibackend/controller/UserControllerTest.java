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
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser(username = "testuser")
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
        profileDto.setFullName("Test User");

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, ProfileDto.class)).thenReturn(profileDto);

        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Test User"));
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
        ProfileDto profileDto = new ProfileDto(1L, "Updated Full Name", null, null, null, null, null, null, null, 1L);
        when(userService.updateUserProfile(eq("testuser"), any(ProfileDto.class))).thenReturn(profileDto);

        mockMvc.perform(put("/api/users/testuser")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Full Name"));
    }

    @Test
    void testUpdateUserProfile_UserNotFound() throws Exception {
        ProfileDto profileDto = new ProfileDto(1L, "Updated Full Name", null, null, null, null, null, null, null, 1L);
        when(userService.updateUserProfile(eq("nonexistent"), any(ProfileDto.class))).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/users/nonexistent")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isNotFound());
    }
}
