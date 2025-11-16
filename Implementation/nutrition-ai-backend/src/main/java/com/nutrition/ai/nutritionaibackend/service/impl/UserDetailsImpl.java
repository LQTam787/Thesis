package com.nutrition.ai.nutritionaibackend.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Lớp triển khai {@link org.springframework.security.core.userdetails.UserDetails} của Spring Security.
 * Lớp này chịu trách nhiệm lưu trữ thông tin chi tiết về người dùng (như ID, tên người dùng, email, mật khẩu và quyền hạn)
 * được truy xuất bởi {@link UserDetailsService} trong quá trình xác thực và ủy quyền của Spring Security.
 * Nó cung cấp các phương thức cần thiết để Spring Security xác định trạng thái tài khoản và các quyền của người dùng.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    /**
     * Mật khẩu của người dùng. Được đánh dấu {@link JsonIgnore} để ngăn không cho mật khẩu bị hiển thị
     * trong bất kỳ phản hồi JSON nào được trả về bởi API, tăng cường bảo mật.
     */
    @JsonIgnore // Ngăn không cho password bị serialize (hiển thị) trong phản hồi JSON
    private String password;

    /**
     * Tập hợp các quyền (authorities/roles) được cấp cho người dùng.
     * Mỗi quyền được biểu diễn bởi một đối tượng kế thừa {@link GrantedAuthority}.
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Hàm tạo (Constructor) để khởi tạo một đối tượng {@link UserDetailsImpl} với các thông tin chi tiết về người dùng.
     *
     * @param id ID duy nhất của người dùng.
     * @param username Tên đăng nhập của người dùng.
     * @param email Địa chỉ email của người dùng.
     * @param password Mật khẩu đã được mã hóa của người dùng.
     * @param authorities Tập hợp các quyền hạn/vai trò của người dùng.
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Phương thức tĩnh factory để xây dựng một đối tượng {@link UserDetailsImpl} từ một {@link User} Entity.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Chuyển đổi Roles thành GrantedAuthorities:</b> Lấy tập hợp các {@link com.nutrition.ai.nutritionaibackend.model.domain.Role} từ đối tượng {@link User}.
     *         Mỗi {@link com.nutrition.ai.nutritionaibackend.model.domain.Role} được ánh xạ thành một {@link SimpleGrantedAuthority},
     *         sử dụng tên của Role (ví dụ: "ROLE_USER", "ROLE_ADMIN") làm tên quyền hạn.</li>
     *     <li><b>Tạo UserDetailsImpl:</b> Tạo một instance mới của {@link UserDetailsImpl} bằng cách truyền
     *         ID, tên người dùng, email, mật khẩu và danh sách các {@link GrantedAuthority} đã chuyển đổi.</li>
     * </ol>
     *
     * @param user Đối tượng {@link User} Entity chứa thông tin người dùng.
     * @return Một đối tượng {@link UserDetailsImpl} tương ứng.
     */
    public static UserDetailsImpl build(User user) {
        // 1. Chuyển đổi các Roles của User thành List<GrantedAuthority>
        List<GrantedAuthority> authorities = user.getRoles().stream()
                // Ánh xạ mỗi Role thành một SimpleGrantedAuthority với tên ROLE (ví dụ: "ROLE_USER", "ROLE_ADMIN")
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // 2. Tạo và trả về đối tượng UserDetailsImpl mới
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Trả về tập hợp các quyền (granted authorities) được cấp cho người dùng.
     * Các quyền này được sử dụng bởi Spring Security để xác định các tài nguyên
     * mà người dùng có quyền truy cập.
     *
     * @return {@link Collection} các {@link GrantedAuthority} của người dùng.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Các phương thức getter bổ sung không thuộc giao diện UserDetails gốc, nhưng hữu ích để truy cập thông tin người dùng.
    /**
     * Trả về ID duy nhất của người dùng.
     *
     * @return ID của người dùng.
     */
    public Long getId() {
        return id;
    }

    /**
     * Trả về địa chỉ email của người dùng.
     *
     * @return Địa chỉ email của người dùng.
     */
    public String getEmail() {
        return email;
    }

    // Các phương thức của giao diện UserDetails

    /**
     * Trả về mật khẩu được sử dụng để xác thực người dùng.
     * Mật khẩu này đã được mã hóa và được đánh dấu {@link JsonIgnore} để không bị hiển thị.
     *
     * @return Mật khẩu của người dùng.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Trả về tên người dùng được sử dụng để xác thực người dùng.
     * Đây là tên đăng nhập duy nhất của người dùng.
     *
     * @return Tên đăng nhập của người dùng.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Cho biết liệu tài khoản của người dùng có hết hạn (expired) hay không.
     * <p><b>Luồng hoạt động:</b></p>
     * <p>Trong triển khai hiện tại, phương thức này luôn trả về {@code true},
     * ngụ ý rằng tài khoản người dùng không bao giờ hết hạn.</p>
     *
     * @return {@code true} nếu tài khoản không hết hạn, {@code false} nếu ngược lại.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Cho biết liệu người dùng có bị khóa (locked) hay không.
     * <p><b>Luồng hoạt động:</b></p>
     * <p>Trong triển khai hiện tại, phương thức này luôn trả về {@code true},
     * ngụ ý rằng tài khoản người dùng không bao giờ bị khóa.</p>
     *
     * @return {@code true} nếu tài khoản không bị khóa, {@code false} nếu ngược lại.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Cho biết liệu thông tin xác thực (mật khẩu) của người dùng có hết hạn (expired) hay không.
     * <p><b>Luồng hoạt động:</b></p>
     * <p>Trong triển khai hiện tại, phương thức này luôn trả về {@code true},
     * ngụ ý rằng thông tin xác thực của người dùng không bao giờ hết hạn.</p>
     *
     * @return {@code true} nếu thông tin xác thực không hết hạn, {@code false} nếu ngược lại.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Cho biết liệu người dùng có được bật (enabled) hay không.
     * <p><b>Luồng hoạt động:</b></p>
     * <p>Trong triển khai hiện tại, phương thức này luôn trả về {@code true},
     * ngụ ý rằng tài khoản người dùng luôn được kích hoạt và có thể sử dụng.</p>
     *
     * @return {@code true} nếu người dùng được bật, {@code false} nếu ngược lại.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Kiểm tra sự bằng nhau (equality) giữa hai đối tượng {@link UserDetailsImpl} dựa trên ID của chúng.
     * Hai đối tượng được coi là bằng nhau nếu chúng có cùng ID.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Kiểm tra xem đối tượng truyền vào có phải là chính đối tượng hiện tại không (kiểm tra tham chiếu).</li>
     *     <li>Kiểm tra xem đối tượng truyền vào có phải là {@code null} hoặc không cùng lớp hay không.</li>
     *     <li>Ép kiểu đối tượng truyền vào thành {@link UserDetailsImpl}.</li>
     *     <li>So sánh trường {@code id} của hai đối tượng bằng {@link Objects#equals(Object, Object)} để đảm bảo so sánh an toàn null.</li>
     * </ol>
     *
     * @param o Đối tượng để so sánh với đối tượng hiện tại.
     * @return {@code true} nếu các đối tượng bằng nhau, {@code false} nếu ngược lại.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    /**
     * Trả về giá trị mã băm (hash code) cho đối tượng {@link UserDetailsImpl} dựa trên ID của nó.
     * Phương thức này nhất quán với phương thức {@link #equals(Object)}.
     *
     * @return Giá trị mã băm của đối tượng.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}