package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DailyMenu entity.
 */
@Repository
public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {
}
