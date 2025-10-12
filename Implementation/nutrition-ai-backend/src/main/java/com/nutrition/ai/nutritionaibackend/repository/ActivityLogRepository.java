package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.ActivityLog;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the ActivityLog entity.
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * Finds a list of activity logs for a given user within a specific date range.
     *
     * @param user The user entity.
     * @param startDate The start date and time of the range.
     * @param endDate The end date and time of the range.
     * @return A list of ActivityLog entities.
     */
    List<ActivityLog> findByUserAndLogDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

}
