package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.UpdateUserRequestDto;
import com.nutrition.ai.nutritionaibackend.dto.UserResponseDto;
import com.nutrition.ai.nutritionaibackend.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN") // Giả lập người dùng có vai trò ADMIN để kiểm tra bảo mật (authorization)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService; // Service giả lập cho các thao tác quản trị

    @Autowired
    private ObjectMapper objectMapper;

    // --- User Management Tests ---
    @Test
    void testGetAllUsers_Success() throws Exception {
        // 1. Chuẩn bị dữ liệu danh sách người dùng giả lập
        UserResponseDto user1 = new UserResponseDto(1L, "user1", "user1@example.com", null);
        UserResponseDto user2 = new UserResponseDto(2L, "user2", "user2@example.com", null);
        List<UserResponseDto> users = Arrays.asList(user1, user2);

        // 2. Mocking: Giả lập AdminService trả về danh sách này
        when(adminService.getAllUsers()).thenReturn(users);

        // 3. Thực hiện và kiểm tra yêu cầu GET /api/admin/users
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk()) // 4. Kiểm tra: Trạng thái 200 OK
                .andExpect(jsonPath("$[0].username").value("user1")) // Kiểm tra nội dung JSON của phần tử đầu tiên
                .andExpect(jsonPath("$[1].email").value("user2@example.com")); // Kiểm tra nội dung JSON của phần tử thứ hai
    }

    @Test
    void testGetUserById_Success() throws Exception {
        Long userId = 1L;
        UserResponseDto user = new UserResponseDto(userId, "testuser", "test@example.com", null);

        // Mocking: Giả lập AdminService tìm thấy người dùng
        when(adminService.getUserById(userId)).thenReturn(user);

        // Thực hiện yêu cầu GET /api/admin/users/{id}
        mockMvc.perform(get("/api/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser")); // Kiểm tra trường username trong phản hồi
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Long userId = 1L;
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updatedUser", "updated@example.com", null);
        UserResponseDto updatedUser = new UserResponseDto(userId, "updatedUser", "updated@example.com", null);

        // Mocking: Giả lập AdminService cập nhật thành công
        when(adminService.updateUser(eq(userId), any(UpdateUserRequestDto.class))).thenReturn(updatedUser);

        // Thực hiện yêu cầu PUT /api/admin/users/{id}
        mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .with(csrf()) // Thêm token CSRF cho yêu cầu POST/PUT/DELETE
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Long userId = 1L;
        // Mocking: Giả lập Service thực hiện xóa thành công (không trả về gì)
        doNothing().when(adminService).deleteUser(userId);

        // Thực hiện yêu cầu DELETE /api/admin/users/{id}
        mockMvc.perform(delete("/api/admin/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully!")); // Kiểm tra thông báo thành công
    }

    // --- Các bài kiểm thử khác (Recipe, Goal, Nutrition Plan) tuân theo nguyên lý tương tự:
    // 1. Chuẩn bị DTO/Request.
    // 2. Giả lập hành vi của `adminService` (when/thenReturn hoặc doNothing).
    // 3. Thực hiện yêu cầu `mockMvc.perform` (GET, POST, PUT, DELETE).
    // 4. Kiểm tra trạng thái HTTP và nội dung phản hồi (`status().isOk()`, `jsonPath(...)`).
    // ...
}