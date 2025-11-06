package com.nutrition.ai.nutritionaibackend.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component này xử lý việc từ chối các yêu cầu không được xác thực.
 * Nó được kích hoạt bất cứ khi nào một ngoại lệ AuthenticationException bị ném ra,
 * ví dụ: khi một người dùng cố gắng truy cập một tài nguyên yêu cầu xác thực
 * nhưng không cung cấp thông tin xác thực hợp lệ (ví dụ: không có JWT hoặc JWT không hợp lệ/hết hạn).
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Phương thức này được gọi khi một người dùng chưa được xác thực cố gắng truy cập
     * một tài nguyên được bảo vệ.
     * @param request Yêu cầu HTTP
     * @param response Phản hồi HTTP
     * @param authException Ngoại lệ xác thực đã xảy ra
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Ghi log lỗi để phục vụ việc debug
        logger.error("Unauthorized error: {}", authException.getMessage());
        
        // Trả về một lỗi HTTP 401 Unauthorized (Không được phép) cho client.
        // Đây là cách tiêu chuẩn để thông báo rằng người dùng cần xác thực.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}