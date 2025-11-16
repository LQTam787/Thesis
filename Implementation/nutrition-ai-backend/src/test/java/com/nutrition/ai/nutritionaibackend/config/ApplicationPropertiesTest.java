package com.nutrition.ai.nutritionaibackend.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationProperties Unit Tests")
class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        applicationProperties = new ApplicationProperties();
    }

    @Test
    @DisplayName("Test Jwt properties getters and setters")
    void testJwtProperties() {
        ApplicationProperties.Jwt jwt = applicationProperties.getJwt();
        assertNotNull(jwt, "Jwt object should not be null");

        String expectedSecret = "test-jwt-secret";
        long expectedExpiration = 3600000L;

        jwt.setJwtSecret(expectedSecret);
        jwt.setJwtExpirationMs(expectedExpiration);

        assertEquals(expectedSecret, jwt.getJwtSecret(), "Jwt secret should match the set value");
        assertEquals(expectedExpiration, jwt.getJwtExpirationMs(), "Jwt expiration should match the set value");
    }

    @Test
    @DisplayName("Test AiService properties getters and setters")
    void testAiServiceProperties() {
        ApplicationProperties.AiService aiService = applicationProperties.getAiService();
        assertNotNull(aiService, "AiService object should not be null");

        String expectedBaseUrl = "http://localhost:8081/ai";

        aiService.setBaseUrl(expectedBaseUrl);

        assertEquals(expectedBaseUrl, aiService.getBaseUrl(), "AI Service base URL should match the set value");
    }

    @Test
    @DisplayName("Test ApplicationProperties top-level getters")
    void testApplicationPropertiesGetters() {
        assertNotNull(applicationProperties.getJwt(), "getJwt() should return a non-null object");
        assertNotNull(applicationProperties.getAiService(), "getAiService() should return a non-null object");
        // Verify that different calls return the same instance, indicating they are initialized once
        assertSame(applicationProperties.getJwt(), applicationProperties.getJwt(), "getJwt() should return the same instance");
        assertSame(applicationProperties.getAiService(), applicationProperties.getAiService(), "getAiService() should return the same instance");
    }
}
