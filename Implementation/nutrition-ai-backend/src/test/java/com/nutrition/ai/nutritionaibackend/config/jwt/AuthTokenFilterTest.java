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

/**
 * Lớp kiểm thử đơn vị cho {@link AuthTokenFilter}.
 * Đảm bảo rằng bộ lọc JWT xử lý các yêu cầu HTTP một cách chính xác,
 * bao gồm trích xuất, xác thực JWT và thiết lập ngữ cảnh bảo mật Spring Security.
 *
 * <p>Các bài kiểm thử này bao gồm nhiều kịch bản khác nhau:
 * <ul>
 *     <li>JWT hợp lệ: Xác minh rằng ngữ cảnh bảo mật được đặt chính xác.</li>
 *     <li>Không có JWT hoặc JWT không hợp lệ: Đảm bảo rằng ngữ cảnh bảo mật không bị thay đổi.</li>
 *     <li>Ngoại lệ xảy ra trong quá trình xử lý: Xác minh rằng lỗi được xử lý và chuỗi lọc tiếp tục.</li>
 *     <li>Kiểm tra riêng phương thức {@code parseJwt} (private) bằng reflection.</li>
 * </ul>
 * Mục tiêu là đảm bảo rằng cơ chế xác thực dựa trên JWT hoạt động mạnh mẽ và an toàn.</p>
 *
 * @see AuthTokenFilter
 * @see JwtUtils
 * @see UserDetailsServiceImpl
 */
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

    /**
     * Thiết lập môi trường kiểm thử trước mỗi bài kiểm thử.
     * Khởi tạo một đối tượng {@code UserDetails} giả và đặt {@code SecurityContextHolder}
     * với một {@code SecurityContext} mock để kiểm soát ngữ cảnh bảo mật.
     */
    @BeforeEach
    void setUp() {
        // Thiết lập UserDetails chung cho các test case
        userDetails = new User("testuser", "password", new ArrayList<>());
        // Đặt SecurityContextHolder với một SecurityContext mock để kiểm soát ngữ cảnh bảo mật
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Kiểm tra phương thức {@code doFilterInternal} với một JWT hợp lệ.
     *
     * <p>Xác minh rằng khi một JWT hợp lệ được cung cấp, bộ lọc sẽ trích xuất
     * thông tin người dùng, tải {@code UserDetails} và thiết lập
     * {@code Authentication} trong {@code SecurityContextHolder}.</p>
     *
     * @throws ServletException Nếu xảy ra lỗi servlet.
     * @throws IOException      Nếu xảy ra lỗi I/O.
     */
    @Test
    void doFilterInternal_validJwt_setsAuthentication() throws ServletException, IOException {
        // Giả định một JWT hợp lệ và các hành vi liên quan từ jwtUtils và userDetailsService
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

    /**
     * Kiểm tra phương thức {@code doFilterInternal} khi không có JWT trong tiêu đề Authorization.
     *
     * <p>Xác minh rằng nếu không có JWT, ngữ cảnh bảo mật không bị thay đổi
     * và yêu cầu vẫn được chuyển tiếp qua chuỗi lọc.</p>
     *
     * @throws ServletException Nếu xảy ra lỗi servlet.
     * @throws IOException      Nếu xảy ra lỗi I/O.
     */
    @Test
    void doFilterInternal_noJwt_noAuthentication() throws ServletException, IOException {
        // Giả định không có header Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt với Authentication
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Kiểm tra phương thức {@code doFilterInternal} với một JWT không hợp lệ.
     *
     * <p>Xác minh rằng nếu JWT không hợp lệ, ngữ cảnh bảo mật không bị thay đổi
     * và yêu cầu vẫn được chuyển tiếp qua chuỗi lọc.</p>
     *
     * @throws ServletException Nếu xảy ra lỗi servlet.
     * @throws IOException      Nếu xảy ra lỗi I/O.
     */
    @Test
    void doFilterInternal_invalidJwt_noAuthentication() throws ServletException, IOException {
        // Giả định một JWT không hợp lệ
        String jwt = "invalidJwtToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt với Authentication
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Kiểm tra phương thức {@code doFilterInternal} khi một ngoại lệ xảy ra
     * trong quá trình xử lý JWT.
     *
     * <p>Xác minh rằng nếu có lỗi trong quá trình xử lý JWT (ví dụ: không thể lấy
     * tên người dùng), ngữ cảnh bảo mật không bị thay đổi và yêu cầu vẫn được
     * chuyển tiếp qua chuỗi lọc.</p>
     *
     * @throws ServletException Nếu xảy ra lỗi servlet.
     * @throws IOException      Nếu xảy ra lỗi I/O.
     */
    @Test
    void doFilterInternal_exceptionOccurs_noAuthenticationAndLogsError() throws ServletException, IOException {
        // Giả định một ngoại lệ xảy ra trong quá trình xử lý JWT
        String jwt = "validJwtToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        // Giả lập hành vi của jwtUtils.getUserNameFromJwtToken để trả về null (hoặc ném ngoại lệ)
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(null);

        // Thực thi filter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Xác minh rằng SecurityContextHolder không được đặt với Authentication
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
        // Xác minh rằng filterChain được gọi để chuyển tiếp request
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Kiểm tra phương thức private {@code parseJwt} khi tiêu đề Authorization là null.
     *
     * <p>Sử dụng reflection để truy cập và kiểm thử phương thức private này.</p>
     *
     * @throws NoSuchMethodException       Nếu phương thức không tìm thấy.
     * @throws InvocationTargetException Nếu phương thức được gọi ném một ngoại lệ.
     * @throws IllegalAccessException      Nếu không thể truy cập phương thức.
     */
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

    /**
     * Kiểm tra phương thức private {@code parseJwt} khi tiêu đề Authorization
     * không bắt đầu bằng "Bearer ".
     *
     * <p>Sử dụng reflection để truy cập và kiểm thử phương thức private này.</p>
     *
     * @throws NoSuchMethodException       Nếu phương thức không tìm thấy.
     * @throws InvocationTargetException Nếu phương thức được gọi ném một ngoại lệ.
     * @throws IllegalAccessException      Nếu không thể truy cập phương thức.
     */
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

    /**
     * Kiểm tra phương thức private {@code parseJwt} với tiêu đề Authorization
     * hợp lệ chứa tiền tố "Bearer ".
     *
     * <p>Sử dụng reflection để truy cập và kiểm thử phương thức private này.</p>
     *
     * @throws NoSuchMethodException       Nếu phương thức không tìm thấy.
     * @throws InvocationTargetException Nếu phương thức được gọi ném một ngoại lệ.
     * @throws IllegalAccessException      Nếu không thể truy cập phương thức.
     */
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
