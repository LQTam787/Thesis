package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DailyMenu entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<DailyMenu, Long>.
 * 2. Tự động triển khai: Spring Data JPA sẽ tự động tạo một triển khai 
 * cung cấp các phương thức CRUD cho thực thể DailyMenu.
 * 3. Chức năng CRUD: Các phương thức cơ bản (ví dụ: save, findById, findAll) 
 * được cung cấp sẵn.
 */
@Repository
public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {
}