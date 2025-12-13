package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.config.jwt.JwtUtils;
import com.nutrition.ai.nutritionaibackend.dto.JwtResponseDto;
import com.nutrition.ai.nutritionaibackend.dto.LoginDto;
import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.service.UserService;
import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager; // Dependency Injection cho AuthenticationManager
    private final JwtUtils jwtUtils; // Dependency Injection cho JwtUtils

    /**
     * Constructor để inject {@link UserService}, {@link AuthenticationManager}, và {@link JwtUtils} vào AuthController.
     * <p>
     * Nguyên lý hoạt động: Spring Framework tự động tiêm (inject) các thể hiện của các lớp
     * vào constructor này khi tạo bean {@code AuthController}. Điều này đảm bảo rằng controller
     * có thể truy cập các dịch vụ liên quan đến người dùng, xác thực và JWT mà không cần quản lý phụ thuộc thủ công.
     * </p>
     * @param userService Service quản lý logic nghiệp vụ liên quan đến người dùng và xác thực.
     * @param authenticationManager Quản lý xác thực người dùng dựa trên Spring Security.
     * @param jwtUtils Tiện ích để tạo và xác thực JWT tokens.
     */
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) { // Constructor injection
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
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
     * Xác thực thông tin đăng nhập của người dùng và cấp phát JWT token.
     * <p>
     * Endpoint này nhận thông tin đăng nhập dưới dạng {@link LoginDto} (username và password).
     * Nó sử dụng {@link AuthenticationManager} để xác thực người dùng, sau đó tạo một JWT token
     * sử dụng {@link JwtUtils} nếu xác thực thành công.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/auth/login} với {@link LoginDto} trong body.</li>
     *     <li>Tạo một {@link UsernamePasswordAuthenticationToken} từ username và password.</li>
     *     <li>Gọi {@code authenticationManager.authenticate()} để xác thực thông tin đăng nhập.</li>
     *     <li>Nếu xác thực thành công, lấy {@link UserDetailsImpl} từ Authentication object.</li>
     *     <li>Sử dụng {@code jwtUtils.generateJwtToken()} để tạo JWT token.</li>
     *     <li>Trả về {@link JwtResponseDto} chứa token và thông tin người dùng với trạng thái HTTP 200 OK.</li>
     *     <li>Nếu xác thực thất bại, Spring Security sẽ ném ra {@link org.springframework.security.core.AuthenticationException},
     *         trả về thông báo lỗi với trạng thái HTTP 401 UNAUTHORIZED.</li>
     * </ol>
     * </p>
     * @param loginDto DTO chứa thông tin đăng nhập của người dùng (username và password).
     * @return ResponseEntity chứa {@link JwtResponseDto} với JWT token và thông tin người dùng nếu thành công,
     *         hoặc thông báo lỗi nếu thất bại.
     */
    @Operation(summary = "Authenticate a user", description = "Logs in a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login") // Ánh xạ với POST /api/auth/login
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        try {
            // 1. Tạo Authentication token từ username và password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            // 2. Thiết lập Authentication vào SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Lấy UserDetailsImpl từ Authentication object
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

            // 4. Tạo JWT token
            String token = jwtUtils.generateJwtToken(authentication);

            // 5. Trả về JwtResponseDto với token và thông tin người dùng
            JwtResponseDto response = new JwtResponseDto(
                    token,
                    "Bearer",
                    userPrincipal.getId(),
                    userPrincipal.getUsername(),
                    userPrincipal.getEmail()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi xác thực
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Invalid username or password");
        }
    }
}