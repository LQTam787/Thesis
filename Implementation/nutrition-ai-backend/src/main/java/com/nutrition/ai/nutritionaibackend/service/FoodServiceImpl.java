package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.repository.FoodItemRepository;
import com.nutrition.ai.nutritionaibackend.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link FoodItem}.
 * Thực hiện logic nghiệp vụ cho các thao tác với FoodItem, tương tác với FoodItemRepository và RecipeRepository.
 */
@Service
public class FoodServiceImpl implements FoodService {

    private final FoodItemRepository foodItemRepository;
    private final RecipeRepository recipeRepository;

    // Nguyên lý hoạt động: Dependency Injection thông qua Constructor
    // Spring sẽ tự động cung cấp các instance của Repository khi tạo bean FoodServiceImpl.
    public FoodServiceImpl(FoodItemRepository foodItemRepository, RecipeRepository recipeRepository) {
        this.foodItemRepository = foodItemRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public FoodItem save(FoodItem foodItem) {
        // Luồng hoạt động: Lưu FoodItem. Nếu FoodItem là một Recipe (có trường recipe != null), cần đảm bảo tính toàn vẹn dữ liệu.
        // Nguyên lý hoạt động (Logic Song hướng):
        // 1. Nếu FoodItem có Recipe được liên kết, thiết lập mối quan hệ song hướng (setFoodItem trên Recipe).
        // 2. Lưu Recipe trước để đảm bảo Recipe có ID và được cập nhật/lưu.
        // 3. Cuối cùng, lưu FoodItem.
        if (foodItem.getRecipe() != null) {
            foodItem.getRecipe().setFoodItem(foodItem);
            // Save the recipe first if it's new or updated
            recipeRepository.save(foodItem.getRecipe());
        }
        return foodItemRepository.save(foodItem);
    }

    @Override
    public List<FoodItem> findAll() {
        // Luồng hoạt động: Truy xuất tất cả FoodItem.
        return foodItemRepository.findAll();
    }

    @Override
    public Optional<FoodItem> findOne(Long id) {
        // Luồng hoạt động: Tìm FoodItem theo ID.
        return foodItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        // Luồng hoạt động: Xóa FoodItem theo ID.
        foodItemRepository.deleteById(id);
    }

    @Override
    public List<FoodItem> findByNameContaining(String name) {
        // Luồng hoạt động: Tìm kiếm FoodItem có tên chứa chuỗi đã cho (không phân biệt chữ hoa/chữ thường).
        // Nguyên lý hoạt động (Tìm kiếm tại Service - kém hiệu quả hơn tìm kiếm tại Repository):
        // 1. Lấy tất cả các FoodItem từ DB (`findAll()`).
        // 2. Lọc (filter) danh sách trong bộ nhớ (Stream API) dựa trên tên.
        // 3. Chuyển đổi cả tên FoodItem và chuỗi tìm kiếm thành chữ thường để so sánh.
        return foodItemRepository.findAll().stream()
            .filter(food -> {
                if (name == null || name.isEmpty()) {
                    return true; // Return all food items if name is null or empty
                }
                return food.getName().toLowerCase().contains(name.toLowerCase());
            })
            .collect(Collectors.toList());
    }
}