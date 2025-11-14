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
 *
 * Luồng hoạt động: Nhận HTTP request (yêu cầu Bearer Token cho xác thực),
 * sử dụng UserService để truy xuất/lưu dữ liệu, và ModelMapper để chuyển đổi dữ liệu giữa
 * đối tượng Entity (User) và đối tượng truyền tải dữ liệu (ProfileDto), sau đó trả về phản hồi.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/users") // Ánh xạ tất cả các endpoint trong controller này với đường dẫn cơ sở /api/users
@Tag(name = "User Management", description = "Endpoints for managing user profiles") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực Bearer Token (JWT)
public class UserController {

    private final UserService userService; // Dependency Injection cho Service
    private final ModelMapper modelMapper; // Dependency Injection cho ModelMapper (chuyển đổi DTO <-> Entity)

    /**
     * Nguyên lý hoạt động: Spring tự động inject (tiêm) các thể hiện của UserService
     * và ModelMapper vào constructor này.
     * @param userService Service quản lý nghiệp vụ người dùng.
     * @param modelMapper Thư viện giúp chuyển đổi dữ liệu giữa các lớp.
     */
    public UserController(UserService userService, ModelMapper modelMapper) { // Constructor injection
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Lấy thông tin hồ sơ người dùng.
     * Luồng hoạt động:
     * 1. Nhận 'username' từ path variable.
     * 2. Gọi userService.findByUsername() để tìm đối tượng User. Kết quả là một Optional<User>.
     * 3. Sử dụng Optional.map:
     * - Nếu tìm thấy User: Chuyển đổi User thành ProfileDto bằng ModelMapper và trả về HTTP status 200 (OK).
     * - Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * Nguyên lý hoạt động: Sử dụng Optional để xử lý an toàn trường hợp không tìm thấy người dùng,
     * tránh NullPointerException. ModelMapper đảm bảo chỉ các trường an toàn được công khai trong ProfileDto.
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
        // Luồng hoạt động: Tìm User theo username, chuyển đổi thành DTO nếu tìm thấy, nếu không trả về 404
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(modelMapper.map(user, ProfileDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật thông tin hồ sơ người dùng.
     * Luồng hoạt động:
     * 1. Nhận 'username' từ path variable và ProfileDto mới từ body của request.
     * 2. Gọi userService.findByUsername() để tìm User hiện có. Kết quả là một Optional<User>.
     * 3. Sử dụng Optional.map:
     * - Nếu tìm thấy User:
     * - Ánh xạ (map) dữ liệu từ ProfileDto vào đối tượng User hiện có (modelMapper.map(profileDto, user)).
     * - Gọi userService.save() để lưu User đã cập nhật.
     * - Chuyển đổi User đã lưu thành ProfileDto và trả về HTTP status 200 (OK).
     * - Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * Nguyên lý hoạt động: Sử dụng ModelMapper để ánh xạ dữ liệu cập nhật từ DTO vào đối tượng Entity (User) hiện có,
     * sau đó gọi Service để duy trì (lưu) các thay đổi vào cơ sở dữ liệu.
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
        // Luồng hoạt động: Tìm User theo username
        return userService.findByUsername(username)
                .map(user -> {
                    // Luồng hoạt động: Ánh xạ các trường từ DTO vào entity User hiện có để cập nhật
                    modelMapper.map(profileDto, user);
                    // Luồng hoạt động: Lưu User đã cập nhật
                    User updatedUser = userService.save(user); // Lưu User đã cập nhật
                    // Luồng hoạt động: Trả về DTO đã cập nhật với 200 OK
                    return ResponseEntity.ok(modelMapper.map(updatedUser, ProfileDto.class));
                })
                .orElse(ResponseEntity.notFound().build()); // Luồng hoạt động: Nếu không tìm thấy, trả về 404
    }
}