package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodLog;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the FoodLog entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository.
 * 2. Phương thức truy vấn tùy chỉnh: findByUserAndLogDateBetween là một 
 * 'Phương thức tìm kiếm theo Tên'.
 * 3. Tự động tạo truy vấn: Spring Data JPA phân tích tên phương thức để 
 * tạo một truy vấn tìm kiếm FoodLog:
 * - WHERE user = [tham số user] AND logDate BETWEEN [startDate] AND [endDate].
 */
@Repository
public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {
    List<FoodLog> findByUserAndLogDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
}