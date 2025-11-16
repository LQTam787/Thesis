package com.nutrition.ai.nutritionaibackend.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Lớp kiểm thử đơn vị cho {@link OpenApiConfig}.
 * Đảm bảo rằng cấu hình OpenAPI (Swagger) của ứng dụng được thiết lập chính xác,
 * bao gồm thông tin API cơ bản, yêu cầu bảo mật và định nghĩa {@code SecurityScheme} cho JWT.
 *
 * <p>Mục tiêu của lớp kiểm thử này là xác minh rằng tài liệu API được tạo ra
 * phản ánh đúng cấu hình mong muốn, giúp các nhà phát triển và người dùng API
 * hiểu rõ về các điểm cuối, mô hình dữ liệu và cơ chế xác thực của ứng dụng.</p>
 *
 * @see OpenApiConfig
 */
class OpenApiConfigTest {

    /**
     * Kiểm tra xem phương thức {@code customOpenAPI} có trả về một đối tượng {@code OpenAPI}
     * được cấu hình đúng cách hay không.
     *
     * <p>Bài kiểm thử này xác minh ba khía cạnh chính của cấu hình OpenAPI:
     * <ol>
     *     <li>Thông tin chung về API (tiêu đề, phiên bản, mô tả).</li>
     *     <li>Sự hiện diện và nội dung của yêu cầu bảo mật ({@code bearerAuth}).</li>
     *     <li>Định nghĩa chi tiết của lược đồ bảo mật {@code bearerAuth} (loại, lược đồ, định dạng).</li>
     * </ol>
     * Điều này đảm bảo rằng tài liệu Swagger/OpenAPI của ứng dụng được tạo ra
     * chính xác cho mục đích hiển thị và tương tác.</p>
     */
    @Test
    void customOpenAPI_shouldReturnConfiguredOpenAPIObject() {
        // Khởi tạo đối tượng OpenApiConfig để gọi phương thức cấu hình
        OpenApiConfig openApiConfig = new OpenApiConfig();
        // Lấy đối tượng OpenAPI đã được cấu hình
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // 1. Xác minh thông tin API (Info Object)
        assertNotNull(openAPI.getInfo(), "Info should not be null");
        assertEquals("Nutrition AI Backend API", openAPI.getInfo().getTitle(), "API title should match");
        assertEquals("v1", openAPI.getInfo().getVersion(), "API version should match");
        assertEquals("API documentation for the Nutrition AI application backend.", openAPI.getInfo().getDescription(), "API description should match");

        // 2. Xác minh yêu cầu bảo mật (Security Requirements)
        assertNotNull(openAPI.getSecurity(), "Security requirements should not be null");
        assertFalse(openAPI.getSecurity().isEmpty(), "Security requirements should not be empty");
        // Lấy SecurityRequirement đầu tiên và kiểm tra sự hiện diện của "bearerAuth"
        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        assertTrue(securityRequirement.containsKey("bearerAuth"), "Security requirement should contain bearerAuth");

        // 3. Xác minh định nghĩa Security Scheme (Components -> SecuritySchemes)
        assertNotNull(openAPI.getComponents(), "Components should not be null");
        assertNotNull(openAPI.getComponents().getSecuritySchemes(), "Security schemes should not be null");
        // Lấy SecurityScheme "bearerAuth" và kiểm tra các thuộc tính của nó
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(securityScheme, "bearerAuth security scheme should exist");
        assertEquals("bearerAuth", securityScheme.getName(), "Security scheme name should be bearerAuth");
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType(), "Security scheme type should be HTTP");
        assertEquals("bearer", securityScheme.getScheme(), "Security scheme should be bearer");
        assertEquals("JWT", securityScheme.getBearerFormat(), "Bearer format should be JWT");
    }
}
