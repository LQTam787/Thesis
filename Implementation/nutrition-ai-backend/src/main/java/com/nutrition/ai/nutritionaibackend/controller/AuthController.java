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
 * AuthController xử lý các yêu cầu liên quan đến xác thực như đăng ký người dùng và đăng nhập.
 * Nguyên lý hoạt động: Cung cấp các REST endpoint để người dùng tương tác với hệ thống xác thực.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/auth") // Ánh xạ tất cả các endpoint trong controller này với đường dẫn cơ sở /api/auth
@Tag(name = "Authentication", description = "Endpoints for user registration and login") // Tài liệu Swagger
public class AuthController {

    private final UserService userService; // Dependency Injection cho lớp Service

    public AuthController(UserService userService) { // Constructor injection
        this.userService = userService;
    }

    /**
     * Đăng ký tài khoản người dùng mới.
     * Luồng hoạt động:
     * 1. Nhận dữ liệu người dùng (UserDto) từ body của request.
     * 2. Gọi phương thức registerNewUserAccount của UserService để tạo và lưu người dùng mới.
     * 3. Trả về đối tượng người dùng đã đăng ký thành công với HTTP status 201 (CREATED).
     * 4. Nếu có lỗi (ví dụ: tên người dùng đã tồn tại), bắt RuntimeException và trả về HTTP status 400 (BAD_REQUEST).
     *
     * @param userDto DTO chứa thông tin đăng ký người dùng.
     * @return ResponseEntity với người dùng đã tạo hoặc thông báo lỗi.
     * @throws Exception
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register") // Ánh xạ với POST /api/auth/register
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {
        try {
            UserDto registeredUser = userService.registerNewUserAccount(userDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) { // Catch more specific exceptions if possible
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Xác thực người dùng và trả về token khi đăng nhập thành công.
     * Luồng hoạt động:
     * 1. Nhận thông tin đăng nhập (LoginDto: username, password) từ body của request.
     * 2. Gọi phương thức authenticate của UserService.
     * 3. Nếu xác thực thành công (isAuthenticated là true): Trả về HTTP status 200 (OK) và một chuỗi thông báo (lý tưởng là một JWT token).
     * 4. Nếu xác thực thất bại: Trả về HTTP status 401 (UNAUTHORIZED) với thông báo lỗi.
     *
     * @param loginDto DTO chứa thông tin đăng nhập.
     * @return ResponseEntity cho biết thành công hay thất bại.
     */
    @Operation(summary = "Authenticate a user", description = "Logs in a user and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login") // Ánh xạ với POST /api/auth/login
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        // Đây là một cách tiếp cận đơn giản. Trong ứng dụng thực tế sẽ sử dụng Spring Security.
        boolean isAuthenticated = userService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        if (isAuthenticated) {
            // Trong ứng dụng thực, hãy tạo và trả về JWT token tại đây.
            return ResponseEntity.ok("User authenticated successfully. JWT token would be here.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}