package com.nutrition.ai.nutritionaibackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration class.
 * Defines beans that are used across the application, such as ModelMapper.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a ModelMapper bean for object-to-object mapping.
     *
     * @return A new instance of ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Creates a RestTemplate bean for making synchronous HTTP requests.
     *
     * @return A new instance of RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
