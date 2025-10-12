package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.ContentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link ContentLike} entities.
 * Provides methods for data access operations related to content likes.
 */
@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {
    long countBySharedContentId(Long sharedContentId);
}
