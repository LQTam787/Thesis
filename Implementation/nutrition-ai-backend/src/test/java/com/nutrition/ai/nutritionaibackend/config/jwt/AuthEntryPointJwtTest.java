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

    @BeforeEach
    void setUp() {
        // Có thể thêm cài đặt bổ sung nếu cần trước mỗi bài kiểm thử
    }

    @Test
    void commence_ShouldSendUnauthorizedError() throws IOException, ServletException {
        // Thiết lập hành vi của authException
        when(authException.getMessage()).thenReturn("Test Unauthorized Message");

        // Gọi phương thức đang được kiểm thử
        authEntryPointJwt.commence(request, response, authException);

        // Xác minh rằng response.sendError đã được gọi với mã trạng thái và thông báo chính xác
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
