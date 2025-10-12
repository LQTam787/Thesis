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
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final AiService aiService;
    private final ModelMapper modelMapper;

    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, AiService aiService, ModelMapper modelMapper) {
        this.suggestionRepository = suggestionRepository;
        this.aiService = aiService;
        this.modelMapper = modelMapper;
    }

    @Override
    public SuggestionDto save(SuggestionDto suggestionDto) {
        Suggestion suggestion = modelMapper.map(suggestionDto, Suggestion.class);
        return modelMapper.map(suggestionRepository.save(suggestion), SuggestionDto.class);
    }

    @Override
    public List<SuggestionDto> findAllByUser(User user) {
        return suggestionRepository.findAll().stream()
            .filter(suggestion -> suggestion.getUser().equals(user))
            .map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAiRecommendations(Map<String, Object> userProfile, Map<String, Object> dietaryPreferences, Map<String, Object> healthGoals) {
        return aiService.getNutritionRecommendations(userProfile, dietaryPreferences, healthGoals);
    }
}
