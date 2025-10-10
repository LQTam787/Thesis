package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NutritionPlan entity.
 */
@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
}
