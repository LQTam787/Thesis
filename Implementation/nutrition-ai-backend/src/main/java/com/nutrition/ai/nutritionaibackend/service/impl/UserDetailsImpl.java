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
 * Implementation of Spring Security's UserDetails interface.
 * Stores user information retrieved by UserDetailsService for authentication and authorization.
 * Lớp này triển khai giao diện UserDetails của Spring Security.
 * Nó lưu trữ thông tin chi tiết về người dùng (ID, username, email, password, authorities/roles)
 * được sử dụng trong quá trình xác thực và ủy quyền (authorization) của Spring Security.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    @JsonIgnore // Ngăn không cho password bị serialize (hiển thị) trong phản hồi JSON
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor được sử dụng để tạo đối tượng UserDetailsImpl.
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
     * Phương thức tĩnh để xây dựng UserDetailsImpl từ User Entity.
     * 1. Chuyển đổi tập hợp Role của User thành List<GrantedAuthority>.
     * 2. Tạo UserDetailsImpl mới với các thông tin của User và Authorities.
     *
     * @param user User Entity.
     * @return UserDetailsImpl.
     */
    public static UserDetailsImpl build(User user) {
        // 1. Chuyển đổi Roles thành GrantedAuthorities
        List<GrantedAuthority> authorities = user.getRoles().stream()
                // Tạo SimpleGrantedAuthority với tên ROLE (ví dụ: "ROLE_USER", "ROLE_ADMIN")
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // 2. Tạo và trả về đối tượng
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Trả về các quyền (roles) được cấp cho người dùng.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Các getters bổ sung (không phải của UserDetails gốc)
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Các phương thức của giao diện UserDetails

    /**
     * Trả về mật khẩu được sử dụng để xác thực người dùng.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Trả về tên người dùng được sử dụng để xác thực người dùng.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Cho biết liệu tài khoản của người dùng có hết hạn hay không.
     * Luôn trả về true (tài khoản không bao giờ hết hạn) trong triển khai này.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Cho biết liệu người dùng có bị khóa hay không.
     * Luôn trả về true (tài khoản không bị khóa) trong triển khai này.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Cho biết liệu thông tin xác thực (mật khẩu) của người dùng có hết hạn hay không.
     * Luôn trả về true (thông tin xác thực không hết hạn) trong triển khai này.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Cho biết liệu người dùng có được bật (enabled) hay không.
     * Luôn trả về true (người dùng được bật) trong triển khai này.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Kiểm tra sự bằng nhau (equality) giữa hai đối tượng UserDetailsImpl dựa trên ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}