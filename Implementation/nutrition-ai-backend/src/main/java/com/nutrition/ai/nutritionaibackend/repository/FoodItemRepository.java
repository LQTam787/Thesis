package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FoodItem entity.
 */
@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
