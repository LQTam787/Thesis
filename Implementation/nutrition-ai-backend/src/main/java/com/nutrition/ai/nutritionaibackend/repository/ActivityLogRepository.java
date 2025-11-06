package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.ActivityLog;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the ActivityLog entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository.
 * 2. Phương thức truy vấn tùy chỉnh: Phương thức findByUserAndLogDateBetween 
 * là một ví dụ về 'Phương thức tìm kiếm theo Tên' (Query Method).
 * 3. Tự động tạo truy vấn: Spring Data JPA phân tích tên phương thức này 
 * để tự động tạo một truy vấn SQL/JPQL.
 * 4. Chi tiết truy vấn: Nó tìm kiếm tất cả ActivityLog:
 * - WHERE user = [tham số user] AND logDate BETWEEN [startDate] AND [endDate].
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