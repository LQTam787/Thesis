package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.ContentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link ContentComment} entities.
 * Provides methods for data access operations related to content comments.
 */
@Repository
public interface ContentCommentRepository extends JpaRepository<ContentComment, Long> {
    long countBySharedContentId(Long sharedContentId);
}
