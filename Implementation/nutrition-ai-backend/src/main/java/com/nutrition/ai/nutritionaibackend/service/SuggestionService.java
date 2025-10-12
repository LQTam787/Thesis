package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing {@link com.nutrition.ai.nutritionaibackend.model.domain.Suggestion}.
 */
public interface SuggestionService {

    SuggestionDto save(SuggestionDto suggestionDto);

    List<SuggestionDto> findAllByUser(User user);

    Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals);
}
