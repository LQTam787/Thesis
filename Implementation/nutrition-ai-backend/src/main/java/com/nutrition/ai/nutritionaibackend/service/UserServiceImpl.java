package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link UserService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các thao tác quản lý {@link User},
 * đặc biệt là liên quan đến xác thực (authentication) và đăng ký tài khoản (registration).
 * Nó tương tác với {@link UserRepository} để truy cập dữ liệu người dùng
 * và sử dụng {@link PasswordEncoder} để đảm bảo mật khẩu người dùng được mã hóa an toàn.
 * <p>
 * Nguyên tắc hoạt động:
 * <ul>
 *     <li><b>Tách biệt mối quan tâm (Separation of Concerns):</b> Tách biệt logic nghiệp vụ khỏi tầng truy cập dữ liệu (repository)
 *         và tầng trình bày (controller).</li>
 *     <li><b>Bảo mật:</b> Đảm bảo mật khẩu được mã hóa an toàn trước khi lưu trữ và được xác minh đúng cách
 *         trong quá trình xác thực.</li>
 *     <li><b>Tính nhất quán dữ liệu:</b> Xử lý các trường hợp tài khoản trùng lặp (username/email) để duy trì tính toàn vẹn của dữ liệu.</li>
 * </ul>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết.
     * {@code UserRepository} và {@code PasswordEncoder} được tự động cung cấp bởi Spring
     * khi tạo bean {@link UserServiceImpl}, đảm bảo các dependency này luôn sẵn sàng cho việc
     * quản lý người dùng và mã hóa mật khẩu.
     *
     * @param userRepository Repository để truy cập dữ liệu người dùng.
     * @param passwordEncoder Công cụ mã hóa mật khẩu.
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Tìm kiếm một người dùng dựa trên tên đăng nhập (username).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận {@code username} làm tham số.</li>
     *     <li>Gọi phương thức {@code findByUsername} của {@link UserRepository} để truy vấn cơ sở dữ liệu.</li>
     *     <li>Trả về một {@link Optional<User>} chứa đối tượng người dùng nếu tìm thấy,
     *         hoặc {@link Optional#empty()} nếu không tìm thấy.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Phương thức này thường được sử dụng trong quá trình xác thực hoặc
     * để kiểm tra sự tồn tại của người dùng trước khi thực hiện các thao tác khác.</p>
     *
     * @param username Tên đăng nhập của người dùng cần tìm.
     * @return Optional chứa đối tượng User nếu tìm thấy, ngược lại là Optional trống.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Kiểm tra xem một tên đăng nhập (username) đã tồn tại trong hệ thống hay chưa.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận {@code username} làm tham số.</li>
     *     <li>Gọi phương thức {@code existsByUsername} của {@link UserRepository} để kiểm tra sự tồn tại.</li>
     *     <li>Trả về {@code true} nếu username đã tồn tại, {@code false} nếu chưa.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Được sử dụng phổ biến trong quá trình đăng ký tài khoản mới để ngăn chặn
     * việc tạo các tài khoản có tên đăng nhập trùng lặp.</p>
     *
     * @param username Tên đăng nhập cần kiểm tra.
     * @return {@code true} nếu username tồn tại, {@code false} nếu chưa.
     */
    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Kiểm tra xem một địa chỉ email đã tồn tại trong hệ thống hay chưa.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận {@code email} làm tham số.</li>
     *     <li>Gọi phương thức {@code existsByEmail} của {@link UserRepository} để kiểm tra sự tồn tại.</li>
     *     <li>Trả về {@code true} nếu email đã tồn tại, {@code false} nếu chưa.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Tương tự như kiểm tra username, được sử dụng trong quá trình đăng ký tài khoản
     * để ngăn chặn việc tạo các tài khoản có email trùng lặp.</p>
     *
     * @param email Địa chỉ email cần kiểm tra.
     * @return {@code true} nếu email tồn tại, {@code false} nếu chưa.
     */
    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Lưu một đối tượng {@link User} mới hoặc cập nhật một người dùng hiện có vào cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link User} để lưu hoặc cập nhật.</li>
     *     <li>Uỷ quyền cho {@link UserRepository} để thực hiện thao tác lưu/cập nhật dữ liệu vào cơ sở dữ liệu.</li>
     *     <li>Trả về đối tượng {@link User} đã được lưu bền vững, có thể bao gồm ID được tạo tự động nếu là đối tượng mới.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Nếu đối tượng {@link User} có ID, thao tác sẽ là cập nhật (UPDATE);
     * nếu không có ID, thao tác sẽ là tạo mới (INSERT). Phương thức này không xử lý mã hóa mật khẩu.</p>
     *
     * @param user Đối tượng User cần được lưu.
     * @return Đối tượng User đã được lưu bền vững.
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Xác thực thông tin đăng nhập của người dùng.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm kiếm người dùng:</b> Truy vấn {@link UserRepository} để tìm đối tượng {@link User}
     *         bằng {@code username} được cung cấp.</li>
     *     <li><b>Kiểm tra sự tồn tại:</b> Nếu không tìm thấy người dùng, quá trình xác thực thất bại.</li>
     *     <li><b>So sánh mật khẩu:</b> Nếu tìm thấy người dùng, sử dụng {@link PasswordEncoder#matches(CharSequence, String)}
     *         để so sánh mật khẩu thô (plain text) {@code password} với mật khẩu đã mã hóa của người dùng ({@code user.getPassword()})
     *         trong cơ sở dữ liệu.</li>
     *     <li><b>Trả về kết quả:</b> Trả về {@code true} nếu mật khẩu khớp, {@code false} nếu không khớp hoặc không tìm thấy người dùng.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Phương thức {@code matches} của {@link PasswordEncoder} xử lý quá trình
     * so sánh mật khẩu một cách an toàn mà không cần giải mã mật khẩu đã lưu.</p>
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu thô (plain text) của người dùng.
     * @return {@code true} nếu xác thực thành công, {@code false} nếu thất bại.
     */
    @Override
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    /**
     * Đăng ký một tài khoản người dùng mới vào hệ thống.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Kiểm tra trùng lặp:</b> Thực hiện kiểm tra xem {@code username} và {@code email}
     *         đã tồn tại trong hệ thống chưa bằng cách gọi {@code existsByUsername} và {@code existsByEmail}.
     *         Nếu bất kỳ thông tin nào trùng lặp, ném một {@link Exception} để ngăn đăng ký.</li>
     *     <li><b>Chuyển đổi DTO sang Entity:</b> Tạo một đối tượng {@link User} mới và thiết lập
     *         {@code username} và {@code email} từ {@link UserDto} đầu vào.</li>
     *     <li><b>Mã hóa mật khẩu:</b> Sử dụng {@link PasswordEncoder#encode(CharSequence)} để mã hóa
     *         mật khẩu thô từ {@code userDto.getPassword()} trước khi gán vào đối tượng {@link User}.
     *         Đây là một bước bảo mật cực kỳ quan trọng.</li>
     *     <li><b>Lưu người dùng:</b> Gọi {@link UserRepository#save(Object)} để lưu đối tượng {@link User} đã được mã hóa mật khẩu
     *         vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi Entity đã lưu sang DTO:</b> Tạo một {@link UserDto} mới từ đối tượng {@link User} đã lưu
     *         để trả về. Quan trọng là KHÔNG bao gồm mật khẩu (ngay cả khi đã mã hóa) trong DTO trả về.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Đảm bảo dữ liệu người dùng mới được lưu trữ an toàn và duy nhất.</p>
     *
     * @param userDto Đối tượng {@link UserDto} chứa thông tin tài khoản người dùng mới.
     * @return {@link UserDto} của tài khoản người dùng đã được đăng ký thành công (không bao gồm mật khẩu).
     * @throws Exception nếu tên đăng nhập hoặc email đã tồn tại.
     */
    @Override
    public UserDto registerNewUserAccount(UserDto userDto) throws Exception {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //user.setPassword(userDto.getPassword());

        User savedUser = userRepository.save(user);

        UserDto returnedDto = new UserDto();
        returnedDto.setUsername(savedUser.getUsername());
        returnedDto.setEmail(savedUser.getEmail());

        return returnedDto;
    }
}