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
 * Lớp dịch vụ chịu trách nhiệm giao tiếp với các API Dịch vụ AI bên ngoài.
 * Sử dụng {@link RestTemplate} để thực hiện các cuộc gọi HTTP đến các endpoint của AI Service.
 * Các endpoint này bao gồm phân tích văn bản (NLP), phân tích hình ảnh (Computer Vision),
 * và tạo đề xuất dinh dưỡng cá nhân hóa.
 */
@Service
public class AiService {

    /**
     * {@code restTemplate} được sử dụng để thực hiện các request HTTP.
     * Nó được inject (tiêm) vào qua Constructor (Hàm tạo) bởi Spring Framework.
     * Các request được thực hiện đến các dịch vụ AI bên ngoài.
     */
    private final RestTemplate restTemplate;

    /**
     * {@code applicationProperties} chứa cấu hình của ứng dụng, bao gồm Base URL
     * của Dịch vụ AI bên ngoài. Nó được inject qua Constructor bởi Spring Framework.
     * Đối tượng này giúp cấu hình động các endpoint API.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Hàm tạo (Constructor) để Spring tiêm các dependency cần thiết (Dependency Injection).
     * Đảm bảo rằng {@link RestTemplate} và {@link ApplicationProperties} được cấu hình
     * và sẵn sàng để sử dụng khi {@link AiService} được khởi tạo.
     *
     * @param restTemplate Đối tượng RestTemplate để thực hiện request HTTP.
     * @param applicationProperties Cấu hình ứng dụng để lấy Base URL của AI Service.
     */
    public AiService(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Phương thức gửi yêu cầu Phân tích Văn bản (Natural Language Processing - NLP)
     * cho thông tin dinh dưỡng đến Dịch vụ AI bên ngoài.
     *
     * <p>Luồng hoạt động chi tiết:</p>
     * <ol>
     *     <li><b>Xây dựng URL:</b> Kết hợp Base URL của AI Service (từ {@code applicationProperties})
     *         với endpoint cụ thể "/api/ai/nlp/analyze" để tạo URL đầy đủ cho request.</li>
     *     <li><b>Thiết lập Header:</b> Tạo {@link HttpHeaders} và đặt {@code Content-Type} là {@code application/json},
     *         cho biết định dạng của dữ liệu gửi đi.</li>
     *     <li><b>Xây dựng Request Body:</b> Tạo một {@link Map} chứa dữ liệu đầu vào. Đối với phân tích văn bản,
     *         body sẽ chứa một khóa "text" với giá trị là văn bản cần phân tích.</li>
     *     <li><b>Tạo HttpEntity:</b> Gói Request Body và HttpHeaders vào một đối tượng {@link HttpEntity}.
     *         Đối tượng này đại diện cho toàn bộ request HTTP (bao gồm header và body).</li>
     *     <li><b>Thực hiện Request POST:</b> Sử dụng {@code restTemplate.exchange()} để gửi request HTTP POST.
     *         Các tham số bao gồm URL, phương thức HTTP (POST), HttpEntity và kiểu dữ liệu mong muốn của phản hồi
     *         (sử dụng {@link ParameterizedTypeReference} để xử lý kiểu generics).</li>
     *     <li><b>Trả về kết quả:</b> Trích xuất và trả về body của phản hồi từ AI Service,
     *         thường là một {@link Map} chứa các kết quả phân tích dinh dưỡng.</li>
     * </ol>
     *
     * @param text Văn bản đầu vào cần phân tích (ví dụ: "Tôi đã ăn một quả táo").
     * @return Map chứa kết quả phân tích từ AI Service (ví dụ: các giá trị dinh dưỡng).
     */
    public Map<String, Object> analyzeTextForNutrition(String text) {
        // 1. Xây dựng URL đầy đủ cho endpoint NLP
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/nlp/analyze";

        // 2. Thiết lập Header cho request (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body với văn bản đầu vào
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        // 4. Tạo HttpEntity từ body và header
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // 5 & 6. Thực hiện POST request và trả về body của phản hồi
        return restTemplate.exchange(
                url, // URL đầy đủ của dịch vụ AI
                HttpMethod.POST, // Phương thức HTTP là POST
                request, // HttpEntity chứa request body và headers
                new ParameterizedTypeReference<Map<String, Object>>() {} // Định nghĩa kiểu phản hồi mong muốn
        ).getBody();
    }

    /**
     * Phương thức gửi yêu cầu Phân tích Hình ảnh (Computer Vision) để nhận dạng thực phẩm
     * và trích xuất thông tin dinh dưỡng từ hình ảnh đến Dịch vụ AI bên ngoài.
     *
     * <p>Luồng hoạt động tương tự như {@link #analyzeTextForNutrition(String)}, nhưng với các điểm khác biệt:</p>
     * <ol>
     *     <li><b>Endpoint:</b> Sử dụng endpoint "/api/ai/vision/analyze".</li>
     *     <li><b>Request Body:</b> Chứa một khóa "image_data" với giá trị là dữ liệu hình ảnh (thường là chuỗi mã hóa Base64).</li>
     * </ol>
     * Phần còn lại của luồng (thiết lập header, tạo HttpEntity, thực hiện request và xử lý phản hồi) là tương tự.
     *
     * @param imageData Chuỗi dữ liệu hình ảnh (ví dụ: Base64) cần phân tích.
     * @return Map chứa kết quả nhận dạng và phân tích thực phẩm từ AI Service.
     */
    public Map<String, Object> analyzeImageForFood(String imageData) {
        // 1. Xây dựng URL đầy đủ cho endpoint Vision AI
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/vision/analyze";

        // 2. Thiết lập Header cho request (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body với dữ liệu hình ảnh đã mã hóa Base64
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image_data", imageData);

        // 4. Tạo HttpEntity từ body và header
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
     * dựa trên hồ sơ người dùng, sở thích ăn kiêng và mục tiêu sức khỏe
     * đến Dịch vụ AI bên ngoài.
     *
     * <p>Luồng hoạt động tương tự, nhưng Request Body chứa nhiều Map phức tạp hơn:</p>
     * <ol>
     *     <li><b>Endpoint:</b> Sử dụng endpoint "/api/ai/recommendation/generate".</li>
     *     <li><b>Request Body:</b> Chứa các khóa "user_profile", "dietary_preferences", và "health_goals",
     *         mỗi khóa là một {@link Map} chứa các thông tin liên quan.</li>
     * </ol>
     * Phần còn lại của luồng (thiết lập header, tạo HttpEntity, thực hiện request và xử lý phản hồi) là tương tự.
     *
     * @param userProfile Hồ sơ người dùng (ví dụ: tuổi, giới tính, cân nặng).
     * @param dietaryPreferences Sở thích ăn kiêng (ví dụ: ăn chay, không gluten).
     * @param healthGoals Mục tiêu sức khỏe (ví dụ: giảm cân, tăng cơ).
     * @return Map chứa các đề xuất dinh dưỡng cá nhân hóa từ AI Service.
     */
    public Map<String, Object> getNutritionRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals) {
        // 1. Xây dựng URL đầy đủ cho endpoint Recommendation AI
        String url = applicationProperties.getAiService().getBaseUrl() + "/api/ai/recommendation/generate";

        // 2. Thiết lập Header cho request (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Xây dựng Request Body với các thông tin hồ sơ người dùng
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_profile", userProfile);
        requestBody.put("dietary_preferences", dietaryPreferences);
        requestBody.put("health_goals", healthGoals);

        // 4. Tạo HttpEntity từ body và header
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