package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.Suggestion;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;

/**
 * Service Interface for managing {@link Suggestion}.
 */
public interface SuggestionService {

    /**
     * Save a suggestion.
     *
     * @param suggestion the entity to save.
     * @return the persisted entity.
     */
    Suggestion save(Suggestion suggestion);

    /**
     * Find all suggestions for a specific user.
     *
     * @param user the user to find suggestions for.
     * @return a list of suggestions.
     */
    List<Suggestion> findAllByUser(User user);
}
