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

/**
 * UserController xử lý các yêu cầu liên quan đến quản lý hồ sơ người dùng cá nhân.
 * Nguyên lý hoạt động: Cho phép người dùng xem và cập nhật dữ liệu hồ sơ của chính họ thông qua các REST endpoint.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/users") // Ánh xạ tất cả các endpoint trong controller này với đường dẫn cơ sở /api/users
@Tag(name = "User Management", description = "Endpoints for managing user profiles") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực Bearer Token (JWT)
public class UserController {

    private final UserService userService; // Dependency Injection cho Service
    private final ModelMapper modelMapper; // Dependency Injection cho ModelMapper (chuyển đổi DTO <-> Entity)

    public UserController(UserService userService, ModelMapper modelMapper) { // Constructor injection
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Lấy thông tin hồ sơ người dùng.
     * Luồng hoạt động:
     * 1. Nhận 'username' từ path variable.
     * 2. Gọi userService.findByUsername() để tìm đối tượng User.
     * 3. Sử dụng Optional.map:
     * - Nếu tìm thấy User: Chuyển đổi User thành ProfileDto bằng ModelMapper và trả về HTTP status 200 (OK).
     * - Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param username Tên người dùng của hồ sơ cần truy xuất.
     * @return ResponseEntity chứa ProfileDto hoặc status 404.
     */
    @Operation(summary = "Get user profile", description = "Retrieves the profile information for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{username}") // Ánh xạ với GET /api/users/{username}
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(modelMapper.map(user, ProfileDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật thông tin hồ sơ người dùng.
     * Luồng hoạt động:
     * 1. Nhận 'username' từ path variable và ProfileDto mới từ body của request.
     * 2. Gọi userService.findByUsername() để tìm User hiện có.
     * 3. Sử dụng Optional.map:
     * - Nếu tìm thấy User: Ánh xạ dữ liệu từ ProfileDto vào đối tượng User hiện có.
     * - Gọi userService.save() để lưu User đã cập nhật.
     * - Chuyển đổi User đã lưu thành ProfileDto và trả về HTTP status 200 (OK).
     * - Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param username Tên người dùng của hồ sơ cần cập nhật.
     * @param profileDto DTO chứa dữ liệu hồ sơ mới.
     * @return ResponseEntity chứa ProfileDto đã cập nhật hoặc status 404.
     */
    @Operation(summary = "Update user profile", description = "Updates the profile information for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{username}") // Ánh xạ với PUT /api/users/{username}
    public ResponseEntity<ProfileDto> updateUserProfile(@PathVariable String username, @RequestBody ProfileDto profileDto) {
        return userService.findByUsername(username)
                .map(user -> {
                    // Ánh xạ các trường từ DTO vào entity User hiện có
                    modelMapper.map(profileDto, user);
                    User updatedUser = userService.save(user); // Lưu User đã cập nhật
                    return ResponseEntity.ok(modelMapper.map(updatedUser, ProfileDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}