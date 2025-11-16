package com.nutrition.ai.nutritionaibackend.service.ai;

import com.nutrition.ai.nutritionaibackend.config.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Lớp kiểm thử đơn vị cho {@link AiService}.
 * Đảm bảo rằng tất cả các phương thức trong AiService hoạt động chính xác
 * và giao tiếp đúng cách với các dịch vụ AI bên ngoài thông qua RestTemplate.
 */
class AiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private ApplicationProperties.AiService aiServiceProperties;

    @InjectMocks
    private AiService aiService;

    /**
     * Thiết lập môi trường kiểm thử trước mỗi lần chạy test.
     * Khởi tạo các mock và thiết lập hành vi mặc định cho ApplicationProperties.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationProperties.getAiService()).thenReturn(aiServiceProperties);
        when(aiServiceProperties.getBaseUrl()).thenReturn("http://test-ai-service.com");
    }

    /**
     * Kiểm thử phương thức {@link AiService#analyzeTextForNutrition(String)}.
     * Xác minh rằng phương thức gửi yêu cầu POST chính xác đến dịch vụ AI
     * và trả về kết quả phân tích văn bản.
     */
    @Test
    @SuppressWarnings("unchecked")
    void analyzeTextForNutrition_shouldReturnNutritionAnalysis() {
        // Chuẩn bị dữ liệu
        String text = "Tôi đã ăn một quả táo";
        String expectedUrl = "http://test-ai-service.com/api/ai/nlp/analyze";
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("calories", 95);
        expectedResponse.put("food_items", "apple");

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // Định nghĩa hành vi của mock RestTemplate
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        )).thenReturn(responseEntity);

        // Thực thi phương thức cần kiểm thử
        Map<String, Object> result = aiService.analyzeTextForNutrition(text);

        // Xác minh
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // Xác minh rằng restTemplate.exchange đã được gọi với các đối số chính xác
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        );
    }

    /**
     * Kiểm thử phương thức {@link AiService#analyzeImageForFood(String)}.
     * Xác minh rằng phương thức gửi yêu cầu POST chính xác đến dịch vụ AI
     * và trả về kết quả phân tích hình ảnh thực phẩm.
     */
    @Test
    @SuppressWarnings("unchecked")
    void analyzeImageForFood_shouldReturnFoodAnalysis() {
        // Chuẩn bị dữ liệu
        String imageData = "base64encodedImageData";
        String expectedUrl = "http://test-ai-service.com/api/ai/vision/analyze";
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("food_recognized", "pizza");
        expectedResponse.put("confidence", 0.98);

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // Định nghĩa hành vi của mock RestTemplate
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        )).thenReturn(responseEntity);

        // Thực thi phương thức cần kiểm thử
        Map<String, Object> result = aiService.analyzeImageForFood(imageData);

        // Xác minh
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // Xác minh rằng restTemplate.exchange đã được gọi với các đối số chính xác
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        );
    }

    /**
     * Kiểm thử phương thức {@link AiService#getNutritionRecommendations(Map, Map, Map)}.
     * Xác minh rằng phương thức gửi yêu cầu POST chính xác đến dịch vụ AI
     * và trả về các đề xuất dinh dưỡng cá nhân hóa.
     */
    @Test
    @SuppressWarnings("unchecked")
    void getNutritionRecommendations_shouldReturnRecommendations() {
        // Chuẩn bị dữ liệu
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("age", 30);
        userProfile.put("gender", "female");

        Map<String, Object> dietaryPreferences = new HashMap<>();
        dietaryPreferences.put("vegetarian", true);

        Map<String, Object> healthGoals = new HashMap<>();
        healthGoals.put("weight_loss", true);

        String expectedUrl = "http://test-ai-service.com/api/ai/recommendation/generate";
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("recommendations", "Ăn nhiều rau xanh và protein.");

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // Định nghĩa hành vi của mock RestTemplate
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        )).thenReturn(responseEntity);

        // Thực thi phương thức cần kiểm thử
        Map<String, Object> result = aiService.getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals);

        // Xác minh
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // Xác minh rằng restTemplate.exchange đã được gọi với các đối số chính xác
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Map<String, Object>>) any()
        );
    }
}
