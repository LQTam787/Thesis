package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FoodItem}.
 */
public interface FoodService {

    /**
     * Save a food item.
     *
     * @param foodItem the entity to save.
     * @return the persisted entity.
     */
    FoodItem save(FoodItem foodItem);

    /**
     * Get all the food items.
     *
     * @return the list of entities.
     */
    List<FoodItem> findAll();

    /**
     * Get one food item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodItem> findOne(Long id);

    /**
     * Delete the food item by id.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for food items by name.
     *
     * @param name the name to search for.
     * @return a list of matching food items.
     */
    List<FoodItem> findByNameContaining(String name);
}
