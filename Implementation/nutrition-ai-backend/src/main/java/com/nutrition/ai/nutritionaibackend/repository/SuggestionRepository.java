package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.Suggestion;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Suggestion entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository<Suggestion, Long>.
 * 2. Phương thức truy vấn tùy chỉnh: findByUser là 'Phương thức tìm kiếm theo Tên'.
 * 3. Tự động tạo truy vấn: Spring Data JPA tạo truy vấn để tìm các gợi ý (Suggestion)
 * - WHERE user = [tham số user].
 */
@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUser(User user);
}