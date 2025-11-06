package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activity entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<Activity, Long>.
 * 2. Tự động triển khai: Spring Data JPA sẽ tự động tạo một triển khai 
 * (proxy) cho interface này tại thời điểm chạy.
 * 3. Chức năng CRUD: Triển khai tự động này cung cấp các phương thức CRUD
 * (ví dụ: save, findById, findAll, delete) cho thực thể Activity.
 * 4. Kiểu thực thể và ID: 'Activity' là thực thể mà repository này quản lý, 
 * và 'Long' là kiểu dữ liệu của khóa chính của thực thể Activity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}