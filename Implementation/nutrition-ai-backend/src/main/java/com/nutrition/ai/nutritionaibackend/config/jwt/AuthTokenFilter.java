package com.nutrition.ai.nutritionaibackend.config.jwt;

import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter này mở rộng OncePerRequestFilter để đảm bảo nó chỉ được chạy một lần
 * cho mỗi request HTTP. Nó chịu trách nhiệm trích xuất và xác thực JWT
 * trong mọi request.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils; // Tiện ích để thao tác với JWT

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Dịch vụ để tải thông tin người dùng

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * Logic chính của Filter: Được gọi cho mỗi request đến ứng dụng.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Trích xuất JWT từ Header Authorization của request
            String jwt = parseJwt(request);

            // 2. Kiểm tra nếu JWT tồn tại và hợp lệ
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3. Lấy tên người dùng (username) từ token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Tải thông tin người dùng (UserDetails) từ cơ sở dữ liệu/dịch vụ
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 5. Tạo một đối tượng xác thực (Authentication) cho Spring Security
                // Đối tượng này đại diện cho người dùng đã được xác thực
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                
                // 6. Đặt chi tiết xác thực (ví dụ: địa chỉ IP, session ID)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Thiết lập đối tượng Authentication vào SecurityContextHolder
                // Đây là bước quan trọng nhất: Nó cho Spring Security biết rằng
                // người dùng đã được xác thực thành công cho request này.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Ghi log nếu có lỗi xảy ra trong quá trình xác thực/thiết lập ngữ cảnh
            logger.error("Cannot set user authentication: {}", e);
        }

        // Chuyển request đến filter tiếp theo trong chuỗi hoặc đến đích cuối cùng (Controller)
        filterChain.doFilter(request, response);
    }

    /**
     * Phương thức tiện ích để trích xuất JWT từ Header Authorization.
     * JWT được mong đợi ở định dạng: "Bearer <token>".
     * @param request Yêu cầu HTTP
     * @return Chuỗi JWT, hoặc null nếu không tìm thấy.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // Kiểm tra xem header có tồn tại, có văn bản và bắt đầu bằng "Bearer " không
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Trích xuất chuỗi token (bỏ qua "Bearer " và khoảng trắng)
            return headerAuth.substring(7);
        }

        return null;
    }
}