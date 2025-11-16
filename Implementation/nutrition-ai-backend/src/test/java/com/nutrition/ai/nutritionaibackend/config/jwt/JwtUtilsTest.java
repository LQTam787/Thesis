package com.nutrition.ai.nutritionaibackend.config.jwt;

import com.nutrition.ai.nutritionaibackend.config.ApplicationProperties;
import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtUtilsTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private ApplicationProperties.Jwt jwtProperties;

    @InjectMocks
    private JwtUtils jwtUtils;

    private final String TEST_SECRET = "testSecretKeyForJwtAuthenticationVeryLongStringIndeed";
    private final long JWT_EXPIRATION_MS = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        when(applicationProperties.getJwt()).thenReturn(jwtProperties);
        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        when(jwtProperties.getJwtExpirationMs()).thenReturn(JWT_EXPIRATION_MS);
    }

    @Test
    void generateJwtToken_shouldReturnValidJwtToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userPrincipal = new UserDetailsImpl(1L, "testuser", "test@example.com", "password", null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().getTime() > claims.getIssuedAt().getTime());
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnCorrectUsername() {
        String username = "testuser";
        String token = createTestToken(username);

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateJwtToken_withValidToken_shouldReturnTrue() {
        String token = createTestToken("testuser");
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_withInvalidJwtToken_shouldReturnFalse() {
        String invalidToken = "invalid.jwt.token";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void validateJwtToken_withExpiredJwtToken_shouldReturnFalse() throws InterruptedException {
        String username = "testuser";
        // Create a token with a very short expiration to make it expire quickly
        when(jwtProperties.getJwtExpirationMs()).thenReturn(1L); // 1 millisecond
        JwtUtils expiredJwtUtils = new JwtUtils(applicationProperties);
        String expiredToken = expiredJwtUtils.generateJwtToken(mockAuthentication(username));

        // Wait for the token to expire
        Thread.sleep(100);

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    void validateJwtToken_withSignatureException_shouldReturnFalse() {
        // Tạo một token với chữ ký không hợp lệ
        String malformedToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("anotherSecretKeyThatIsNotTheSameSizeAsTestSecret")), SignatureAlgorithm.HS256) // Chữ ký khác
                .compact();

        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    void validateJwtToken_withMalformedJwtException_shouldReturnFalse() {
        String malformedJwt = "abc.def.ghi"; // Không phải JWT hợp lệ
        assertFalse(jwtUtils.validateJwtToken(malformedJwt));
    }

    @Test
    void validateJwtToken_withUnsupportedJwtException_shouldReturnFalse() {
        // Jwts.builder().compact() không có signature mặc định sẽ gây ra SignatureException chứ không phải UnsupportedJwtException
        // Để tạo UnsupportedJwtException, có thể cần một thư viện khác hoặc tạo một token không chuẩn
        // Hiện tại, chúng ta sẽ kiểm tra một token không có phần . (dot) thứ hai, làm cho nó không được hỗ trợ định dạng JWT tiêu chuẩn
        String unsupportedJwt = "header.payload"; // Thiếu phần signature
        assertFalse(jwtUtils.validateJwtToken(unsupportedJwt));
    }

    @Test
    void validateJwtToken_withIllegalArgumentException_shouldReturnFalse() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
    }

    private String createTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Authentication mockAuthentication(String username) {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userPrincipal = new UserDetailsImpl(1L, username, "test@example.com", "password", null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        return authentication;
    }
}
