package com.nutrition.ai.nutritionaibackend.config;

import com.nutrition.ai.nutritionaibackend.config.jwt.AuthEntryPointJwt;
import com.nutrition.ai.nutritionaibackend.config.jwt.AuthTokenFilter;
import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Lớp kiểm thử đơn vị cho {@link SecurityConfig}.
 * Đảm bảo rằng cấu hình bảo mật của ứng dụng được thiết lập chính xác,
 * bao gồm các thành phần như bộ lọc JWT, nhà cung cấp xác thực, trình mã hóa mật khẩu
 * và chuỗi bộ lọc bảo mật tổng thể ({@link SecurityFilterChain}).
 *
 * <p>Các bài kiểm thử này sử dụng Mockito để giả lập các dependency của {@code SecurityConfig}
 * và xác minh hành vi của từng phương thức cấu hình. Mục tiêu là đảm bảo rằng
 * {@code SecurityConfig} tạo ra các bean Spring Security đúng với mong đợi
 * và cấu hình {@code HttpSecurity} một cách chính xác.</p>
 *
 * @see SecurityConfig
 * @see AuthTokenFilter
 * @see AuthEntryPointJwt
 * @see UserDetailsServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AuthEntryPointJwt unauthorizedHandler;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    /**
     * Thiết lập môi trường kiểm thử trước mỗi bài kiểm thử.
     * Tiêm các mock vào {@code securityConfig} một cách thủ công vì nó không phải là
     * một Spring bean trong ngữ cảnh kiểm thử này.
     */
    @BeforeEach
    void setUp() {
        // Tiêm mock userDetailsService vào securityConfig
        ReflectionTestUtils.setField(securityConfig, "userDetailsService", userDetailsService);
        // Tiêm mock unauthorizedHandler vào securityConfig
        ReflectionTestUtils.setField(securityConfig, "unauthorizedHandler", unauthorizedHandler);
    }

    /**
     * Kiểm tra xem phương thức {@code authenticationJwtTokenFilter} có trả về
     * một instance của {@link AuthTokenFilter} hay không.
     *
     * <p>Xác minh rằng bộ lọc xác thực JWT được tạo và có thể sử dụng được
     * trong chuỗi bảo mật.</p>
     */
    @Test
    void authenticationJwtTokenFilter_ShouldReturnAuthTokenFilterInstance() {
        // Khi phương thức authenticationJwtTokenFilter được gọi
        AuthTokenFilter authTokenFilter = securityConfig.authenticationJwtTokenFilter();

        // Thì
        // Đảm bảo đối tượng trả về không null
        assertNotNull(authTokenFilter);
        // Đảm bảo đối tượng trả về là một instance của AuthTokenFilter
        assertTrue(authTokenFilter instanceof AuthTokenFilter);
    }

    /**
     * Kiểm tra xem phương thức {@code authenticationProvider} có trả về một
     * {@link DaoAuthenticationProvider} được cấu hình đúng cách hay không.
     *
     * <p>Xác minh rằng {@code DaoAuthenticationProvider} sử dụng {@code userDetailsService}
     * và {@code passwordEncoder} chính xác để xác thực người dùng.</p>
     */
    @Test
    void authenticationProvider_ShouldReturnConfiguredDaoAuthenticationProvider() {
        // Khi phương thức authenticationProvider được gọi
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Thì
        // Đảm bảo đối tượng trả về không null
        assertNotNull(authProvider);
        // Đảm bảo userDetailsService được tiêm chính xác
        assertEquals(userDetailsService, ReflectionTestUtils.getField(authProvider, "userDetailsService"));
        // Đảm bảo passwordEncoder không null
        assertNotNull(ReflectionTestUtils.getField(authProvider, "passwordEncoder"));
        // Đảm bảo passwordEncoder là một instance của BCryptPasswordEncoder
        assertTrue(ReflectionTestUtils.getField(authProvider, "passwordEncoder") instanceof BCryptPasswordEncoder);
    }

    /**
     * Kiểm tra xem phương thức {@code authenticationManager} có trả về
     * {@link AuthenticationManager} từ {@link AuthenticationConfiguration} đã cho hay không.
     *
     * @throws Exception nếu có lỗi xảy ra trong quá trình lấy AuthenticationManager.
     */
    @Test
    void authenticationManager_ShouldReturnAuthenticationManagerFromConfig() throws Exception {
        // Cho trước
        // Tạo một mock AuthenticationManager mong đợi
        AuthenticationManager expectedAuthManager = mock(AuthenticationManager.class);
        // Giả lập hành vi của authenticationConfiguration để trả về mock AuthenticationManager
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedAuthManager);

        // Khi phương thức authenticationManager được gọi
        AuthenticationManager actualAuthManager = securityConfig.authenticationManager(authenticationConfiguration);

        // Thì
        // Đảm bảo đối tượng trả về không null
        assertNotNull(actualAuthManager);
        // Đảm bảo AuthenticationManager trả về giống với cái mong đợi
        assertEquals(expectedAuthManager, actualAuthManager);
        // Xác minh rằng getAuthenticationManager() đã được gọi một lần
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    /**
     * Kiểm tra xem phương thức {@code passwordEncoder} có trả về một
     * {@link BCryptPasswordEncoder} instance hay không.
     *
     * <p>Xác minh rằng trình mã hóa mật khẩu được sử dụng là {@code BCryptPasswordEncoder}
     * đảm bảo an toàn cho việc lưu trữ mật khẩu.</p>
     */
    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoderInstance() {
        // Khi phương thức passwordEncoder được gọi
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Thì
        // Đảm bảo đối tượng trả về không null
        assertNotNull(passwordEncoder);
        // Đảm bảo đối tượng trả về là một instance của BCryptPasswordEncoder
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    /**
     * Kiểm tra xem phương thức {@code filterChain} có cấu hình {@link HttpSecurity}
     * đúng cách và trả về một {@link SecurityFilterChain} hay không.
     *
     * <p>Bài kiểm thử này xác minh nhiều khía cạnh của cấu hình {@code HttpSecurity}:
     * <ul>
     *     <li>Tắt CSRF (Cross-Site Request Forgery).</li>
     *     <li>Cấu hình xử lý ngoại lệ với {@code unauthorizedHandler}.</li>
     *     <li>Thiết lập chính sách tạo phiên là {@code STATELESS}.</li>
     *     <li>Cấu hình quy tắc ủy quyền cho các URL cụ thể (ví dụ: cho phép tất cả cho auth và test, yêu cầu vai trò ADMIN cho admin, và yêu cầu xác thực cho tất cả các request khác).</li>
     *     <li>Đăng ký {@code DaoAuthenticationProvider}.</li>
     *     <li>Thêm {@code AuthTokenFilter} trước {@code UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     * Sử dụng {@code ArgumentCaptor} để nắm bắt và kiểm tra các {@code Customizer} được áp dụng.</p>
     *
     * @throws Exception nếu có lỗi trong quá trình cấu hình HttpSecurity.
     */
    @Test
    @SuppressWarnings("unchecked")
    void filterChain_ShouldConfigureHttpSecurityAndReturnSecurityFilterChain() throws Exception {
        // Cho trước
        // Tạo một mock HttpSecurity với hành vi trả về sâu
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Argument Captors để nắm bắt các Customizer được truyền vào HttpSecurity
        ArgumentCaptor<Customizer<CsrfConfigurer<HttpSecurity>>> csrfCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<ExceptionHandlingConfigurer<HttpSecurity>>> exceptionHandlingCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<SessionManagementConfigurer<HttpSecurity>>> sessionManagementCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>> authorizeHttpRequestsCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);

        // Giả lập các phương thức trung gian của HttpSecurity để tránh NullPointerExceptions và nắm bắt customizers
        when(httpSecurity.csrf(csrfCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(exceptionHandlingCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(sessionManagementCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any(DaoAuthenticationProvider.class))).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(AuthTokenFilter.class), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);

        // Khi phương thức filterChain được gọi
        SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurity);

        // Thì
        // Đảm bảo SecurityFilterChain không null
        assertNotNull(filterChain);
        // Xác minh rằng phương thức build() của HttpSecurity đã được gọi một lần
        verify(httpSecurity, times(1)).build();

        // Xác minh rằng các customizer đã được nắm bắt
        assertNotNull(csrfCustomizerCaptor.getValue());
        assertNotNull(exceptionHandlingCustomizerCaptor.getValue());
        assertNotNull(sessionManagementCustomizerCaptor.getValue());
        assertNotNull(authorizeHttpRequestsCustomizerCaptor.getValue());

        // Bây giờ, áp dụng các customizer đã nắm bắt cho các configurers giả lập và xác minh hành vi của chúng

        // 1. Xác minh cấu hình CSRF
        // Tạo một mock cho CsrfConfigurer
        CsrfConfigurer<HttpSecurity> mockCsrfConfigurer = (CsrfConfigurer<HttpSecurity>) mock(CsrfConfigurer.class);
        // Áp dụng customizer đã nắm bắt
        csrfCustomizerCaptor.getValue().customize(mockCsrfConfigurer);
        // Xác minh rằng phương thức disable() đã được gọi một lần
        verify(mockCsrfConfigurer, times(1)).disable();

        // 2. Xác minh cấu hình xử lý ngoại lệ
        // Tạo một mock cho ExceptionHandlingConfigurer
        ExceptionHandlingConfigurer<HttpSecurity> mockExceptionHandlingConfigurer = (ExceptionHandlingConfigurer<HttpSecurity>) mock(ExceptionHandlingConfigurer.class);
        // Áp dụng customizer đã nắm bắt
        exceptionHandlingCustomizerCaptor.getValue().customize(mockExceptionHandlingConfigurer);
        // Xác minh rằng authenticationEntryPoint() đã được gọi với unauthorizedHandler
        verify(mockExceptionHandlingConfigurer, times(1)).authenticationEntryPoint(unauthorizedHandler);

        // 3. Xác minh cấu hình quản lý phiên
        // Tạo một mock cho SessionManagementConfigurer
        SessionManagementConfigurer<HttpSecurity> mockSessionManagementConfigurer = (SessionManagementConfigurer<HttpSecurity>) mock(SessionManagementConfigurer.class);
        // Áp dụng customizer đã nắm bắt
        sessionManagementCustomizerCaptor.getValue().customize(mockSessionManagementConfigurer);
        // Xác minh rằng sessionCreationPolicy() đã được gọi với STATELESS
        verify(mockSessionManagementConfigurer, times(1)).sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 4. Xác minh cấu hình ủy quyền (AuthorizeHttpRequests)
        // Tạo một mock cho AuthorizationManagerRequestMatcherRegistry
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry mockAuthorizeHttpRequestsRegistry = mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        // Tạo một mock cho AuthorizedUrl (để chuỗi phương thức)
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl mockAuthorizedUrl = mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class, RETURNS_DEEP_STUBS);

        // Giả lập hành vi của requestMatchers và anyRequest để trả về mockAuthorizedUrl
        when(mockAuthorizeHttpRequestsRegistry.requestMatchers(any(String[].class))).thenReturn(mockAuthorizedUrl);
        when(mockAuthorizeHttpRequestsRegistry.anyRequest()).thenReturn(mockAuthorizedUrl);

        // Áp dụng customizer đã nắm bắt
        authorizeHttpRequestsCustomizerCaptor.getValue().customize(mockAuthorizeHttpRequestsRegistry);

        // Xác minh rằng requestMatchers("/api/auth/**", "/api/test/**") đã được gọi chính xác
        verify(mockAuthorizeHttpRequestsRegistry).requestMatchers("/api/auth/**", "/api/test/**");
        // Xác minh rằng permitAll() đã được gọi trên các request đó
        verify(mockAuthorizedUrl).permitAll();

        // Xác minh rằng requestMatchers("/api/admin/**") đã được gọi chính xác
        verify(mockAuthorizeHttpRequestsRegistry).requestMatchers("/api/admin/**");
        // Xác minh rằng hasRole("ADMIN") đã được gọi trên các request đó
        verify(mockAuthorizedUrl).hasRole("ADMIN");

        // Xác minh rằng anyRequest() đã được gọi chính xác
        verify(mockAuthorizeHttpRequestsRegistry).anyRequest();
        // Xác minh rằng authenticated() đã được gọi trên các request đó
        verify(mockAuthorizedUrl).authenticated();

        // Xác minh các lời gọi cấp cao nhất khác trên httpSecurity không phải là một phần của customizer
        verify(httpSecurity).authenticationProvider(any(DaoAuthenticationProvider.class));
        verify(httpSecurity).addFilterBefore(any(AuthTokenFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
    }
}
