package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.Suggestion;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.SuggestionRepository;
import com.nutrition.ai.nutritionaibackend.service.ai.AiService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Suggestion}.
 * Thực hiện logic cho Gợi ý Dinh dưỡng, sử dụng ModelMapper để chuyển đổi DTO/Entity và AiService để lấy đề xuất thông minh.
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final AiService aiService;
    private final ModelMapper modelMapper;

    // Nguyên lý hoạt động: Dependency Injection
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, AiService aiService, ModelMapper modelMapper) {
        this.suggestionRepository = suggestionRepository;
        this.aiService = aiService;
        this.modelMapper = modelMapper;
    }

    @Override
    public SuggestionDto save(SuggestionDto suggestionDto) {
        // Luồng hoạt động: Lưu Suggestion DTO.
        // Nguyên lý hoạt động: Sử dụng ModelMapper để chuyển đổi SuggestionDto (input) thành Suggestion Entity, lưu vào DB, sau đó chuyển đổi Entity đã lưu (output) trở lại SuggestionDto để trả về.
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        return modelMapper.map(suggestionRepository.save(suggestion), SuggestionDto.class);
    }

    @Override
    public List<SuggestionDto> findAllByUser(User user) {
        // Luồng hoạt động: Tìm tất cả Gợi ý Dinh dưỡng của một User.
        // Nguyên lý hoạt động (Lọc trong bộ nhớ - cần tối ưu hóa bằng cách dùng `findByUser` ở Repository):
        // 1. Lấy tất cả Suggestion.
        // 2. Lọc danh sách trong bộ nhớ (stream) để tìm các gợi ý có User khớp với User được cung cấp.
        // 3. Sử dụng ModelMapper để chuyển đổi từ Entity sang DTO.
        return suggestionRepository.findAll().stream()
            .filter(suggestion -> suggestion.getUser().equals(user))
            .map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals) {
        // Luồng hoạt động: Lấy đề xuất từ AI.
        // Nguyên lý hoạt động (Tách biệt trách nhiệm): Uỷ quyền hoàn toàn việc giao tiếp và xử lý AI cho một Service chuyên biệt khác là `AiService`, giúp giữ cho SuggestionService tập trung vào nghiệp vụ quản lý Suggestion.
        return aiService.getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals);
    }
}