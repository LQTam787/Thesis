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
 */
@Service
public class FoodServiceImpl implements FoodService {

    private final FoodItemRepository foodItemRepository;
    private final RecipeRepository recipeRepository;

    public FoodServiceImpl(FoodItemRepository foodItemRepository, RecipeRepository recipeRepository) {
        this.foodItemRepository = foodItemRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public FoodItem save(FoodItem foodItem) {
        // If the food item has a recipe, ensure the bidirectional relationship is set and the recipe is saved
        if (foodItem.getRecipe() != null) {
            foodItem.getRecipe().setFoodItem(foodItem);
            // Save the recipe first if it's new or updated
            recipeRepository.save(foodItem.getRecipe());
        }
        return foodItemRepository.save(foodItem);
    }

    @Override
    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }

    @Override
    public Optional<FoodItem> findOne(Long id) {
        return foodItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        foodItemRepository.deleteById(id);
    }

    @Override
    public List<FoodItem> findByNameContaining(String name) {
        return foodItemRepository.findAll().stream()
            .filter(food -> food.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
    }
}
