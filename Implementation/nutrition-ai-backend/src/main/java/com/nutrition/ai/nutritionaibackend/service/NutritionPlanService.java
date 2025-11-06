package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link NutritionPlan}.
 * Định nghĩa các thao tác CRUD cơ bản và tìm kiếm theo người dùng cho Kế hoạch Dinh dưỡng.
 */
public interface NutritionPlanService {

    /**
     * Luồng hoạt động: Lưu một kế hoạch dinh dưỡng mới hoặc cập nhật một kế hoạch hiện có.
     * Nguyên lý hoạt động: Thường ủy quyền cho Repository để thực hiện thao tác lưu vào cơ sở dữ liệu.
     *
     * @param nutritionPlan the entity to save.
     * @return the persisted entity.
     */
    NutritionPlan save(NutritionPlan nutritionPlan);

    /**
     * Luồng hoạt động: Truy xuất tất cả các kế hoạch dinh dưỡng có trong hệ thống.
     * Nguyên lý hoạt động: Gọi phương thức `findAll()` từ NutritionPlanRepository.
     *
     * @return the list of entities.
     */
    List<NutritionPlan> findAll();

    /**
     * Luồng hoạt động: Tìm kiếm một kế hoạch dinh dưỡng cụ thể theo ID.
     * Nguyên lý hoạt động: Sử dụng `findById(id)` của Repository, trả về Optional để xử lý trường hợp không tìm thấy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NutritionPlan> findOne(Long id);

    /**
     * Luồng hoạt động: Xóa một kế hoạch dinh dưỡng khỏi cơ sở dữ liệu bằng ID.
     * Nguyên lý hoạt động: Gọi `deleteById(id)` của NutritionPlanRepository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Luồng hoạt động: Tìm kiếm tất cả kế hoạch dinh dưỡng được liên kết với một người dùng cụ thể.
     * Nguyên lý hoạt động: Sử dụng một phương thức tùy chỉnh trong Repository (ví dụ: `findByUser`).
     *
     * @param user the user to find plans for.
     * @return a list of nutrition plans.
     */
    List<NutritionPlan> findAllByUser(User user);
}