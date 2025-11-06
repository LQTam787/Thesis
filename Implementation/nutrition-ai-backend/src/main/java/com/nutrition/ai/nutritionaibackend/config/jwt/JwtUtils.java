package com.nutrition.ai.nutritionaibackend.config.jwt;

import com.nutrition.ai.nutritionaibackend.config.ApplicationProperties;
import com.nutrition.ai.nutritionaibackend.service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class (lớp tiện ích) để tạo, trích xuất thông tin và xác thực token JWT.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final ApplicationProperties applicationProperties;

    public JwtUtils(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Tạo một chuỗi JWT từ đối tượng Authentication (sau khi đăng nhập thành công).
     * @param authentication Đối tượng Authentication chứa thông tin người dùng đã xác thực.
     * @return Chuỗi JWT đã được tạo.
     */
    public String generateJwtToken(Authentication authentication) {
        // Lấy thông tin chi tiết người dùng (principal) từ đối tượng Authentication
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Xây dựng chuỗi JWT
        return Jwts.builder()
                // Đặt 'subject' của token là tên người dùng (username)
                .setSubject((userPrincipal.getUsername()))
                // Đặt thời gian phát hành (Issued At) là thời điểm hiện tại
                .setIssuedAt(new Date())
                // Đặt thời gian hết hạn (Expiration) bằng thời gian hiện tại + thời gian hết hạn được cấu hình
                .setExpiration(new Date((new Date()).getTime() + applicationProperties.getJwt().getJwtExpirationMs()))
                // Ký token bằng khóa bí mật (secret key) và thuật toán HS256
                .signWith(key(), SignatureAlgorithm.HS256)
                // Hoàn tất việc tạo token
                .compact();
    }

    /**
     * Lấy Key (khóa) dùng để ký và xác thực token JWT.
     * Khóa được tạo từ chuỗi secret đã được mã hóa Base64 trong cấu hình ứng dụng.
     * @return Khóa bí mật
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationProperties.getJwt().getJwtSecret()));
    }

    /**
     * Trích xuất tên người dùng (username) từ token JWT.
     * @param token Chuỗi JWT
     * @return Tên người dùng
     */
    public String getUserNameFromJwtToken(String token) {
        // Sử dụng JwtParser để phân tích (parse) token
        return Jwts.parserBuilder().setSigningKey(key()).build()
                // Phân tích JWS (JSON Web Signature) và lấy phần thân (body)
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Xác thực tính hợp lệ của token JWT (bao gồm chữ ký và thời gian hết hạn).
     * @param authToken Chuỗi JWT
     * @return true nếu token hợp lệ, false nếu có lỗi.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Cố gắng phân tích token. Nếu token không hợp lệ, một Exception sẽ được ném ra.
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            // Token không được cấu trúc đúng (ví dụ: không phải là JWT)
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // Token đã hết hạn
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Token không được hỗ trợ (ví dụ: JWT không có chữ ký, token JWE thay vì JWS)
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // Chuỗi JWT trống hoặc null
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}