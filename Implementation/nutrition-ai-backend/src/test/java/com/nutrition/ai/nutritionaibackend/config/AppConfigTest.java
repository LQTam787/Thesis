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
 */
class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    /**
     * Kiểm tra xem bean ModelMapper có được tạo và không null.
     */
    @Test
    @DisplayName("Kiểm tra khởi tạo ModelMapper bean")
    void modelMapperBeanShouldBeCreated() {
        ModelMapper modelMapper = appConfig.modelMapper();
        assertNotNull(modelMapper, "ModelMapper bean không được null");
        assertTrue(modelMapper instanceof ModelMapper, "Bean phải là một instance của ModelMapper");
    }

    /**
     * Kiểm tra xem bean RestTemplate có được tạo và không null.
     */
    @Test
    @DisplayName("Kiểm tra khởi tạo RestTemplate bean")
    void restTemplateBeanShouldBeCreated() {
        RestTemplate restTemplate = appConfig.restTemplate();
        assertNotNull(restTemplate, "RestTemplate bean không được null");
        assertTrue(restTemplate instanceof RestTemplate, "Bean phải là một instance của RestTemplate");
    }
}
