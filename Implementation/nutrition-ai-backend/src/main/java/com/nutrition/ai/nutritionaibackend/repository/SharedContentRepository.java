package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.SharedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link SharedContent} entities.
 * Provides standard CRUD operations and custom query methods for accessing shared content data.
 */
@Repository
public interface SharedContentRepository extends JpaRepository<SharedContent, Long> {
}
