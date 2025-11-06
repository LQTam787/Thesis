package com.nutrition.ai.nutritionaibackend.config;

import com.nutrition.ai.nutritionaibackend.config.jwt.AuthEntryPointJwt;
import com.nutrition.ai.nutritionaibackend.config.jwt.AuthTokenFilter;
import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Lớp cấu hình bảo mật chính cho ứng dụng Spring.
 * @EnableMethodSecurity: Kích hoạt bảo mật ở cấp độ phương thức (@PreAuthorize, @PostAuthorize, v.v.).
 * @RequiredArgsConstructor: Lombok tạo constructor với các tham số là final fields (userDetailsService, unauthorizedHandler).
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Dependency Injection (DI) các service cần thiết
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    /**
     * Bean để tạo và quản lý AuthTokenFilter (Bộ lọc JWT tùy chỉnh).
     * Bộ lọc này chịu trách nhiệm trích xuất JWT từ request và xác thực người dùng.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        // Luồng hoạt động: Bộ lọc này được chèn vào chuỗi bộ lọc bảo mật
        // và chạy trước UsernamePasswordAuthenticationFilter.
        return new AuthTokenFilter();
    }

    /**
     * Định nghĩa Authentication Provider (Nhà cung cấp xác thực) sử dụng DAO.
     * Nguyên lý hoạt động: Nó lấy UserDetails từ UserDetailsService
     * và sử dụng PasswordEncoder để so sánh mật khẩu.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Cấu hình dịch vụ tải thông tin người dùng
        authProvider.setUserDetailsService(userDetailsService);
        // Cấu hình bộ mã hóa mật khẩu
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean AuthenticationManager.
     * Nó là giao diện chính để thực hiện xác thực trong Spring Security.
     * Nó điều phối việc sử dụng các AuthenticationProvider đã cấu hình.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bean PasswordEncoder.
     * Sử dụng BCryptPasswordEncoder để mã hóa mật khẩu, đảm bảo mật khẩu được lưu trữ an toàn.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Định nghĩa SecurityFilterChain (Chuỗi Bộ lọc Bảo mật).
     * Đây là nơi cấu hình các quy tắc bảo mật cho HTTP request.
     *
     * @param http Đối tượng HttpSecurity để cấu hình các quy tắc.
     * @return Chuỗi Bộ lọc Bảo mật đã được xây dựng.
     * @throws Exception Lỗi cấu hình.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. Tắt CSRF (Thích hợp cho ứng dụng RESTful API stateless)
        http.csrf(csrf -> csrf.disable())
                // 2. Xử lý Exception: Sử dụng AuthEntryPointJwt để xử lý lỗi xác thực (401 Unauthorized)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // 3. Quản lý Session: Đặt chính sách tạo session là STATELESS
                // (Không sử dụng session HTTP để lưu trạng thái, phù hợp với JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 4. Ủy quyền yêu cầu HTTP (Authorization)
                .authorizeHttpRequests(auth ->
                        auth
                                // Cho phép truy cập không cần xác thực tới các endpoint Auth (đăng ký/đăng nhập)
                                .requestMatchers("/api/auth/**").permitAll()
                                // Cho phép truy cập không cần xác thực tới các endpoint Test
                                .requestMatchers("/api/test/**").permitAll()
                                // Tất cả các request khác PHẢI được xác thực (authenticated)
                                .anyRequest().authenticated()
                );

        // 5. Cấu hình sử dụng AuthenticationProvider đã định nghĩa
        http.authenticationProvider(authenticationProvider());

        // 6. CHÈN bộ lọc JWT tùy chỉnh (authenticationJwtTokenFilter) VÀO TRƯỚC
        // bộ lọc xác thực UsernamePasswordAuthenticationFilter mặc định của Spring Security.
        // Luồng hoạt động: Bộ lọc JWT sẽ chạy trước, xác thực bằng token.
        // Nếu token hợp lệ, nó thiết lập Context bảo mật.
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // 7. Xây dựng và trả về SecurityFilterChain
        return http.build();
    }
}