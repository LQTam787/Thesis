package com.nutrition.ai.nutritionaibackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Lớp cấu hình để liên kết các thuộc tính tùy chỉnh từ application.properties.
 * Lớp này hoạt động như một facade, cung cấp quyền truy cập vào các nhóm thuộc tính con.
 */
@Configuration
@EnableConfigurationProperties({ApplicationProperties.Jwt.class, ApplicationProperties.AiService.class})
public class ApplicationProperties {

    private final Jwt jwt;
    private final AiService aiService;

    public ApplicationProperties(Jwt jwt, AiService aiService) {
        this.jwt = jwt;
        this.aiService = aiService;
    }

    // Getter cho cấu hình JWT
    public Jwt getJwt() {
        return jwt;
    }

    // Getter cho cấu hình AI Service
    public AiService getAiService() {
        return aiService;
    }

    /**
     * Các thuộc tính liên quan đến cấu hình JWT.
     * @ConfigurationProperties(prefix = "nutrition.ai"): Spring sẽ liên kết
     * các thuộc tính có prefix là `nutrition.ai.` (ví dụ: `nutrition.ai.jwt-secret`)
     * vào các trường của lớp này.
     */
    @ConfigurationProperties(prefix = "nutrition.ai")
    public static class Jwt {
        // Tương ứng với thuộc tính `nutrition.ai.jwt-secret`
        private String jwtSecret;
        // Tương ứng với thuộc tính `nutrition.ai.jwt-expiration-ms`
        private long jwtExpirationMs;

        // Getters và Setters cho các thuộc tính (bắt buộc cho ConfigurationProperties)

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
     * @ConfigurationProperties(prefix = "ai.service"): Spring sẽ liên kết
     * các thuộc tính có prefix là `ai.service.` (ví dụ: `ai.service.base-url`)
     * vào các trường của lớp này.
     */
    @ConfigurationProperties(prefix = "ai.service")
    public static class AiService {
        // Tương ứng với thuộc tính `ai.service.base-url`
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}