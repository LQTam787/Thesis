package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link NutritionPlan}.
 * Thực hiện các thao tác quản lý Kế hoạch Dinh dưỡng, đảm bảo tính hợp lệ của User liên quan.
 */
@Service
public class NutritionPlanServiceImpl implements NutritionPlanService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserRepository userRepository;

    // Nguyên lý hoạt động: Dependency Injection
    public NutritionPlanServiceImpl(NutritionPlanRepository nutritionPlanRepository, UserRepository userRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NutritionPlan save(NutritionPlan nutritionPlan) {
        // Luồng hoạt động: Lưu/Cập nhật Kế hoạch Dinh dưỡng. Cần đảm bảo User liên kết là hợp lệ.
        // Nguyên lý hoạt động (Xử lý mối quan hệ):
        // 1. Tìm User trong DB bằng ID từ NutritionPlan. Nếu không tìm thấy, ném ngoại lệ (RuntimeException).
        // 2. Gán lại entity User được quản lý (managed entity) vào NutritionPlan để đảm bảo tính nhất quán trong Persistence Context.
        // 3. Lưu NutritionPlan.
        User user = userRepository.findById(nutritionPlan.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + nutritionPlan.getUser().getId()));
        nutritionPlan.setUser(user); // Re-associate managed user entity
        return nutritionPlanRepository.save(nutritionPlan);
    }

    @Override
    public List<NutritionPlan> findAll() {
        // Luồng hoạt động: Lấy tất cả các Kế hoạch Dinh dưỡng.
        return nutritionPlanRepository.findAll();
    }

    @Override
    public Optional<NutritionPlan> findOne(Long id) {
        // Luồng hoạt động: Tìm Kế hoạch Dinh dưỡng theo ID.
        return nutritionPlanRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        // Luồng hoạt động: Xóa Kế hoạch Dinh dưỡng theo ID.
        nutritionPlanRepository.deleteById(id);
    }

    @Override
    public List<NutritionPlan> findAllByUser(User user) {
        // Luồng hoạt động: Tìm tất cả Kế hoạch Dinh dưỡng thuộc về một User cụ thể.
        // Nguyên lý hoạt động: Gọi phương thức được tạo tự động trong Repository dựa trên tên thuộc tính (`findByUser`).
        return nutritionPlanRepository.findByUser(user);
    }
}