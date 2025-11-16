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
 * {@code UserServiceTest} là một lớp kiểm thử đơn vị cho {@link UserServiceImpl}.
 * Lớp này sử dụng JUnit 5 và Mockito để kiểm thử các phương thức logic nghiệp vụ của dịch vụ người dùng,
 * cô lập {@link UserServiceImpl} khỏi các phụ thuộc bên ngoài như cơ sở dữ liệu (thông qua {@link UserRepository})
 * và mã hóa mật khẩu (thông qua {@link PasswordEncoder}).
 * Mục tiêu chính là đảm bảo rằng các chức năng như tìm kiếm người dùng theo tên, kiểm tra sự tồn tại của người dùng/email,
 * lưu người dùng, xác thực và đăng ký tài khoản người dùng mới hoạt động chính xác và xử lý các trường hợp biên.
 */
class UserServiceTest {

    /**
     * Mô phỏng {@link UserRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link User}.
     * Điều này cho phép kiểm thử {@link UserServiceImpl} mà không cần tương tác với cơ sở dữ liệu thực.
     * Nguyên lý: Mockito sẽ thay thế đối tượng thực bằng một phiên bản có thể kiểm soát được hành vi
     * (ví dụ: trả về dữ liệu giả hoặc xác minh các lệnh gọi phương thức).
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mô phỏng {@link PasswordEncoder} để kiểm soát hành vi mã hóa và so sánh mật khẩu.
     * Điều này giúp kiểm thử các quy trình xác thực và đăng ký mà không cần thực hiện quá trình mã hóa mật khẩu thực tế,
     * giúp tăng tốc độ kiểm thử và đảm bảo tính độc lập.
     * Nguyên lý: Dùng để mô phỏng việc mã hóa và so sánh mật khẩu mà không cần chạy quá trình mã hóa thực tế,
     * giúp kiểm thử nhanh hơn và độc lập.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Tiêm {@link UserServiceImpl} và tự động tiêm các đối tượng {@code @Mock} ({@code userRepository},
     * {@code passwordEncoder}) vào các trường tương ứng của nó. Đây là đối tượng thực sẽ được kiểm thử.
     * Nguyên lý: Đây là đối tượng thực (sẽ được kiểm thử) với các phụ thuộc đã được giả lập.
     */
    @InjectMocks
    private UserServiceImpl userService; // Giả định UserServiceImpl triển khai UserService

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (phương thức được chú thích bởi {@code @Test}).
     * Phương thức này chịu trách nhiệm khởi tạo các đối tượng mock và tiêm chúng vào đối tượng {@code @InjectMocks}
     * bằng cách gọi {@link MockitoAnnotations#openMocks(Object)}.
     * Nguyên lý: Khởi tạo các đối tượng @Mock và tiêm chúng vào đối tượng @InjectMocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Kiểm thử phương thức {@code findByUsername} của {@link UserServiceImpl} khi người dùng được tìm thấy.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} giả định với tên người dùng "testuser".
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findByUsername("testuser")} được gọi,
     *    nó sẽ trả về một {@link Optional} chứa đối tượng {@link User} giả định.
     * 3. Gọi phương thức {@code findByUsername} thực tế của {@code userService} với tên người dùng "testuser".
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem {@link Optional} trả về có chứa giá trị hay không (isPresent).
     *    - Kiểm tra xem tên người dùng của đối tượng {@link User} được trả về có chính xác là "testuser" hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng {@code userRepository.findByUsername("testuser")} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code findByUsername} của {@link UserServiceImpl} khi không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findByUsername("nonexistent")} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (rỗng).
     * 2. Gọi phương thức {@code findByUsername} thực tế của {@code userService} với tên người dùng "nonexistent".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem {@link Optional} trả về không chứa giá trị (isPresent là false).
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.findByUsername("nonexistent")} đã được gọi đúng 1 lần.
     */
    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    /**
     * Kiểm thử phương thức {@code existsByUsername} của {@link UserServiceImpl} khi tên người dùng tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code existsByUsername("testuser")} được gọi,
     *    nó sẽ trả về {@code true}.
     * 2. Gọi phương thức {@code existsByUsername} thực tế của {@code userService} với tên người dùng "testuser".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code true} hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.existsByUsername("testuser")} đã được gọi đúng 1 lần.
     */
    @Test
    void testExistsByUsername_UserExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertTrue(userService.existsByUsername("testuser"));
        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    /**
     * Kiểm thử phương thức {@code existsByUsername} của {@link UserServiceImpl} khi tên người dùng không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code existsByUsername("nonexistent")} được gọi,
     *    nó sẽ trả về {@code false}.
     * 2. Gọi phương thức {@code existsByUsername} thực tế của {@code userService} với tên người dùng "nonexistent".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code false} hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.existsByUsername("nonexistent")} đã được gọi đúng 1 lần.
     */
    @Test
    void testExistsByUsername_UserDoesNotExist() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertFalse(userService.existsByUsername("nonexistent"));
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    /**
     * Kiểm thử phương thức {@code existsByEmail} của {@link UserServiceImpl} khi email tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code existsByEmail("test@example.com")} được gọi,
     *    nó sẽ trả về {@code true}.
     * 2. Gọi phương thức {@code existsByEmail} thực tế của {@code userService} với email "test@example.com".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code true} hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.existsByEmail("test@example.com")} đã được gọi đúng 1 lần.
     */
    @Test
    void testExistsByEmail_UserExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertTrue(userService.existsByEmail("test@example.com"));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    /**
     * Kiểm thử phương thức {@code existsByEmail} của {@link UserServiceImpl} khi email không tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code existsByEmail("nonexistent@example.com")} được gọi,
     *    nó sẽ trả về {@code false}.
     * 2. Gọi phương thức {@code existsByEmail} thực tế của {@code userService} với email "nonexistent@example.com".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code false} hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.existsByEmail("nonexistent@example.com")} đã được gọi đúng 1 lần.
     */
    @Test
    void testExistsByEmail_UserDoesNotExist() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertFalse(userService.existsByEmail("nonexistent@example.com"));
        verify(userRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    /**
     * Kiểm thử phương thức {@code save} của {@link UserServiceImpl} để đảm bảo người dùng được lưu trữ chính xác.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} mới với tên người dùng "newuser".
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code userRepository.save(user)} được gọi,
     *    nó sẽ trả về chính đối tượng {@link User} đó (mô phỏng hành vi lưu thành công).
     * 3. Gọi phương thức {@code save} thực tế của {@code userService} với đối tượng {@link User} mới.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link User} trả về có không null hay không.
     *    - Kiểm tra xem tên người dùng của đối tượng đã lưu có chính xác là "newuser" hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng {@code userRepository.save(user)} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code authenticate} của {@link UserServiceImpl} trong trường hợp xác thực thành công.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} giả định với tên người dùng "authuser" và mật khẩu đã mã hóa "encodedPassword".
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findByUsername("authuser")} được gọi,
     *    nó sẽ trả về một {@link Optional} chứa {@link User} giả định.
     * 3. Thiết lập hành vi giả lập cho {@code passwordEncoder}: khi {@code matches("rawPassword", "encodedPassword")}
     *    được gọi, nó sẽ trả về {@code true} (mô phỏng mật khẩu khớp).
     * 4. Gọi phương thức {@code authenticate} thực tế của {@code userService} với tên người dùng "authuser" và mật khẩu thô "rawPassword".
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code true} hay không (xác thực thành công).
     * 6. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findByUsername("authuser")} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code passwordEncoder.matches("rawPassword", "encodedPassword")} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code authenticate} của {@link UserServiceImpl} trong trường hợp người dùng không được tìm thấy.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findByUsername("nonexistent")} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code authenticate} thực tế của {@code userService} với tên người dùng "nonexistent" và mật khẩu thô "rawPassword".
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code false} hay không (xác thực thất bại).
     * 4. Xác minh tương tác: Đảm bảo rằng {@code passwordEncoder.matches} KHÔNG BAO GIỜ được gọi, vì không có người dùng
     *    để so sánh mật khẩu.
     */
    @Test
    void testAuthenticate_Failure_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertFalse(userService.authenticate("nonexistent", "rawPassword"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    /**
     * Kiểm thử phương thức {@code authenticate} của {@link UserServiceImpl} trong trường hợp mật khẩu không khớp.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link User} giả định với tên người dùng "authuser" và mật khẩu đã mã hóa "encodedPassword".
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findByUsername("authuser")} được gọi,
     *    nó sẽ trả về một {@link Optional} chứa {@link User} giả định.
     * 3. Thiết lập hành vi giả lập cho {@code passwordEncoder}: khi {@code matches("wrongPassword", "encodedPassword")}
     *    được gọi, nó sẽ trả về {@code false} (mô phỏng mật khẩu không khớp).
     * 4. Gọi phương thức {@code authenticate} thực tế của {@code userService} với tên người dùng "authuser" và mật khẩu thô "wrongPassword".
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem phương thức có trả về {@code false} hay không (xác thực thất bại).
     * 6. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findByUsername("authuser")} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code passwordEncoder.matches("wrongPassword", "encodedPassword")} đã được gọi đúng 1 lần.
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
     * Kiểm thử phương thức {@code registerNewUserAccount} của {@link UserServiceImpl} trong trường hợp đăng ký thành công.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link UserDto} chứa thông tin đăng ký của người dùng mới.
     * 2. Tạo một đối tượng {@link User} giả định để mô phỏng người dùng sau khi được lưu.
     * 3. Thiết lập hành vi giả lập cho {@code userRepository}:
     *    - {@code existsByUsername} và {@code existsByEmail} trả về {@code false} (tên người dùng và email chưa tồn tại).
     *    - {@code save(any(User.class))} trả về đối tượng {@link User} giả định (mô phỏng lưu thành công).
     * 4. Thiết lập hành vi giả lập cho {@code passwordEncoder}: khi {@code encode(userDto.getPassword())} được gọi,
     *    nó sẽ trả về "encodedPassword" (mô phỏng mã hóa mật khẩu).
     * 5. Gọi phương thức {@code registerNewUserAccount} thực tế của {@code userService} với {@link UserDto}.
     * 6. Xác minh kết quả:
     *    - Kiểm tra xem {@link UserDto} trả về có không null hay không.
     *    - Kiểm tra xem tên người dùng của {@link UserDto} đã đăng ký có chính xác hay không.
     * 7. Xác minh tương tác: Đảm bảo rằng tất cả các phương thức giả lập ({@code existsByUsername}, {@code existsByEmail},
     *    {@code encode}, và {@code save}) đều được gọi đúng 1 lần với các đối số chính xác.
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
     * Kiểm thử phương thức {@code registerNewUserAccount} của {@link UserServiceImpl} trong trường hợp đăng ký thất bại
     * do tên người dùng đã tồn tại.
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link UserDto} với tên người dùng đã tồn tại ("existinguser").
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code existsByUsername} được gọi,
     *    nó sẽ trả về {@code true}.
     * 3. Gọi phương thức {@code registerNewUserAccount} thực tế của {@code userService} với {@link UserDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link Exception} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác là "Username already exists" hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng {@code existsByUsername} được gọi 1 lần, nhưng các phương thức
     *    {@code existsByEmail}, {@code encode}, và {@code save} KHÔNG BAO GIỜ được gọi vì quy trình bị dừng ngay sau
     *    khi kiểm tra tên người dùng.
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
     * Kiểm thử phương thức {@code registerNewUserAccount} của {@link UserServiceImpl} trong trường hợp đăng ký thất bại
     * do email đã tồn tại (sau khi kiểm tra tên người dùng thành công).
     * Luồng hoạt động:
     * 1. Tạo một đối tượng {@link UserDto} với email đã tồn tại ("existing@example.com").
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}:
     *    - {@code existsByUsername} trả về {@code false} (tên người dùng chưa tồn tại).
     *    - {@code existsByEmail} trả về {@code true} (email đã tồn tại).
     * 3. Gọi phương thức {@code registerNewUserAccount} thực tế của {@code userService} với {@link UserDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link Exception} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác là "Email already exists" hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng {@code existsByUsername} và {@code existsByEmail} được gọi 1 lần,
     *    nhưng các phương thức {@code encode} và {@code save} KHÔNG BAO GIỜ được gọi vì quy trình bị dừng ngay sau
     *    khi kiểm tra email.
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