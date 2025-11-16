package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link org.springframework.security.core.userdetails.UserDetailsService} của Spring Security.
 * Lớp này chịu trách nhiệm tải thông tin chi tiết về người dùng (UserDetails) từ cơ sở dữ liệu dựa trên tên người dùng (username).
 * Thông tin này sau đó được Spring Security sử dụng trong quá trình xác thực (authentication) để xác minh danh tính của người dùng.
 * Nó sử dụng {@link UserRepository} để truy cập dữ liệu người dùng và {@link UserDetailsImpl} để xây dựng đối tượng UserDetails.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * {@code userRepository} là giao diện repository để truy cập dữ liệu của {@link User}.
     * Được tiêm thông qua constructor để tìm kiếm người dùng trong cơ sở dữ liệu.
     */
    private final UserRepository userRepository;

    /**
     * Tải thông tin chi tiết về người dùng ({@link UserDetails}) bằng tên người dùng (username).
     * Đây là phương thức cốt lõi của {@link UserDetailsService} được Spring Security gọi trong quá trình xác thực.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm User Entity:</b> Truy vấn {@code userRepository} để tìm đối tượng {@link User} trong cơ sở dữ liệu
     *         dựa trên {@code username} được cung cấp.</li>
     *     <li><b>Xử lý không tìm thấy:</b> Nếu không tìm thấy {@link User} nào với {@code username} đã cho, phương thức
     *         sẽ ném {@link UsernameNotFoundException}, cho biết quá trình xác thực không thành công.</li>
     *     <li><b>Chuyển đổi và Trả về:</b> Nếu tìm thấy người dùng, đối tượng {@link User} Entity sẽ được chuyển đổi
     *         thành đối tượng {@link UserDetailsImpl} (một triển khai của {@link UserDetails}) sử dụng phương thức
     *         {@link UserDetailsImpl#build(User)}. Đối tượng {@link UserDetailsImpl} này chứa tất cả thông tin
     *         cần thiết cho Spring Security để quản lý phiên của người dùng.</li>
     * </ol>
     * <p>Annotation {@code @Transactional} đảm bảo rằng toàn bộ thao tác tải người dùng này được thực hiện
     * trong một giao dịch cơ sở dữ liệu duy nhất, giúp duy trì tính nhất quán dữ liệu.</p>
     *
     * @param username Tên người dùng mà thông tin chi tiết cần được tải.
     * @return Một đối tượng {@link UserDetails} chứa thông tin người dùng và quyền hạn (authorities).
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng với tên người dùng đã cho.
     */
    @Override
    @Transactional // Đảm bảo toàn bộ quá trình tải được thực hiện trong một transaction
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm User Entity trong cơ sở dữ liệu bằng username
        User user = userRepository.findByUsername(username)
                // 2. Ném UsernameNotFoundException nếu User không tìm thấy
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // 3. Chuyển đổi User Entity thành UserDetailsImpl và trả về
        return UserDetailsImpl.build(user);
    }
}