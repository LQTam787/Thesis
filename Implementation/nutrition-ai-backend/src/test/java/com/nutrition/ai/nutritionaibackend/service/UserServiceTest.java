package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lớp kiểm thử đơn vị cho UserService (UserServiceImpl).
 * Sử dụng JUnit 5 và Mockito để kiểm thử các phương thức logic nghiệp vụ,
 * cô lập UserService khỏi các phụ thuộc bên ngoài như cơ sở dữ liệu và mã hóa mật khẩu.
 */
class UserServiceTest {

    // @Mock: Tạo một đối tượng giả (mock) cho UserRepository.
    // Nguyên lý: Mockito sẽ thay thế đối tượng thực bằng một phiên bản
    // có thể kiểm soát được hành vi (ví dụ: trả về dữ liệu giả).
    @Mock
    private UserRepository userRepository;

    // @Mock: Tạo một đối tượng giả cho PasswordEncoder.
    // Nguyên lý: Dùng để mô phỏng việc mã hóa và so sánh mật khẩu mà không cần
    // chạy quá trình mã hóa thực tế, giúp kiểm thử nhanh hơn và độc lập.
    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks: Tạo một thể hiện (instance) của UserServiceImpl và tự động
    // tiêm (inject) các đối tượng @Mock (userRepository, passwordEncoder) vào nó.
    // Nguyên lý: Đây là đối tượng thực (sẽ được kiểm thử) với các phụ thuộc đã được giả lập.
    @InjectMocks
    private UserServiceImpl userService; // Giả định UserServiceImpl triển khai UserService

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (@Test).
     * Nguyên lý: Khởi tạo các đối tượng @Mock và tiêm chúng vào đối tượng @InjectMocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Kiểm thử phương thức findByUsername khi tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập đối tượng User giả định.
     * 2. Thiết lập hành vi giả lập cho userRepository: khi gọi findByUsername("testuser"), trả về Optional chứa User giả.
     * 3. Gọi phương thức thực tế của userService.
     * 4. Xác minh kết quả: Kiểm tra Optional có chứa giá trị và username có đúng không.
     * 5. Xác minh tương tác: Đảm bảo userRepository.findByUsername đã được gọi đúng 1 lần.
     */
    @Test
    void testFindByUsername_UserFound() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    /**
     * Kiểm thử phương thức findByUsername khi không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho userRepository: khi gọi findByUsername("nonexistent"), trả về Optional rỗng (empty).
     * 2. Gọi phương thức thực tế của userService.
     * 3. Xác minh kết quả: Kiểm tra Optional không chứa giá trị (isPresent là false).
     * 4. Xác minh tương tác: Đảm bảo userRepository.findByUsername đã được gọi đúng 1 lần.
     */
    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    /**
     * Kiểm thử phương thức existsByUsername khi người dùng tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập: khi gọi existsByUsername("testuser"), trả về true.
     * 2. Gọi phương thức thực tế của userService.
     * 3. Xác minh kết quả: Kiểm tra phương thức trả về true.
     * 4. Xác minh tương tác: Đảm bảo userRepository.existsByUsername đã được gọi đúng 1 lần.
     */
    @Test
    void testExistsByUsername_UserExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertTrue(userService.existsByUsername("testuser"));
        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    /**
     * Kiểm thử phương thức existsByUsername khi người dùng không tồn tại.
     */
    @Test
    void testExistsByUsername_UserDoesNotExist() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertFalse(userService.existsByUsername("nonexistent"));
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    /**
     * Kiểm thử phương thức existsByEmail khi email tồn tại.
     */
    @Test
    void testExistsByEmail_UserExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertTrue(userService.existsByEmail("test@example.com"));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    /**
     * Kiểm thử phương thức existsByEmail khi email không tồn tại.
     */
    @Test
    void testExistsByEmail_UserDoesNotExist() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertFalse(userService.existsByEmail("nonexistent@example.com"));
        verify(userRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    /**
     * Kiểm thử phương thức save.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập: khi gọi userRepository.save(user), trả về chính đối tượng user đó (mô phỏng lưu thành công).
     * 2. Gọi phương thức thực tế của userService.
     * 3. Xác minh kết quả: Kiểm tra đối tượng trả về không null và username đúng.
     * 4. Xác minh tương tác: Đảm bảo userRepository.save đã được gọi đúng 1 lần với đối tượng user đó.
     */
    @Test
    void testSave() {
        User user = new User();
        user.setUsername("newuser");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("newuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Kiểm thử phương thức authenticate (đăng nhập) thành công.
     * Luồng hoạt động:
     * 1. Giả lập tìm thấy người dùng (userRepository.findByUsername -> Optional.of(user)).
     * 2. Giả lập so sánh mật khẩu thành công (passwordEncoder.matches -> true).
     * 3. Gọi phương thức authenticate.
     * 4. Xác minh kết quả: Trả về true.
     * 5. Xác minh tương tác: userRepository.findByUsername và passwordEncoder.matches đều được gọi 1 lần.
     */
    @Test
    void testAuthenticate_Success() {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("authuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        assertTrue(userService.authenticate("authuser", "rawPassword"));
        verify(userRepository, times(1)).findByUsername("authuser");
        verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
    }

    /**
     * Kiểm thử phương thức authenticate khi không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Giả lập không tìm thấy người dùng (userRepository.findByUsername -> Optional.empty()).
     * 2. Gọi phương thức authenticate.
     * 3. Xác minh kết quả: Trả về false.
     * 4. Xác minh tương tác: Đảm bảo passwordEncoder.matches KHÔNG BAO GIỜ được gọi, vì không có người dùng để so sánh mật khẩu.
     */
    @Test
    void testAuthenticate_Failure_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertFalse(userService.authenticate("nonexistent", "rawPassword"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    /**
     * Kiểm thử phương thức authenticate khi mật khẩu sai.
     * Luồng hoạt động:
     * 1. Giả lập tìm thấy người dùng.
     * 2. Giả lập so sánh mật khẩu thất bại (passwordEncoder.matches -> false).
     * 3. Gọi phương thức authenticate.
     * 4. Xác minh kết quả: Trả về false.
     */
    @Test
    void testAuthenticate_Failure_WrongPassword() {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("authuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertFalse(userService.authenticate("authuser", "wrongPassword"));
        verify(userRepository, times(1)).findByUsername("authuser");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedPassword");
    }

    /**
     * Kiểm thử phương thức registerNewUserAccount (đăng ký) thành công.
     * Luồng hoạt động:
     * 1. Giả lập: username và email chưa tồn tại (existsByUsername/Email -> false).
     * 2. Giả lập: mã hóa mật khẩu (passwordEncoder.encode -> "encodedPassword").
     * 3. Giả lập: lưu người dùng thành công (userRepository.save -> User đã lưu).
     * 4. Gọi phương thức registerNewUserAccount.
     * 5. Xác minh kết quả: Đối tượng DTO trả về không null và username đúng.
     * 6. Xác minh tương tác: Tất cả các phương thức giả lập (existsByUsername, existsByEmail, encode, save) đều được gọi đúng 1 lần.
     */
    @Test
    void testRegisterNewUserAccount_Success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("newreguser");
        userDto.setEmail("newreg@example.com");
        userDto.setPassword("rawPassword");

        User user = new User();
        user.setUsername("newreguser");
        user.setEmail("newreg@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        // any(User.class) được dùng vì chúng ta không quan tâm chính xác User nào được lưu,
        // mà chỉ quan tâm nó có được gọi với một đối tượng User hay không.
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto registeredUserDto = userService.registerNewUserAccount(userDto);

        assertNotNull(registeredUserDto);
        assertEquals("newreguser", registeredUserDto.getUsername());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Kiểm thử đăng ký thất bại do Username đã tồn tại.
     * Luồng hoạt động:
     * 1. Giả lập: existsByUsername -> true.
     * 2. Gọi phương thức đăng ký.
     * 3. Xác minh kết quả: Ném ra một Exception và thông báo lỗi đúng.
     * 4. Xác minh tương tác: existsByEmail, encode, và save KHÔNG ĐƯỢC gọi vì quy trình bị dừng ngay sau khi kiểm tra username.
     */
    @Test
    void testRegisterNewUserAccount_UsernameExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("existinguser");
        userDto.setEmail("newreg@example.com");
        userDto.setPassword("rawPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // assertThrows: Kiểm tra xem một ngoại lệ (Exception) cụ thể có được ném ra hay không.
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerNewUserAccount(userDto);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Kiểm thử đăng ký thất bại do Email đã tồn tại (sau khi kiểm tra username thành công).
     * Luồng hoạt động:
     * 1. Giả lập: existsByUsername -> false.
     * 2. Giả lập: existsByEmail -> true.
     * 3. Gọi phương thức đăng ký.
     * 4. Xác minh kết quả: Ném ra một Exception và thông báo lỗi đúng.
     * 5. Xác minh tương tác: existsByUsername và existsByEmail được gọi 1 lần, nhưng encode và save KHÔNG ĐƯỢC gọi.
     */
    @Test
    void testRegisterNewUserAccount_EmailExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newreguser");
        userDto.setEmail("existing@example.com");
        userDto.setPassword("rawPassword");

        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerNewUserAccount(userDto);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}