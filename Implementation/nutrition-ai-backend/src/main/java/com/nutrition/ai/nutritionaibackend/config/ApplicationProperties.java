package com.nutrition.ai.nutritionaibackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Lớp cấu hình để liên kết các thuộc tính tùy chỉnh từ application.properties.
 * Điều này giúp cung cấp quyền truy cập an toàn kiểu vào các thuộc tính ứng dụng.
 */
@Configuration
@ConfigurationProperties
public class ApplicationProperties {

    private final Jwt jwt = new Jwt();
    private final AiService aiService = new AiService();

    public Jwt getJwt() {
        return jwt;
    }

    public AiService getAiService() {
        return aiService;
    }

    /**
     * Các thuộc tính liên quan đến cấu hình JWT.
     */
    @ConfigurationProperties(prefix = "nutrition.ai")
    public static class Jwt {
        private String jwtSecret;
        private long jwtExpirationMs;

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public long getJwtExpirationMs() {
            return jwtExpirationMs;
        }

        public void setJwtExpirationMs(long jwtExpirationMs) {
            this.jwtExpirationMs = jwtExpirationMs;
        }
    }

    /**
     * Các thuộc tính liên quan đến cấu hình AI service.
     */
    @ConfigurationProperties(prefix = "ai.service")
    public static class AiService {
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
