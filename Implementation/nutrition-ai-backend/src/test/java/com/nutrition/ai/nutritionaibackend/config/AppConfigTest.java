package com.nutrition.ai.nutritionaibackend.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lớp kiểm thử đơn vị (unit test) cho {@link AppConfig}.
 * Đảm bảo rằng các bean được định nghĩa trong AppConfig được khởi tạo chính xác.
 *
 * <p>Mục tiêu chính của lớp kiểm thử này là xác minh rằng các phương thức
 * cấu hình (như {@code modelMapper()} và {@code restTemplate()}) trong {@code AppConfig}
 * trả về các instance không null và đúng kiểu mong đợi. Điều này đảm bảo rằng
 * cấu hình ứng dụng cơ bản hoạt động như mong đợi và các dependency
 * cần thiết có sẵn cho các thành phần khác của ứng dụng.</p>
 *
 * @see AppConfig
 */
class AppConfigTest {

    /**
     * Instance của {@link AppConfig} được sử dụng để gọi các phương thức tạo bean.
     * Được khởi tạo một lần cho tất cả các bài kiểm thử trong lớp này.
     */
    private final AppConfig appConfig = new AppConfig();

    /**
     * Kiểm tra xem bean ModelMapper có được tạo và không null.
     *
     * <p>Xác minh rằng phương thức {@code appConfig.modelMapper()} trả về một
     * đối tượng {@code ModelMapper} hợp lệ. Điều này quan trọng để đảm bảo
     * cơ chế ánh xạ đối tượng giữa các lớp DTO và Entity hoạt động chính xác.</p>
     */
    @Test
    @DisplayName("Kiểm tra khởi tạo ModelMapper bean")
    void modelMapperBeanShouldBeCreated() {
        // Gọi phương thức tạo ModelMapper bean từ AppConfig
        ModelMapper modelMapper = appConfig.modelMapper();
        // Đảm bảo ModelMapper bean không phải là null
        assertNotNull(modelMapper, "ModelMapper bean không được null");
        // Đảm bảo đối tượng trả về là một instance của ModelMapper
        assertTrue(modelMapper instanceof ModelMapper, "Bean phải là một instance của ModelMapper");
    }

    /**
     * Kiểm tra xem bean RestTemplate có được tạo và không null.
     *
     * <p>Xác minh rằng phương thức {@code appConfig.restTemplate()} trả về một
     * đối tượng {@code RestTemplate} hợp lệ. Điều này cần thiết cho các
     * thành phần ứng dụng để thực hiện các cuộc gọi HTTP đến các dịch vụ bên ngoài.</p>
     */
    @Test
    @DisplayName("Kiểm tra khởi tạo RestTemplate bean")
    void restTemplateBeanShouldBeCreated() {
        // Gọi phương thức tạo RestTemplate bean từ AppConfig
        RestTemplate restTemplate = appConfig.restTemplate();
        // Đảm bảo RestTemplate bean không phải là null
        assertNotNull(restTemplate, "RestTemplate bean không được null");
        // Đảm bảo đối tượng trả về là một instance của RestTemplate
        assertTrue(restTemplate instanceof RestTemplate, "Bean phải là một instance của RestTemplate");
    }
}
