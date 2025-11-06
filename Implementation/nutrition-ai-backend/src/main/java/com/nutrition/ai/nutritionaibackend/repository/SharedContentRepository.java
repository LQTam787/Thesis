package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.shared.SharedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link SharedContent} entities.
 * Provides standard CRUD operations and custom query methods for accessing shared content data.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Interface này mở rộng JpaRepository<SharedContent, Long>.
 * 2. Tự động triển khai: Spring Data JPA cung cấp các phương thức CRUD 
 * tiêu chuẩn cho thực thể SharedContent.
 */
@Repository
public interface SharedContentRepository extends JpaRepository<SharedContent, Long> {
}