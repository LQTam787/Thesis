package com.nutrition.ai.nutritionaibackend.service.ai;

import com.nutrition.ai.nutritionaibackend.config.ApplicationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * @Service
 * Lớp dịch vụ chịu trách nhiệm giao tiếp với các API Dịch vụ AI bên ngoài.
 * Sử dụng RestTemplate để thực hiện các cuộc gọi HTTP.
 */
@Service
public class AiService {

    /**
     * restTemplate được sử dụng để thực hiện các request HTTP.
     * Nó được inject (tiêm) vào qua Constructor (Hàm tạo).
     */
    private final RestTemplate restTemplate;

    /**
     * applicationProperties chứa cấu hình của ứng dụng, bao gồm Base URL
     * của Dịch vụ AI bên ngoài. Nó được inject qua Constructor.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết (Dependency Injection).
     *
     * @param restTemplate Đối tượng RestTemplate để thực hiện request HTTP.
     * @param applicationProperties Cấu hình ứng dụng để lấy Base URL của AI Service.
     */
    public AiService(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Phương thức gửi yêu cầu Phân tích Văn bản (NLP) cho thông tin dinh dưỡng
     * đến Dịch vụ AI bên ngoài.
     *
     * Luồng hoạt động:
     * 1. Xây dựng URL đầy đủ bằng cách lấy Base URL và nối thêm endpoint "/api/ai/nlp/analyze".
     * 2. Thiết lập Header: Đặt Content-Type là application/json.
     * 3. Xây dựng Request Body: Tạo một Map chứa khóa "text" và giá trị là văn bản đầu vào.
     * 4. Tạo HttpEntity: Gói Body và Header lại với nhau.
     * 5. Thực hiện request POST: Sử dụng restTemplate.exchange() để gửi request.
     * 6. Trả về kết quả: Lấy Body của phản hồi (Map<String, Object>).
     *
     * @param text Văn bản đầu vào cần phân tích (ví dụ: "Tôi đã ăn một quả táo").
     * @return Map chứa kết quả phân tích từ AI Service (ví dụ: các giá trị dinh dưỡng).
     */
    public Map<String, Object> analyzeTextForNutrition(String text) {
        // 1. Xây dựng URL
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/nlp/analyze";

        // 2. Thiết lập Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        // 4. Tạo HttpEntity
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // 5 & 6. Thực hiện POST request và trả về body của phản hồi
        return restTemplate.exchange(
                url, // URL đầy đủ
                HttpMethod.POST, // Phương thức HTTP
                request, // Entity (Body + Header)
                new ParameterizedTypeReference<Map<String, Object>>() {} // Kiểu dữ liệu mong muốn của phản hồi
        ).getBody();
    }

    /**
     * Phương thức gửi yêu cầu Phân tích Hình ảnh (Computer Vision) để nhận dạng thực phẩm
     * đến Dịch vụ AI bên ngoài.
     *
     * Luồng hoạt động tương tự analyzeTextForNutrition, nhưng với endpoint và body khác:
     * - Endpoint: "/api/ai/vision/analyze"
     * - Body: Chứa khóa "image_data" (thường là dữ liệu hình ảnh đã được mã hóa Base64).
     *
     * @param imageData Chuỗi dữ liệu hình ảnh (ví dụ: Base64) cần phân tích.
     * @return Map chứa kết quả nhận dạng và phân tích thực phẩm từ AI Service.
     */
    public Map<String, Object> analyzeImageForFood(String imageData) {
        // 1. Xây dựng URL cho phân tích hình ảnh
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/vision/analyze";

        // 2. Thiết lập Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body với dữ liệu hình ảnh
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image_data", imageData);

        // 4. Tạo HttpEntity
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // 5 & 6. Thực hiện POST request và trả về body của phản hồi
        return restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
        ).getBody();
    }

    /**
     * Phương thức gửi yêu cầu tạo Đề xuất Dinh dưỡng cá nhân hóa
     * đến Dịch vụ AI bên ngoài.
     *
     * Luồng hoạt động tương tự, nhưng Request Body chứa nhiều Map phức tạp hơn:
     * - Endpoint: "/api/ai/recommendation/generate"
     * - Body: Chứa "user_profile", "dietary_preferences", và "health_goals" (đều là Map).
     *
     * @param userProfile Hồ sơ người dùng (ví dụ: tuổi, giới tính, cân nặng).
     * @param dietaryPreferences Sở thích ăn kiêng (ví dụ: ăn chay, không gluten).
     * @param healthGoals Mục tiêu sức khỏe (ví dụ: giảm cân, tăng cơ).
     * @return Map chứa các đề xuất dinh dưỡng cá nhân hóa từ AI Service.
     */
    public Map<String, Object> getNutritionRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals) {
        // 1. Xây dựng URL cho đề xuất
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/recommendation/generate";

        // 2. Thiết lập Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body với các thông tin hồ sơ
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_profile", userProfile);
        requestBody.put("dietary_preferences", dietaryPreferences);
        requestBody.put("health_goals", healthGoals);

        // 4. Tạo HttpEntity
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // 5 & 6. Thực hiện POST request và trả về body của phản hồi
        return restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
        ).getBody();
    }
}