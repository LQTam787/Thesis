package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link NutritionPlan}.
 */
public interface NutritionPlanService {

    /**
     * Save a nutrition plan.
     *
     * @param nutritionPlan the entity to save.
     * @return the persisted entity.
     */
    NutritionPlan save(NutritionPlan nutritionPlan);

    /**
     * Get all the nutrition plans.
     *
     * @return the list of entities.
     */
    List<NutritionPlan> findAll();

    /**
     * Get one nutrition plan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NutritionPlan> findOne(Long id);

    /**
     * Delete the nutrition plan by id.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Find all nutrition plans for a specific user.
     *
     * @param user the user to find plans for.
     * @return a list of nutrition plans.
     */
    List<NutritionPlan> findAllByUser(User user);
}
