package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FoodItem}.
 * Định nghĩa các thao tác CRUD cơ bản và tìm kiếm theo tên cho Món ăn.
 */
public interface FoodService {

    /**
     * Luồng hoạt động: Lưu một món ăn.
     * Nguyên lý hoạt động: Uỷ quyền cho Repository để lưu đối tượng vào DB.
     *
     * @param foodItem the entity to save.
     * @return the persisted entity.
     */
    FoodItem save(FoodItem foodItem);

    /**
     * Luồng hoạt động: Truy xuất tất cả các món ăn.
     * Nguyên lý hoạt động: Gọi `findAll()` của FoodItemRepository.
     *
     * @return the list of entities.
     */
    List<FoodItem> findAll();

    /**
     * Luồng hoạt động: Tìm một món ăn theo ID.
     * Nguyên lý hoạt động: Gọi `findById(id)` của FoodItemRepository.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodItem> findOne(Long id);

    /**
     * Luồng hoạt động: Xóa một món ăn theo ID.
     * Nguyên lý hoạt động: Gọi `deleteById(id)` của FoodItemRepository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Luồng hoạt động: Tìm kiếm các món ăn có tên chứa một chuỗi cụ thể.
     * Nguyên lý hoạt động: Thực hiện logic tìm kiếm tên.
     *
     * @param name the name to search for.
     * @return a list of matching food items.
     */
    List<FoodItem> findByNameContaining(String name);
}