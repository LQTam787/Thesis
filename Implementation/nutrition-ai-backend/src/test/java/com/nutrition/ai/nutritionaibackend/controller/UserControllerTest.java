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

/**
 * Lớp kiểm thử `UserControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến quản lý hồ sơ người dùng
 * (xem và cập nhật hồ sơ) trong `UserController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `UserController`.
 * `@WithMockUser(username = "testuser")` giả lập một người dùng đã đăng nhập với tên người dùng "testuser",
 * cho phép kiểm thử các điểm cuối yêu cầu xác thực và ủy quyền dựa trên tên người dùng trong đường dẫn.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `UserService` và `ModelMapper`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc và cô lập logic của controller để kiểm thử.
 */
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

    /**
     * Kiểm thử kịch bản thành công khi lấy hồ sơ người dùng theo tên người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `username`, một đối tượng `User` giả lập và một `ProfileDto` mong đợi.
     * 2. Giả lập `userService.findByUsername()` để trả về `Optional.of(user)`.
     * 3. Giả lập `modelMapper.map(user, ProfileDto.class)` để chuyển đổi `User` sang `ProfileDto`.
     * 4. Thực hiện yêu cầu GET đến `/api/users/{username}`.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `fullName` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
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

    /**
     * Kiểm thử kịch bản không tìm thấy người dùng khi lấy hồ sơ theo tên người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `username` không tồn tại.
     * 2. Giả lập `userService.findByUsername()` để trả về `Optional.empty()`.
     * 3. Thực hiện yêu cầu GET đến `/api/users/{username}` với tên người dùng không tồn tại.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
    @Test
    void testGetUserProfile_UserNotFound() throws Exception {
        String username = "nonexistent";

        // 1. Mocking Service: Giả lập không tìm thấy User
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET /api/users/nonexistent
        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    /**
     * Kiểm thử kịch bản thành công khi cập nhật hồ sơ người dùng.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `username`, một đối tượng `User` giả lập và một `ProfileDto` với dữ liệu cập nhật.
     * 2. Giả lập `userService.findByUsername()` để trả về `Optional.of(user)`.
     * 3. Giả lập `modelMapper.map(ProfileDto, User)` để ánh xạ dữ liệu DTO vào Entity (phương thức `void`).
     * 4. Giả lập `userService.save()` để trả về `User` đã lưu.
     * 5. Giả lập `modelMapper.map(User, ProfileDto)` để chuyển đổi `User` đã lưu sang `ProfileDto` phản hồi.
     * 6. Thực hiện yêu cầu PUT đến `/api/users/{username}` với `ProfileDto` dưới dạng JSON và token CSRF.
     * 7. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `fullName` trong JSON phản hồi đã được cập nhật.
     */
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

    /**
     * Kiểm thử kịch bản không tìm thấy người dùng khi cập nhật hồ sơ.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một `username` không tồn tại và một `ProfileDto`.
     * 2. Giả lập `userService.findByUsername()` để trả về `Optional.empty()`.
     * 3. Thực hiện yêu cầu PUT đến `/api/users/{username}` với dữ liệu cập nhật và token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
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