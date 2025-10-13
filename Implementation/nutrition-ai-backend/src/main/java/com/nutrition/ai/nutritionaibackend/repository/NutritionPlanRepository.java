package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the NutritionPlan entity.
 */
@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findByUser(User user);
}
