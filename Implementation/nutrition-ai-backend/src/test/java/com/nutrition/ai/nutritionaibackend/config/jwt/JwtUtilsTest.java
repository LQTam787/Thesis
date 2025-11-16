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

/**
 * Lớp kiểm thử đơn vị toàn diện cho {@link JwtUtils}.
 * Đảm bảo rằng {@code JwtUtils} có khả năng tạo, phân tích cú pháp và xác thực JWT
 * một cách chính xác trong nhiều kịch bản, bao gồm cả các trường hợp hợp lệ và không hợp lệ.
 *
 * <p>Các bài kiểm thử này bao gồm:
 * <ul>
 *     <li>Tạo JWT hợp lệ và xác minh nội dung của nó.</li>
 *     <li>Trích xuất tên người dùng từ JWT.</li>
 *     <li>Xác thực JWT hợp lệ.</li>
 *     <li>Xác thực các JWT không hợp lệ (hết hạn, chữ ký sai, định dạng sai, v.v.).</li>
 * </ul>
 * Mục tiêu là đảm bảo rằng các chức năng liên quan đến JWT hoạt động mạnh mẽ
 * và an toàn trong hệ thống xác thực.</p>
 *
 * @see JwtUtils
 * @see ApplicationProperties
 * @see UserDetailsImpl
 */
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

    /**
     * Thiết lập môi trường kiểm thử trước mỗi bài kiểm thử.
     * Giả lập hành vi của {@code ApplicationProperties} để cung cấp
     * các giá trị bí mật và thời gian hết hạn JWT thử nghiệm.
     */
    @BeforeEach
    void setUp() {
        // Khi applicationProperties.getJwt() được gọi, trả về jwtProperties mock
        when(applicationProperties.getJwt()).thenReturn(jwtProperties);
        // Khi jwtProperties.getJwtSecret() được gọi, trả về TEST_SECRET
        when(jwtProperties.getJwtSecret()).thenReturn(TEST_SECRET);
        // Khi jwtProperties.getJwtExpirationMs() được gọi, trả về JWT_EXPIRATION_MS
        when(jwtProperties.getJwtExpirationMs()).thenReturn(JWT_EXPIRATION_MS);
    }

    /**
     * Kiểm tra xem phương thức {@code generateJwtToken} có tạo ra một JWT hợp lệ
     * với các claims mong đợi (chủ đề, ngày phát hành, ngày hết hạn) hay không.
     */
    @Test
    void generateJwtToken_shouldReturnValidJwtToken() {
        // Tạo một mock Authentication và UserDetailsImpl
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userPrincipal = new UserDetailsImpl(1L, "testuser", "test@example.com", "password", null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Tạo JWT
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token, "Token không được null");

        // Phân tích cú pháp token và xác minh các claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testuser", claims.getSubject(), "Chủ đề của token phải là 'testuser'");
        assertNotNull(claims.getIssuedAt(), "Ngày phát hành không được null");
        assertNotNull(claims.getExpiration(), "Ngày hết hạn không được null");
        assertTrue(claims.getExpiration().getTime() > claims.getIssuedAt().getTime(), "Ngày hết hạn phải sau ngày phát hành");
    }

    /**
     * Kiểm tra xem phương thức {@code getUserNameFromJwtToken} có trích xuất
     * tên người dùng chính xác từ JWT hay không.
     */
    @Test
    void getUserNameFromJwtToken_shouldReturnCorrectUsername() {
        // Tạo một token thử nghiệm với tên người dùng
        String username = "testuser";
        String token = createTestToken(username);

        // Trích xuất tên người dùng và xác minh
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, extractedUsername, "Tên người dùng trích xuất phải khớp");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code true}
     * cho một JWT hợp lệ hay không.
     */
    @Test
    void validateJwtToken_withValidToken_shouldReturnTrue() {
        // Tạo một token thử nghiệm hợp lệ
        String token = createTestToken("testuser");
        // Xác minh rằng token hợp lệ
        assertTrue(jwtUtils.validateJwtToken(token), "Token hợp lệ phải trả về true");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * cho một JWT không hợp lệ (ví dụ: bị thay đổi, định dạng sai) hay không.
     */
    @Test
    void validateJwtToken_withInvalidJwtToken_shouldReturnFalse() {
        // Token không phải là JWT hợp lệ
        String invalidToken = "invalid.jwt.token";
        // Xác minh rằng token không hợp lệ trả về false
        assertFalse(jwtUtils.validateJwtToken(invalidToken), "Token không hợp lệ phải trả về false");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * cho một JWT đã hết hạn hay không.
     *
     * @throws InterruptedException Nếu luồng bị gián đoạn trong khi chờ token hết hạn.
     */
    @Test
    void validateJwtToken_withExpiredJwtToken_shouldReturnFalse() throws InterruptedException {
        String username = "testuser";
        // Tạo một token với thời gian hết hạn rất ngắn để nó hết hạn nhanh chóng
        when(jwtProperties.getJwtExpirationMs()).thenReturn(1L); // 1 mili giây
        // Tạo một instance JwtUtils mới với cấu hình hết hạn ngắn
        JwtUtils expiredJwtUtils = new JwtUtils(applicationProperties);
        String expiredToken = expiredJwtUtils.generateJwtToken(mockAuthentication(username));

        // Đợi cho token hết hạn
        Thread.sleep(100);

        // Xác minh rằng token đã hết hạn trả về false
        assertFalse(jwtUtils.validateJwtToken(expiredToken), "Token đã hết hạn phải trả về false");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * khi chữ ký JWT không hợp lệ hay không.
     */
    @Test
    void validateJwtToken_withSignatureException_shouldReturnFalse() {
        // Tạo một token với chữ ký không khớp với khóa bí mật đã cấu hình
        String malformedToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                // Ký bằng một khóa khác để tạo SignatureException
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("anotherSecretKeyThatIsNotTheSameSizeAsTestSecret")), SignatureAlgorithm.HS256)
                .compact();

        // Xác minh rằng token có chữ ký sai trả về false
        assertFalse(jwtUtils.validateJwtToken(malformedToken), "Token có chữ ký sai phải trả về false");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * cho một JWT bị định dạng sai (MalformedJwtException) hay không.
     */
    @Test
    void validateJwtToken_withMalformedJwtException_shouldReturnFalse() {
        // Chuỗi không phải là JWT hợp lệ về mặt định dạng
        String malformedJwt = "abc.def.ghi";
        // Xác minh rằng token bị định dạng sai trả về false
        assertFalse(jwtUtils.validateJwtToken(malformedJwt), "Token bị định dạng sai phải trả về false");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * cho một JWT không được hỗ trợ (UnsupportedJwtException) hay không.
     *
     * <p>Trong trường hợp này, chúng ta tạo một JWT thiếu phần chữ ký
     * để mô phỏng một định dạng không được hỗ trợ.</p>
     */
    @Test
    void validateJwtToken_withUnsupportedJwtException_shouldReturnFalse() {
        // Token thiếu phần signature, dẫn đến UnsupportedJwtException
        String unsupportedJwt = "header.payload";
        // Xác minh rằng token không được hỗ trợ trả về false
        assertFalse(jwtUtils.validateJwtToken(unsupportedJwt), "Token không được hỗ trợ phải trả về false");
    }

    /**
     * Kiểm tra xem phương thức {@code validateJwtToken} có trả về {@code false}
     * cho các đối số bất hợp pháp (IllegalArgumentException), ví dụ: chuỗi rỗng hoặc null.
     */
    @Test
    void validateJwtToken_withIllegalArgumentException_shouldReturnFalse() {
        // Xác minh rằng chuỗi rỗng trả về false
        assertFalse(jwtUtils.validateJwtToken(""), "Chuỗi JWT rỗng phải trả về false");
        // Xác minh rằng null trả về false
        assertFalse(jwtUtils.validateJwtToken(null), "JWT null phải trả về false");
    }

    /**
     * Phương thức tiện ích để tạo Key dùng để ký và xác minh JWT.
     *
     * @return Một đối tượng {@link Key} được tạo từ TEST_SECRET.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
    }

    /**
     * Phương thức tiện ích để tạo một JWT thử nghiệm.
     *
     * @param username Tên người dùng sẽ được đặt làm chủ đề (subject) của JWT.
     * @return Một chuỗi JWT đã được ký.
     */
    private String createTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Phương thức tiện ích để tạo một đối tượng {@link Authentication} giả.
     *
     * @param username Tên người dùng cho UserDetailsImpl.
     * @return Một đối tượng Authentication mock.
     */
    private Authentication mockAuthentication(String username) {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userPrincipal = new UserDetailsImpl(1L, username, "test@example.com", "password", null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        return authentication;
    }
}
