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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@WithMockUser(username = "testuser") // Giả lập người dùng hiện tại, quan trọng cho việc kiểm tra quyền truy cập
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Service giả lập cho User

    @MockBean
    private ModelMapper modelMapper; // ModelMapper giả lập

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUserProfile_UserFound() throws Exception {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setFullName("Test User");

        // 1. Mocking Service: Giả lập tìm thấy User
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        // 2. Mocking ModelMapper: Giả lập chuyển đổi Entity -> DTO
        when(modelMapper.map(user, ProfileDto.class)).thenReturn(profileDto);

        // 3. Thực hiện yêu cầu GET /api/users/{username}
        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isOk()) // 4. Kiểm tra: Trạng thái 200 OK
                .andExpect(jsonPath("$.fullName").value("Test User")); // Kiểm tra nội dung JSON
    }

    @Test
    void testGetUserProfile_UserNotFound() throws Exception {
        String username = "nonexistent";

        // 1. Mocking Service: Giả lập không tìm thấy User
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET /api/users/nonexistent
        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    @Test
    void testUpdateUserProfile_Success() throws Exception {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        ProfileDto profileDto = new ProfileDto(1L, "Updated Full Name", null, null, null, null, null, null, null, 1L);

        // 1. Mocking Service: Giả lập tìm thấy User ban đầu
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        // 2. Mocking ModelMapper: Giả lập ánh xạ DTO vào Entity (void method)
        doNothing().when(modelMapper).map(any(ProfileDto.class), any(User.class));
        // 3. Mocking Service: Giả lập lưu Entity thành công
        when(userService.save(any(User.class))).thenReturn(user);
        // 4. Mocking ModelMapper: Giả lập chuyển đổi Entity đã lưu -> DTO phản hồi
        when(modelMapper.map(user, ProfileDto.class)).thenReturn(profileDto);

        // 5. Thực hiện yêu cầu PUT /api/users/{username}
        mockMvc.perform(put("/api/users/{username}", username)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Full Name")); // 6. Kiểm tra: Cập nhật thành công
    }

    @Test
    void testUpdateUserProfile_UserNotFound() throws Exception {
        String username = "nonexistent";
        ProfileDto profileDto = new ProfileDto();

        // 1. Mocking Service: Giả lập không tìm thấy User
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu PUT với username không tồn tại
        mockMvc.perform(put("/api/users/{username}", username)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }
}