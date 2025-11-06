package com.nutrition.ai.nutritionaibackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lớp cấu hình cho tài liệu OpenAPI (Swagger UI).
 * Nó định nghĩa các thông tin cơ bản của API và cấu hình bảo mật (JWT Bearer Token)
 * để hiển thị trong giao diện người dùng Swagger.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Định nghĩa bean OpenAPI tùy chỉnh.
     * Bean này được Spring Boot tự động nhận diện và sử dụng để tạo tài liệu API.
     *
     * @return Đối tượng OpenAPI đã được cấu hình.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 1. Khai báo tên scheme bảo mật (thường là "bearerAuth")
        final String securitySchemeName = "bearerAuth";

        // 2. Bắt đầu cấu hình đối tượng OpenAPI
        return new OpenAPI()
                // 3. Thêm yêu cầu bảo mật: Yêu cầu tất cả các endpoint (trừ các endpoint được cấu hình bỏ qua)
                // phải có header Authorization với scheme "bearerAuth".
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 4. Định nghĩa các thành phần cấu hình (Components)
                .components(
                        new Components()
                                // 5. Định nghĩa scheme bảo mật "bearerAuth"
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                // Đặt tên scheme
                                                .name(securitySchemeName)
                                                // Loại bảo mật là HTTP (thường dùng cho token)
                                                .type(SecurityScheme.Type.HTTP)
                                                // Scheme là "bearer" (tiêu chuẩn cho JWT)
                                                .scheme("bearer")
                                                // Định dạng token là JWT
                                                .bearerFormat("JWT")
                                )
                )
                // 6. Định nghĩa thông tin cơ bản của API (Info)
                .info(new Info()
                        // Tiêu đề của API
                        .title("Nutrition AI Backend API")
                        // Phiên bản
                        .version("v1")
                        // Mô tả
                        .description("API documentation for the Nutrition AI application backend."));
        // Luồng hoạt động: Spring sử dụng bean này để tạo file OpenAPI JSON/YAML,
        // sau đó Swagger UI đọc file này để hiển thị giao diện tương tác.
    }
}