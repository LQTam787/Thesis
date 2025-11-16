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
 * AuthController xử lý tất cả các yêu cầu liên quan đến xác thực người dùng, bao gồm đăng ký tài khoản mới và đăng nhập.
 * <p>
 * Nguyên lý hoạt động: Controller này cung cấp các RESTful endpoint cho phép người dùng tương tác với hệ thống xác thực.
 * Nó ủy quyền logic nghiệp vụ cho {@link com.nutrition.ai.nutritionaibackend.service.UserService}.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP POST từ client tại các đường dẫn {@code /api/auth/register} và {@code /api/auth/login}.</li>
 *     <li>Trích xuất dữ liệu đăng ký hoặc đăng nhập từ body của yêu cầu.</li>
 *     <li>Gọi các phương thức tương ứng trong {@code UserService} để xử lý logic nghiệp vụ (ví dụ: tạo người dùng mới, xác thực thông tin đăng nhập).</li>
 *     <li>Dựa trên kết quả từ {@code UserService}, controller xây dựng và trả về {@code ResponseEntity} với trạng thái HTTP và dữ liệu phù hợp (ví dụ: 201 CREATED cho đăng ký thành công, 200 OK cho đăng nhập thành công, 400 BAD REQUEST hoặc 401 UNAUTHORIZED cho lỗi).</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/auth") // Ánh xạ tất cả các endpoint trong controller này với đường dẫn cơ sở /api/auth
@Tag(name = "Authentication", description = "Endpoints for user registration and login") // Tài liệu Swagger
public class AuthController {

    private final UserService userService; // Dependency Injection cho lớp Service

    /**
     * Constructor để inject {@link UserService} vào AuthController.
     * <p>
     * Nguyên lý hoạt động: Spring Framework tự động tiêm (inject) một thể hiện của {@code UserService}
     * vào constructor này khi tạo bean {@code AuthController}. Điều này đảm bảo rằng controller
     * có thể truy cập các dịch vụ liên quan đến người dùng và xác thực mà không cần quản lý phụ thuộc thủ công.
     * </p>
     * @param userService Service quản lý logic nghiệp vụ liên quan đến người dùng và xác thực.
     */
    public AuthController(UserService userService) { // Constructor injection
        this.userService = userService;
    }

    /**
     * Đăng ký một tài khoản người dùng mới vào hệ thống.
     * <p>
     * Endpoint này nhận dữ liệu người dùng mới dưới dạng {@link UserDto} và chuyển tiếp đến
     * {@link UserService} để xử lý việc tạo tài khoản. Nếu đăng ký thành công, nó trả về thông tin
     * người dùng đã đăng ký; nếu không, nó xử lý ngoại lệ và trả về lỗi.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/auth/register} với {@link UserDto} trong body.</li>
     *     <li>Thử gọi phương thức {@code userService.registerNewUserAccount(userDto)}.</li>
     *     <li>Nếu thành công, trả về {@link UserDto} của người dùng đã tạo với trạng thái HTTP 201 CREATED.</li>
     *     <li>Nếu {@code RuntimeException} xảy ra (ví dụ: tên người dùng đã tồn tại), bắt ngoại lệ và trả về thông báo lỗi với trạng thái HTTP 400 BAD REQUEST.</li>
     * </ol>
     * </p>
     * @param userDto DTO chứa thông tin cần thiết để đăng ký người dùng mới (ví dụ: username, password, email).
     * @return ResponseEntity chứa {@link UserDto} của người dùng đã đăng ký nếu thành công, hoặc thông báo lỗi nếu thất bại.
     * @throws Exception có thể ném ra nếu có vấn đề trong quá trình đăng ký tài khoản.
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register") // Ánh xạ với POST /api/auth/register
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {
        try {
            // Luồng hoạt động: Gọi Service để thực hiện logic nghiệp vụ đăng ký
            UserDto registeredUser = userService.registerNewUserAccount(userDto);
            // Luồng hoạt động: Trả về kết quả thành công (201 CREATED)
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) { // Catch more specific exceptions if possible
            // Luồng hoạt động: Xử lý ngoại lệ nghiệp vụ và trả về lỗi (400 BAD_REQUEST)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Xác thực thông tin đăng nhập của người dùng và cấp phát token (mô phỏng).
     * <p>
     * Endpoint này nhận thông tin đăng nhập dưới dạng {@link LoginDto} (username và password).
     * Nó sử dụng {@link UserService} để xác thực người dùng. Trong một ứng dụng thực tế,
     * phương thức này sẽ tạo và trả về một JWT (JSON Web Token) sau khi xác thực thành công.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/auth/login} với {@link LoginDto} trong body.</li>
     *     <li>Trích xuất username và password từ {@code loginDto}.</li>
     *     <li>Gọi phương thức {@code userService.authenticate(username, password)}.</li>
     *     <li>Nếu xác thực thành công ({@code isAuthenticated} là {@code true}), trả về thông báo thành công (mô phỏng token) với trạng thái HTTP 200 OK.</li>
     *     <li>Nếu xác thực thất bại, trả về thông báo lỗi với trạng thái HTTP 401 UNAUTHORIZED.</li>
     * </ol>
     * </p>
     * @param loginDto DTO chứa thông tin đăng nhập của người dùng (username và password).
     * @return ResponseEntity chứa thông báo xác thực thành công hoặc lỗi. Trong môi trường thực tế, đây sẽ là JWT token.
     */
    @Operation(summary = "Authenticate a user", description = "Logs in a user and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login") // Ánh xạ với POST /api/auth/login
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        // Đây là một cách tiếp cận đơn giản. Trong ứng dụng thực tế sẽ sử dụng Spring Security.
        // Luồng hoạt động: Gọi Service để kiểm tra thông tin đăng nhập
        boolean isAuthenticated = userService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        if (isAuthenticated) {
            // Luồng hoạt động: Nếu xác thực thành công, trả về 200 OK
            // Trong ứng dụng thực, hãy tạo và trả về JWT token tại đây.
            return ResponseEntity.ok("User authenticated successfully. JWT token would be here.");
        } else {
            // Luồng hoạt động: Nếu xác thực thất bại, trả về 401 UNAUTHORIZED
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}