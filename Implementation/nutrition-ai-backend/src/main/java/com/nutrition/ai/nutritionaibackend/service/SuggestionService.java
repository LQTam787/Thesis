package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion}.
 * Định nghĩa các thao tác cho Gợi ý Dinh dưỡng, bao gồm cả việc tương tác với mô hình AI.
 */
public interface SuggestionService {

    /**
     * Luồng hoạt động: Lưu một Gợi ý Dinh dưỡng.
     * Nguyên lý hoạt động: Nhận DTO, chuyển đổi sang Entity, lưu vào DB (thao tác này thường được thực hiện bởi người dùng hoặc hệ thống sau khi nhận gợi ý từ AI).
     *
     * @param suggestionDto The DTO to save.
     * @return The persisted SuggestionDto.
     */
    SuggestionDto save(SuggestionDto suggestionDto);

    /**
     * Luồng hoạt động: Truy xuất tất cả Gợi ý Dinh dưỡng của một người dùng.
     * Nguyên lý hoạt động: Truy vấn DB để tìm các gợi ý liên quan đến User.
     *
     * @param user The user whose suggestions are to be retrieved.
     * @return A list of SuggestionDto.
     */
    List<SuggestionDto> findAllByUser(User user);

    /**
     * Luồng hoạt động: Lấy các đề xuất dinh dưỡng được tạo bởi AI.
     * Nguyên lý hoạt động: Gửi dữ liệu đầu vào của người dùng (hồ sơ, sở thích ăn uống, mục tiêu sức khỏe) đến một Service AI chuyên biệt (`AiService`) để xử lý và nhận lại kết quả.
     *
     * @param userProfile User's profile data (e.g., age, weight, height).
     * @param dietaryPreferences User's dietary constraints (e.g., vegan, allergies).
     * @param healthGoals User's health goals (e.g., weight loss, muscle gain).
     * @return A Map containing the AI's nutrition recommendations.
     */
    Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals);
}