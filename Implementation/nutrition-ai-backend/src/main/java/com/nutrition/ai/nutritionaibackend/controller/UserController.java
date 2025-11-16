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
 * UserController xử lý các yêu cầu HTTP liên quan đến quản lý hồ sơ cá nhân của người dùng.
 * <p>
 * Nguyên lý hoạt động: Controller này cung cấp các RESTful endpoint cho phép người dùng đã xác thực
 * xem và cập nhật thông tin hồ sơ của chính họ. Nó ủy quyền logic nghiệp vụ cho
 * {@link com.nutrition.ai.nutritionaibackend.service.UserService} để tương tác với dữ liệu người dùng,
 * và sử dụng {@link org.modelmapper.ModelMapper} để chuyển đổi dữ liệu giữa các đối tượng Entity ({@link com.nutrition.ai.nutritionaibackend.model.domain.User})
 * và các đối tượng truyền tải dữ liệu (DTOs) như {@link ProfileDto}.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP (GET, PUT) từ client tại đường dẫn cơ sở {@code /api/users/{username}}.</li>
 *     <li>Yêu cầu phải bao gồm Bearer Token để xác thực (thông qua {@code @SecurityRequirement}).</li>
 *     <li>Trích xuất {@code username} từ đường dẫn để xác định người dùng.</li>
 *     <li>Ủy quyền các yêu cầu xử lý nghiệp vụ cho {@code UserService} (ví dụ: tìm kiếm, lưu dữ liệu người dùng).</li>
 *     <li>Sử dụng {@code ModelMapper} để chuyển đổi giữa {@link User} entity và {@link ProfileDto}.</li>
 *     <li>Trả về {@code ResponseEntity} với dữ liệu {@link ProfileDto} hoặc thông báo trạng thái HTTP thích hợp (ví dụ: 200 OK, 404 NOT FOUND).</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/users") // Ánh xạ tất cả các endpoint trong controller này với đường dẫn cơ sở /api/users
@Tag(name = "User Management", description = "Endpoints for managing user profiles") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực Bearer Token (JWT)
public class UserController {

    private final UserService userService; // Dependency Injection cho Service
    private final ModelMapper modelMapper; // Dependency Injection cho ModelMapper (chuyển đổi DTO <-> Entity)

    /**
     * Constructor để inject {@link UserService} và {@link ModelMapper} vào UserController.
     * <p>
     * Nguyên lý hoạt động: Spring Framework tự động tiêm các thể hiện của {@code UserService}
     * và {@code ModelMapper} vào constructor này khi tạo bean {@code UserController}. Điều này đảm bảo
     * rằng controller có thể truy cập các dịch vụ cần thiết để xử lý logic người dùng và chuyển đổi dữ liệu.
     * </p>
     * @param userService Service quản lý logic nghiệp vụ liên quan đến người dùng.
     * @param modelMapper ModelMapper để chuyển đổi giữa các DTO và entity.
     */
    public UserController(UserService userService, ModelMapper modelMapper) { // Constructor injection
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Lấy thông tin hồ sơ của một người dùng cụ thể dựa trên tên người dùng.
     * <p>
     * Endpoint này truy xuất thông tin hồ sơ của một {@link com.nutrition.ai.nutritionaibackend.model.domain.User}
     * được xác định bởi {@code username}. Nếu người dùng được tìm thấy, thông tin của họ sẽ được trả về dưới dạng
     * {@link ProfileDto}. Nếu không, một phản hồi 404 NOT FOUND sẽ được gửi đi.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/users/{username}}.</li>
     *     <li>Trích xuất {@code username} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code userService.findByUsername(username)} để tìm người dùng.</li>
     *     <li>Nếu {@code Optional<User>} chứa một giá trị:
     *         <ul>
     *             <li>Ánh xạ {@link User} sang {@link ProfileDto} bằng {@code ModelMapper}.</li>
     *             <li>Trả về {@link ProfileDto} với trạng thái HTTP 200 OK.</li>
     *         </ul>
     *     </li>
     *     <li>Nếu {@code Optional<User>} rỗng (không tìm thấy người dùng), trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param username Tên người dùng của hồ sơ cần truy xuất.
     * @return ResponseEntity chứa {@link ProfileDto} của người dùng nếu tìm thấy, hoặc trạng thái 404 NOT FOUND.
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
     * Cập nhật thông tin hồ sơ của một người dùng hiện có.
     * <p>
     * Endpoint này cho phép cập nhật thông tin hồ sơ của một {@link com.nutrition.ai.nutritionaibackend.model.domain.User}
     * được xác định bởi {@code username}. Dữ liệu cập nhật được cung cấp trong {@link ProfileDto}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu PUT đến {@code /api/users/{username}} với {@link ProfileDto} trong body.</li>
     *     <li>Trích xuất {@code username} từ đường dẫn và {@code profileDto} từ body.</li>
     *     <li>Gọi phương thức {@code userService.findByUsername(username)} để tìm người dùng hiện có.</li>
     *     <li>Nếu tìm thấy người dùng:
     *         <ul>
     *             <li>Ánh xạ các trường từ {@code profileDto} vào đối tượng {@link User} hiện có bằng {@code ModelMapper}.</li>
     *             <li>Gọi phương thức {@code userService.save(user)} để lưu các thay đổi vào cơ sở dữ liệu.</li>
     *             <li>Ánh xạ {@link User} đã cập nhật trở lại {@link ProfileDto} và trả về với trạng thái HTTP 200 OK.</li>
     *         </ul>
     *     </li>
     *     <li>Nếu không tìm thấy người dùng, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param username Tên người dùng của hồ sơ cần cập nhật.
     * @param profileDto DTO chứa dữ liệu hồ sơ mới để cập nhật.
     * @return ResponseEntity chứa {@link ProfileDto} của người dùng đã cập nhật nếu thành công, hoặc trạng thái 404 NOT FOUND.
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