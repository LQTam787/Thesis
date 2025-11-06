package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FoodItem entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<FoodItem, Long>.
 * 2. Tự động triển khai: Spring Data JPA cung cấp các phương thức CRUD 
 * tiêu chuẩn cho thực thể FoodItem.
 */
@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}