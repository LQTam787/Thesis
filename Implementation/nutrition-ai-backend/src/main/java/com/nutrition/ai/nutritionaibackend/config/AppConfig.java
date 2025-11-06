package com.nutrition.ai.nutritionaibackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration class.
 * Defines beans that are used across the application, such as ModelMapper và RestTemplate.
 */
@Configuration
public class AppConfig {

    /**
     * Tạo một ModelMapper bean cho việc ánh xạ đối tượng-sang-đối tượng (object-to-object mapping).
     * Nguyên lý hoạt động: ModelMapper đơn giản hóa việc chuyển dữ liệu giữa các lớp
     * (ví dụ: Entity sang DTO hoặc ngược lại) bằng cách tự động khớp các trường có tên tương tự.
     *
     * @return Một instance mới của ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Tạo một RestTemplate bean để thực hiện các yêu cầu HTTP đồng bộ.
     * Nguyên lý hoạt động: RestTemplate là một công cụ tiện lợi do Spring cung cấp
     * để giao tiếp với các dịch vụ RESTful bên ngoài (ví dụ: AI Service trong ApplicationProperties).
     *
     * @return Một instance mới của RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}