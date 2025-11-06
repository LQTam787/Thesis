package com.nutrition.ai.nutritionaibackend.repository;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the NutritionPlan entity.
 * * Luồng hoạt động & Nguyên lý hoạt động:
 * 1. Kế thừa: Cung cấp các phương thức CRUD cơ bản từ JpaRepository<NutritionPlan, Long>.
 * 2. Phương thức truy vấn tùy chỉnh: findByUser là một 'Phương thức tìm kiếm theo Tên' (Query Method).
 * 3. Tự động tạo truy vấn: Spring Data JPA phân tích tên phương thức này để tự động tạo một truy vấn.
 * 4. Chi tiết truy vấn: Nó tìm kiếm tất cả các kế hoạch dinh dưỡng (NutritionPlan)
 * - WHERE user = [tham số user].
 */
@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findByUser(User user);
}