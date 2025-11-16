package com.nutrition.ai.nutritionaibackend.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

class OpenApiConfigTest {

    @Test
    void customOpenAPI_shouldReturnConfiguredOpenAPIObject() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // 1. Xác minh thông tin API
        assertNotNull(openAPI.getInfo(), "Info should not be null");
        assertEquals("Nutrition AI Backend API", openAPI.getInfo().getTitle(), "API title should match");
        assertEquals("v1", openAPI.getInfo().getVersion(), "API version should match");
        assertEquals("API documentation for the Nutrition AI application backend.", openAPI.getInfo().getDescription(), "API description should match");

        // 2. Xác minh yêu cầu bảo mật
        assertNotNull(openAPI.getSecurity(), "Security requirements should not be null");
        assertFalse(openAPI.getSecurity().isEmpty(), "Security requirements should not be empty");
        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"), "Security requirement should contain bearerAuth");

        // 3. Xác minh định nghĩa Security Scheme
        assertNotNull(openAPI.getComponents(), "Components should not be null");
        assertNotNull(openAPI.getComponents().getSecuritySchemes(), "Security schemes should not be null");
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(securityScheme, "bearerAuth security scheme should exist");
        assertEquals("bearerAuth", securityScheme.getName(), "Security scheme name should be bearerAuth");
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType(), "Security scheme type should be HTTP");
        assertEquals("bearer", securityScheme.getScheme(), "Security scheme should be bearer");
        assertEquals("JWT", securityScheme.getBearerFormat(), "Bearer format should be JWT");
    }
}
