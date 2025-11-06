package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Goal entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<Goal, Long>.
 * 2. Tự động triển khai: Spring Data JPA cung cấp các phương thức CRUD 
 * tiêu chuẩn cho thực thể Goal.
 */
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}