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

    @BeforeEach
    void setUp() {
        // Inject mocks into securityConfig manually since it's not a Spring bean
        ReflectionTestUtils.setField(securityConfig, "userDetailsService", userDetailsService);
        ReflectionTestUtils.setField(securityConfig, "unauthorizedHandler", unauthorizedHandler);
    }

    @Test
    void authenticationJwtTokenFilter_ShouldReturnAuthTokenFilterInstance() {
        // When
        AuthTokenFilter authTokenFilter = securityConfig.authenticationJwtTokenFilter();

        // Then
        assertNotNull(authTokenFilter);
        assertTrue(authTokenFilter instanceof AuthTokenFilter);
    }

    @Test
    void authenticationProvider_ShouldReturnConfiguredDaoAuthenticationProvider() {
        // When
        DaoAuthenticationProvider authProvider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(authProvider);
        assertEquals(userDetailsService, ReflectionTestUtils.getField(authProvider, "userDetailsService"));
        assertNotNull(ReflectionTestUtils.getField(authProvider, "passwordEncoder"));
        assertTrue(ReflectionTestUtils.getField(authProvider, "passwordEncoder") instanceof BCryptPasswordEncoder);
    }

    @Test
    void authenticationManager_ShouldReturnAuthenticationManagerFromConfig() throws Exception {
        // Given
        AuthenticationManager expectedAuthManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedAuthManager);

        // When
        AuthenticationManager actualAuthManager = securityConfig.authenticationManager(authenticationConfiguration);

        // Then
        assertNotNull(actualAuthManager);
        assertEquals(expectedAuthManager, actualAuthManager);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoderInstance() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    @SuppressWarnings("unchecked")
    void filterChain_ShouldConfigureHttpSecurityAndReturnSecurityFilterChain() throws Exception {
        // Given
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Argument Captors for Customizers
        ArgumentCaptor<Customizer<CsrfConfigurer<HttpSecurity>>> csrfCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<ExceptionHandlingConfigurer<HttpSecurity>>> exceptionHandlingCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<SessionManagementConfigurer<HttpSecurity>>> sessionManagementCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        ArgumentCaptor<Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>> authorizeHttpRequestsCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);

        // Mock the intermediate methods to avoid NullPointerExceptions and capture customizers
        when(httpSecurity.csrf(csrfCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(exceptionHandlingCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(sessionManagementCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizerCaptor.capture())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any(DaoAuthenticationProvider.class))).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(AuthTokenFilter.class), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);

        // When
        SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurity);

        // Then
        assertNotNull(filterChain);
        verify(httpSecurity, times(1)).build(); // Verify build is called once at the end

        // Verify that customizers were captured
        assertNotNull(csrfCustomizerCaptor.getValue());
        assertNotNull(exceptionHandlingCustomizerCaptor.getValue());
        assertNotNull(sessionManagementCustomizerCaptor.getValue());
        assertNotNull(authorizeHttpRequestsCustomizerCaptor.getValue());

        // Now, apply the captured customizers to mock configurers and verify their behavior

        // 1. Verify CSRF configuration
        CsrfConfigurer<HttpSecurity> mockCsrfConfigurer = (CsrfConfigurer<HttpSecurity>) mock(CsrfConfigurer.class);
        csrfCustomizerCaptor.getValue().customize(mockCsrfConfigurer);
        verify(mockCsrfConfigurer, times(1)).disable();

        // 2. Verify Exception Handling configuration
        ExceptionHandlingConfigurer<HttpSecurity> mockExceptionHandlingConfigurer = (ExceptionHandlingConfigurer<HttpSecurity>) mock(ExceptionHandlingConfigurer.class);
        exceptionHandlingCustomizerCaptor.getValue().customize(mockExceptionHandlingConfigurer);
        verify(mockExceptionHandlingConfigurer, times(1)).authenticationEntryPoint(unauthorizedHandler);

        // 3. Verify Session Management configuration
        SessionManagementConfigurer<HttpSecurity> mockSessionManagementConfigurer = (SessionManagementConfigurer<HttpSecurity>) mock(SessionManagementConfigurer.class);
        sessionManagementCustomizerCaptor.getValue().customize(mockSessionManagementConfigurer);
        verify(mockSessionManagementConfigurer, times(1)).sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 4. Verify Authorization configuration
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry mockAuthorizeHttpRequestsRegistry = mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl mockAuthorizedUrl = mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class, RETURNS_DEEP_STUBS);

        when(mockAuthorizeHttpRequestsRegistry.requestMatchers(any(String[].class))).thenReturn(mockAuthorizedUrl);
        when(mockAuthorizeHttpRequestsRegistry.anyRequest()).thenReturn(mockAuthorizedUrl);

        authorizeHttpRequestsCustomizerCaptor.getValue().customize(mockAuthorizeHttpRequestsRegistry);

        // Verify that requestMatchers and permitAll were called correctly
        verify(mockAuthorizeHttpRequestsRegistry).requestMatchers("/api/auth/**", "/api/test/**");
        verify(mockAuthorizedUrl).permitAll();

        // Verify that requestMatchers and hasRole were called correctly
        verify(mockAuthorizeHttpRequestsRegistry).requestMatchers("/api/admin/**");
        verify(mockAuthorizedUrl).hasRole("ADMIN");

        // Verify that anyRequest and authenticated were called correctly
        verify(mockAuthorizeHttpRequestsRegistry).anyRequest();
        verify(mockAuthorizedUrl).authenticated();

        // Verify other top-level calls on httpSecurity that are not part of customizers
        verify(httpSecurity).authenticationProvider(any(DaoAuthenticationProvider.class));
        verify(httpSecurity).addFilterBefore(any(AuthTokenFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
    }
}
