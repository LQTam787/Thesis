package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.MealFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MealFoodItem entity.
 */
@Repository
public interface MealFoodItemRepository extends JpaRepository<MealFoodItem, Long> {
}
