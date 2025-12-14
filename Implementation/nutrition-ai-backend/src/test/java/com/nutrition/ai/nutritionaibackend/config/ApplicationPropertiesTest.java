//package com.nutrition.ai.nutritionaibackend.config;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Lớp kiểm thử đơn vị cho {@link ApplicationProperties}.
// * Đảm bảo rằng các thuộc tính cấu hình ứng dụng, đặc biệt là các thuộc tính liên quan đến JWT
// * và dịch vụ AI, được khởi tạo đúng cách và có thể được truy cập thông qua các phương thức
// * getter và setter.
// *
// * <p>Các bài kiểm thử này xác minh tính toàn vẹn và hành vi mong đợi của các
// * đối tượng thuộc tính cấu hình, đảm bảo rằng ứng dụng có thể đọc và sử dụng
// * các giá trị cấu hình một cách chính xác. Điều này bao gồm kiểm tra khả năng
// * thiết lập và truy xuất các giá trị, cũng như đảm bảo rằng các đối tượng con
// * (như Jwt và AiService) được khởi tạo một cách lười biếng (lazy initialization)
// * hoặc được tạo ra khi cần thiết và giữ nguyên instance.</p>
// *
// * @see ApplicationProperties
// */
//@DisplayName("ApplicationProperties Unit Tests")
//class ApplicationPropertiesTest {
//
//    /**
//     * Đối tượng {@link ApplicationProperties} được kiểm thử.
//     * Được khởi tạo lại trước mỗi bài kiểm thử để đảm bảo môi trường sạch.
//     */
//    private ApplicationProperties applicationProperties;
//
//    /**
//     * Thiết lập môi trường kiểm thử trước mỗi bài kiểm thử.
//     * Khởi tạo một instance mới của {@link ApplicationProperties}.
//     */
//    @BeforeEach
//    void setUp() {
//        // Khởi tạo một đối tượng ApplicationProperties mới trước mỗi bài kiểm thử
//        applicationProperties = new ApplicationProperties();
//    }
//
//    /**
//     * Kiểm tra các phương thức getter và setter cho các thuộc tính JWT.
//     *
//     * <p>Xác minh rằng các thuộc tính như secret và thời gian hết hạn của JWT
//     * có thể được thiết lập và truy xuất một cách chính xác thông qua các phương thức
//     * setter và getter tương ứng của lớp con {@code Jwt}.</p>
//     */
//    @Test
//    @DisplayName("Test Jwt properties getters and setters")
//    void testJwtProperties() {
//        // Lấy đối tượng Jwt từ ApplicationProperties
//        ApplicationProperties.Jwt jwt = applicationProperties.getJwt();
//        // Đảm bảo đối tượng Jwt không null
//        assertNotNull(jwt, "Jwt object should not be null");
//
//        // Định nghĩa các giá trị mong đợi
//        String expectedSecret = "test-jwt-secret";
//        long expectedExpiration = 3600000L;
//
//        // Thiết lập các giá trị JWT
//        jwt.setJwtSecret(expectedSecret);
//        jwt.setJwtExpirationMs(expectedExpiration);
//
//        // Xác nhận rằng các giá trị đã được thiết lập chính xác
//        assertEquals(expectedSecret, jwt.getJwtSecret(), "Jwt secret should match the set value");
//        assertEquals(expectedExpiration, jwt.getJwtExpirationMs(), "Jwt expiration should match the set value");
//    }
//
//    /**
//     * Kiểm tra các phương thức getter và setter cho các thuộc tính của AiService.
//     *
//     * <p>Xác minh rằng thuộc tính {@code baseUrl} của dịch vụ AI có thể được
//     * thiết lập và truy xuất một cách chính xác thông qua các phương thức
//     * setter và getter tương ứng của lớp con {@code AiService}.</p>
//     */
//    @Test
//    @DisplayName("Test AiService properties getters and setters")
//    void testAiServiceProperties() {
//        // Lấy đối tượng AiService từ ApplicationProperties
//        ApplicationProperties.AiService aiService = applicationProperties.getAiService();
//        // Đảm bảo đối tượng AiService không null
//        assertNotNull(aiService, "AiService object should not be null");
//
//        // Định nghĩa giá trị URL cơ sở mong đợi
//        String expectedBaseUrl = "http://localhost:8081/ai";
//
//        // Thiết lập giá trị URL cơ sở
//        aiService.setBaseUrl(expectedBaseUrl);
//
//        // Xác nhận rằng giá trị đã được thiết lập chính xác
//        assertEquals(expectedBaseUrl, aiService.getBaseUrl(), "AI Service base URL should match the set value");
//    }
//
//    /**
//     * Kiểm tra các phương thức getter cấp cao nhất của {@link ApplicationProperties}.
//     *
//     * <p>Xác minh rằng các phương thức {@code getJwt()} và {@code getAiService()}
//     * luôn trả về các đối tượng không null và cùng một instance trong suốt vòng đời
//     * của {@code applicationProperties}. Điều này kiểm tra cơ chế khởi tạo
//     * và quản lý instance của các đối tượng thuộc tính con.</p>
//     */
//    @Test
//    @DisplayName("Test ApplicationProperties top-level getters")
//    void testApplicationPropertiesGetters() {
//        // Đảm bảo getJwt() trả về đối tượng không null
//        assertNotNull(applicationProperties.getJwt(), "getJwt() should return a non-null object");
//        // Đảm bảo getAiService() trả về đối tượng không null
//        assertNotNull(applicationProperties.getAiService(), "getAiService() should return a non-null object");
//        // Xác minh rằng các lần gọi khác nhau đến getJwt() trả về cùng một instance
//        assertSame(applicationProperties.getJwt(), applicationProperties.getJwt(), "getJwt() should return the same instance");
//        // Xác minh rằng các lần gọi khác nhau đến getAiService() trả về cùng một instance
//        assertSame(applicationProperties.getAiService(), applicationProperties.getAiService(), "getAiService() should return the same instance");
//    }
//}
