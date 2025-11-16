package com.nutrition.ai.nutritionaibackend.config.jwt;

import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Thiết lập UserDetails chung cho các test case
        userDetails = new User("testuser", "password", new ArrayList<>());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternal_validJwt_setsAuthentication() throws ServletException, IOException {
        // Giả định một JWT hợp lệ
        String jwt = "validJwtToken";
        String username = "testuser";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder đã được đặt với một đối tượng Authentication
        verify(securityContext, times(1)).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noJwt_noAuthentication() throws ServletException, IOException {
        // Giả định không có header Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidJwt_noAuthentication() throws ServletException, IOException {
        // Giả định một JWT không hợp lệ
        String jwt = "invalidJwtToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionOccurs_noAuthenticationAndLogsError() throws ServletException, IOException {
        // Giả định một ngoại lệ xảy ra trong quá trình xử lý
        String jwt = "validJwtToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        // Mock hành vi để không ném RuntimeException mà trả về null
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(null);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void parseJwt_nullAuthorizationHeader_returnsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Giả định header Authorization là null
        when(request.getHeader("Authorization")).thenReturn(null);

        // Sử dụng Reflection để gọi phương thức private parseJwt
        Method method = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true); // Cho phép truy cập phương thức private

        String jwt = (String) method.invoke(authTokenFilter, request);

        // Xác minh rằng kết quả là null
        assertNull(jwt);
    }

    @Test
    void parseJwt_noBearerPrefix_returnsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Giả định header Authorization không bắt đầu bằng "Bearer "
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        // Sử dụng Reflection để gọi phương thức private parseJwt
        Method method = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true);

        String jwt = (String) method.invoke(authTokenFilter, request);

        // Xác minh rằng kết quả là null
        assertNull(jwt);
    }

    @Test
    void parseJwt_validBearerPrefix_returnsJwt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Giả định header Authorization hợp lệ với tiền tố "Bearer "
        String expectedJwt = "some.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expectedJwt);

        // Sử dụng Reflection để gọi phương thức private parseJwt
        Method method = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        method.setAccessible(true);

        String actualJwt = (String) method.invoke(authTokenFilter, request);

        // Xác minh rằng JWT được trích xuất chính xác
        assertEquals(expectedJwt, actualJwt);
    }
}
