package com.nutrition.ai.nutritionaibackend.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Lớp kiểm thử đơn vị cho {@link AuthEntryPointJwt}.
 * Đảm bảo rằng khi một người dùng không được xác thực cố gắng truy cập tài nguyên bảo vệ,
 * {@code AuthEntryPointJwt} sẽ gửi phản hồi lỗi HTTP 401 (Unauthorized) chính xác.
 *
 * <p>Lớp này kiểm tra hành vi của phương thức {@code commence}, đây là điểm vào
 * khi xảy ra lỗi xác thực trong chuỗi bộ lọc bảo mật của Spring Security.
 * Việc xác minh này đảm bảo rằng các yêu cầu không có JWT hợp lệ hoặc
 * các lỗi xác thực khác được xử lý một cách nhất quán và cung cấp thông tin phản hồi
 * rõ ràng cho client.</p>
 *
 * @see AuthEntryPointJwt
 * @see HttpServletResponse#SC_UNAUTHORIZED
 */
@ExtendWith(MockitoExtension.class)
public class AuthEntryPointJwtTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    /**
     * Phương thức thiết lập được gọi trước mỗi bài kiểm thử.
     * Hiện tại không có thiết lập cụ thể nào cần thiết cho các mock.
     */
    @BeforeEach
    void setUp() {
        // Có thể thêm cài đặt bổ sung nếu cần trước mỗi bài kiểm thử
    }

    /**
     * Kiểm tra xem phương thức {@code commence} có gửi phản hồi lỗi 401 Unauthorized
     * với thông báo lỗi chính xác hay không.
     *
     * <p>Giả lập một {@code AuthenticationException} và sau đó gọi {@code commence}
     * để xác minh rằng {@code HttpServletResponse.sendError} được gọi với
     * mã trạng thái {@code HttpServletResponse.SC_UNAUTHORIZED} và một thông báo lỗi
     * được định nghĩa trước. Điều này đảm bảo rằng các client nhận được phản hồi
     * thích hợp khi xác thực thất bại.</p>
     *
     * @throws IOException      Nếu xảy ra lỗi I/O.
     * @throws ServletException Nếu xảy ra lỗi servlet.
     */
    @Test
    void commence_ShouldSendUnauthorizedError() throws IOException, ServletException {
        // Thiết lập hành vi của authException để trả về một thông báo lỗi cụ thể
        when(authException.getMessage()).thenReturn("Test Unauthorized Message");

        // Gọi phương thức đang được kiểm thử của AuthEntryPointJwt
        authEntryPointJwt.commence(request, response, authException);

        // Xác minh rằng response.sendError đã được gọi một lần với mã trạng thái 401 và thông báo lỗi "Error: Unauthorized"
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
