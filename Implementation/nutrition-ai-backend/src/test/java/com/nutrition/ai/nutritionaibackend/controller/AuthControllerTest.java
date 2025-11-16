package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.LoginDto;
import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lớp kiểm thử `AuthControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến xác thực người dùng
 * (đăng ký và đăng nhập) trong `AuthController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `AuthController`.
 * `@AutoConfigureMockMvc(addFilters = false)` được sử dụng để vô hiệu hóa các bộ lọc bảo mật của Spring Security
 * trong quá trình kiểm thử, đơn giản hóa việc kiểm thử chức năng xác thực mà không bị ảnh hưởng bởi cấu hình bảo mật toàn diện.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `UserService`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc.
 */
@WebMvcTest(controllers = AuthController.class) // Chỉ tải Spring MVC cho AuthController
@AutoConfigureMockMvc(addFilters = false) // Tắt bộ lọc bảo mật (Security Filters) để đơn giản hóa kiểm thử Auth
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Dùng để mô phỏng các yêu cầu HTTP đến Controller

    @MockBean
    private UserService userService; // Tạo đối tượng giả (mock) cho Service để kiểm soát hành vi

    @Autowired
    private ObjectMapper objectMapper; // Dùng để chuyển đổi đối tượng Java thành JSON

    /**
     * Kiểm thử kịch bản đăng ký người dùng thành công.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `UserDto` chứa thông tin người dùng mới.
     * 2. Giả lập `userService.registerNewUserAccount()` để trả về `UserDto` đã tạo, mô phỏng việc đăng ký thành công.
     * 3. Thực hiện yêu cầu POST đến `/api/auth/register` với `UserDto` dưới dạng JSON.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 201 CREATED.
     */
    @Test
    void testRegisterUser_Success() throws Exception {
        // 1. Chuẩn bị dữ liệu DTO cho yêu cầu POST
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        // 2. Định nghĩa hành vi của Service (Mocking): Khi gọi userService.registerNewUserAccount,
        // trả về đối tượng DTO đã chuẩn bị (giả lập đăng ký thành công)
        when(userService.registerNewUserAccount(any(UserDto.class))).thenReturn(userDto);

        // 3. Thực hiện và kiểm tra yêu cầu:
        mockMvc.perform(post("/api/auth/register") // Gửi yêu cầu POST đến /api/auth/register
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))) // Gắn nội dung JSON
                .andExpect(status().isCreated()); // 4. Kiểm tra: Mong đợi mã trạng thái HTTP 201 CREATED
    }

    /**
     * Kiểm thử kịch bản đăng ký người dùng thất bại do tên người dùng đã tồn tại.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `UserDto`.
     * 2. Giả lập `userService.registerNewUserAccount()` để ném ra một `RuntimeException` (hoặc một ngoại lệ cụ thể hơn)
     *    mô phỏng trường hợp tên người dùng đã tồn tại.
     * 3. Thực hiện yêu cầu POST đến `/api/auth/register` với `UserDto` dưới dạng JSON.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 400 BAD REQUEST.
     */
    @Test
    void testRegisterUser_UsernameExists() throws Exception {
        // 1. Chuẩn bị dữ liệu DTO
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");

        // 2. Định nghĩa hành vi của Service (Mocking): Giả lập Service ném ra ngoại lệ (ví dụ: tên người dùng đã tồn tại)
        when(userService.registerNewUserAccount(any(UserDto.class))).thenThrow(new RuntimeException("Username already exists"));

        // 3. Thực hiện và kiểm tra yêu cầu:
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest()); // 4. Kiểm tra: Mong đợi mã trạng thái HTTP 400 BAD REQUEST
    }

    /**
     * Kiểm thử kịch bản đăng nhập người dùng thành công.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `LoginDto` chứa thông tin đăng nhập hợp lệ.
     * 2. Giả lập `userService.authenticate()` để trả về `true`, mô phỏng xác thực thành công.
     * 3. Thực hiện yêu cầu POST đến `/api/auth/login` với `LoginDto` dưới dạng JSON.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và nội dung phản hồi là thông báo thành công.
     */
    @Test
    void testLoginUser_Success() throws Exception {
        // 1. Chuẩn bị dữ liệu đăng nhập
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password");

        // 2. Định nghĩa hành vi của Service: Giả lập xác thực thành công
        when(userService.authenticate("testuser", "password")).thenReturn(true);

        // 3. Thực hiện và kiểm tra yêu cầu:
        mockMvc.perform(post("/api/auth/login") // Gửi yêu cầu POST đến /api/auth/login
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk()) // 4. Kiểm tra: Mong đợi mã trạng thái HTTP 200 OK
                .andExpect(content().string("User authenticated successfully. JWT token would be here.")); // Kiểm tra nội dung phản hồi
    }

    /**
     * Kiểm thử kịch bản đăng nhập người dùng thất bại do thông tin đăng nhập không hợp lệ.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một đối tượng `LoginDto` chứa thông tin đăng nhập không hợp lệ.
     * 2. Giả lập `userService.authenticate()` để trả về `false`, mô phỏng xác thực thất bại.
     * 3. Thực hiện yêu cầu POST đến `/api/auth/login` với `LoginDto` dưới dạng JSON.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 401 UNAUTHORIZED và nội dung phản hồi là thông báo lỗi.
     */
    @Test
    void testLoginUser_Failure() throws Exception {
        // 1. Chuẩn bị dữ liệu đăng nhập thất bại
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("wrongpassword");

        // 2. Định nghĩa hành vi của Service: Giả lập xác thực thất bại
        when(userService.authenticate("testuser", "wrongpassword")).thenReturn(false);

        // 3. Thực hiện và kiểm tra yêu cầu:
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized()) // 4. Kiểm tra: Mong đợi mã trạng thái HTTP 401 UNAUTHORIZED
                .andExpect(content().string("Invalid username or password.")); // Kiểm tra thông báo lỗi
    }
}