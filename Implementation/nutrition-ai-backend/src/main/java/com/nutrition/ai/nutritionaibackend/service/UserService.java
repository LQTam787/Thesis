package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.Optional;

/**
 * Service Interface cho việc quản lý các đối tượng {@link User} trong hệ thống.
 * Giao diện này định nghĩa các API nghiệp vụ cốt lõi liên quan đến người dùng,
 * bao gồm các thao tác tìm kiếm, kiểm tra sự tồn tại (existence checks), lưu (save),
 * xác thực (authentication), và đăng ký tài khoản mới.
 * Mục tiêu là cung cấp một tầng trừu tượng cho logic nghiệp vụ người dùng, tách biệt khỏi
 * chi tiết triển khai cụ thể của lớp repository và các cơ chế bảo mật như mã hóa mật khẩu.
 */
public interface UserService {

    /**
     * Tìm kiếm một đối tượng {@link User} bằng tên đăng nhập (username) của người dùng.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một chuỗi {@code username} làm tham số.</li>
     *     <li>Truy vấn cơ sở dữ liệu thông qua lớp repository để tìm người dùng có tên đăng nhập tương ứng.</li>
     *     <li>Trả về một {@link Optional<User>} chứa đối tượng người dùng nếu tìm thấy, hoặc một {@code Optional.empty()} nếu không.
     *         Phương thức này thường được sử dụng trong quá trình xác thực hoặc kiểm tra sự tồn tại của người dùng.</li>
     * </ol>
     *
     * @param username Tên đăng nhập của người dùng cần tìm kiếm.
     * @return Optional chứa đối tượng User nếu tìm thấy, ngược lại là Optional trống.
     */
    Optional<User> findByUsername(String username);

    /**
     * Kiểm tra sự tồn tại của một người dùng trong hệ thống dựa trên tên đăng nhập (username).
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một chuỗi {@code username} làm tham số.</li>
     *     <li>Truy vấn cơ sở dữ liệu thông qua lớp repository để kiểm tra xem có bất kỳ người dùng nào
     *         có tên đăng nhập khớp với {@code username} đã cho hay không.</li>
     *     <li>Trả về {@code true} nếu người dùng tồn tại, {@code false} nếu ngược lại.
     *         Phương thức này hữu ích trong quá trình đăng ký để đảm bảo tính duy nhất của tên đăng nhập.</li>
     * </ol>
     *
     * @param username Tên đăng nhập cần kiểm tra.
     * @return {@code true} nếu người dùng tồn tại, {@code false} nếu không.
     */
    Boolean existsByUsername(String username);

    /**
     * Kiểm tra sự tồn tại của một người dùng trong hệ thống dựa trên địa chỉ email.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một chuỗi {@code email} làm tham số.</li>
     *     <li>Truy vấn cơ sở dữ liệu thông qua lớp repository để kiểm tra xem có bất kỳ người dùng nào
     *         có địa chỉ email khớp với {@code email} đã cho hay không.</li>
     *     <li>Trả về {@code true} nếu người dùng tồn tại, {@code false} nếu ngược lại.
     *         Phương thức này hữu ích trong quá trình đăng ký để đảm bảo tính duy nhất của địa chỉ email.</li>
     * </ol>
     *
     * @param email Địa chỉ email cần kiểm tra.
     * @return {@code true} nếu người dùng tồn tại, {@code false} nếu không.
     */
    Boolean existsByEmail(String email);

    /**
     * Lưu (hoặc cập nhật) một đối tượng {@link User} vào cơ sở dữ liệu.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận một đối tượng {@link User} Entity để lưu hoặc cập nhật.</li>
     *     <li>Uỷ quyền cho {@code UserRepository} để thực hiện thao tác lưu bền vững vào cơ sở dữ liệu.</li>
     *     <li>Trả về đối tượng {@link User} đã được lưu, bao gồm ID được tạo tự động nếu là tạo mới.</li>
     * </ol>
     *
     * @param user Đối tượng User cần được lưu.
     * @return Đối tượng User đã được lưu bền vững.
     */
    User save(User user);

    /**
     * Xác thực người dùng bằng cách so sánh tên đăng nhập và mật khẩu được cung cấp.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận tên đăng nhập ({@code username}) và mật khẩu thô ({@code password}) từ yêu cầu xác thực.</li>
     *     <li>Tìm đối tượng {@link User} bằng {@code username}.</li>
     *     <li>Nếu tìm thấy người dùng, sử dụng {@code PasswordEncoder} để so sánh mật khẩu thô với mật khẩu đã mã hóa
     *         (hashed) được lưu trữ trong cơ sở dữ liệu.</li>
     *     <li>Trả về {@code true} nếu quá trình xác thực thành công (mật khẩu khớp), {@code false} nếu ngược lại.</li>
     * </ol>
     * <p>Lưu ý: Phương thức này không nên tự thực hiện mã hóa hoặc giải mã mật khẩu trực tiếp,
     * mà nên sử dụng {@code PasswordEncoder} để đảm bảo bảo mật.</p>
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu thô (chưa được mã hóa) của người dùng.
     * @return {@code true} nếu xác thực thành công, {@code false} nếu không thành công.
     */
    boolean authenticate(String username, String password);

    /**
     * Đăng ký một tài khoản người dùng mới vào hệ thống.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Kiểm tra tính duy nhất:</b> Kiểm tra xem tên đăng nhập ({@code username}) hoặc email của người dùng mới
     *         đã tồn tại trong hệ thống hay chưa. Nếu đã tồn tại, ném ngoại lệ để ngăn chặn việc tạo tài khoản trùng lặp.</li>
     *     <li><b>Mã hóa mật khẩu:</b> Mã hóa mật khẩu thô của người dùng mới bằng cách sử dụng {@code PasswordEncoder}
     *         để đảm bảo an toàn khi lưu trữ.</li>
     *     <li><b>Chuyển đổi DTO sang Entity:</b> Chuyển đổi đối tượng {@link UserDto} chứa thông tin đăng ký
     *         thành một đối tượng {@link User} Entity.</li>
     *     <li><b>Lưu User:</b> Lưu đối tượng {@code User} mới vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi Entity đã lưu sang DTO:</b> Chuyển đổi đối tượng {@code User} đã lưu trở lại thành một {@link UserDto}
     *         (thường là không bao gồm mật khẩu) để trả về cho client.</li>
     * </ol>
     *
     * @param userDto Đối tượng {@link UserDto} chứa thông tin đăng ký của người dùng mới.
     * @return {@link UserDto} của người dùng đã được tạo (không chứa mật khẩu).
     * @throws Exception có thể ném ra nếu username hoặc email đã tồn tại, hoặc các lỗi khác trong quá trình đăng ký.
     */
    UserDto registerNewUserAccount(UserDto userDto) throws Exception;
}