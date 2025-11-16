package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.model.domain.Recipe;
import com.nutrition.ai.nutritionaibackend.repository.FoodItemRepository;
import com.nutrition.ai.nutritionaibackend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceImplTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private FoodServiceImpl foodService;

    private FoodItem foodItem;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(2L);

        foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Test Food Item");
        foodItem.setCalories(100.0);
        foodItem.setRecipe(null); // Initially no recipe
    }

    @Test
    void save_shouldSaveFoodItem_whenNoRecipe() {
        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        FoodItem savedFoodItem = foodService.save(foodItem);

        assertThat(savedFoodItem).isEqualTo(foodItem);
        verify(foodItemRepository, times(1)).save(foodItem);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void save_shouldSaveFoodItemAndRecipe_whenNewRecipe() {
        foodItem.setRecipe(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        FoodItem savedFoodItem = foodService.save(foodItem);

        assertThat(savedFoodItem).isEqualTo(foodItem);
        assertThat(savedFoodItem.getRecipe().getFoodItem()).isEqualTo(foodItem);
        verify(recipeRepository, times(1)).save(recipe);
        verify(foodItemRepository, times(1)).save(foodItem);
    }

    @Test
    void save_shouldSaveFoodItemAndExistingRecipe_whenExistingRecipe() {
        // Simulate an existing recipe with an ID
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(10L); // An existing ID

        foodItem.setRecipe(existingRecipe);
        when(recipeRepository.save(existingRecipe)).thenReturn(existingRecipe);
        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        FoodItem savedFoodItem = foodService.save(foodItem);

        assertThat(savedFoodItem).isEqualTo(foodItem);
        assertThat(savedFoodItem.getRecipe().getFoodItem()).isEqualTo(foodItem);
        verify(recipeRepository, times(1)).save(existingRecipe);
        verify(foodItemRepository, times(1)).save(foodItem);
    }

    @Test
    void findAll_shouldReturnAllFoodItems() {
        List<FoodItem> foodItemList = Arrays.asList(foodItem, new FoodItem());
        when(foodItemRepository.findAll()).thenReturn(foodItemList);

        List<FoodItem> result = foodService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(foodItemList);
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoFoodItems() {
        when(foodItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<FoodItem> result = foodService.findAll();

        assertThat(result).isEmpty();
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findOne_shouldReturnFoodItem_whenFound() {
        when(foodItemRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        Optional<FoodItem> result = foodService.findOne(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(foodItem);
        verify(foodItemRepository, times(1)).findById(1L);
    }

    @Test
    void findOne_shouldReturnEmpty_whenNotFound() {
        when(foodItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<FoodItem> result = foodService.findOne(99L);

        assertThat(result).isNotPresent();
        verify(foodItemRepository, times(1)).findById(99L);
    }

    @Test
    void delete_shouldDeleteFoodItem_whenExists() {
        doNothing().when(foodItemRepository).deleteById(1L);

        foodService.delete(1L);

        verify(foodItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldNotThrowException_whenFoodItemDoesNotExist() {
        // Mocking behavior for a non-existent ID. deleteById typically doesn't throw an exception
        // if the entity doesn't exist, it just completes without action.
        doNothing().when(foodItemRepository).deleteById(99L);

        foodService.delete(99L);

        verify(foodItemRepository, times(1)).deleteById(99L);
    }

    @Test
    void findByNameContaining_shouldReturnMatchingFoodItems_caseInsensitive() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Apple");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("Banana");
        FoodItem foodItem3 = new FoodItem();
        foodItem3.setName("apple pie");
        List<FoodItem> allFoodItems = Arrays.asList(foodItem1, foodItem2, foodItem3);
        when(foodItemRepository.findAll()).thenReturn(allFoodItems);

        List<FoodItem> result = foodService.findByNameContaining("apple");

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(foodItem1, foodItem3);
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findByNameContaining_shouldReturnEmptyList_whenNoMatch() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Apple");
        List<FoodItem> allFoodItems = Collections.singletonList(foodItem1);
        when(foodItemRepository.findAll()).thenReturn(allFoodItems);

        List<FoodItem> result = foodService.findByNameContaining("orange");

        assertThat(result).isEmpty();
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findByNameContaining_shouldReturnAllFoodItems_whenEmptyName() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Apple");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("Banana");
        List<FoodItem> allFoodItems = Arrays.asList(foodItem1, foodItem2);
        when(foodItemRepository.findAll()).thenReturn(allFoodItems);

        List<FoodItem> result = foodService.findByNameContaining("");

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(allFoodItems);
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findByNameContaining_shouldReturnAllFoodItems_whenNullName() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Apple");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("Banana");
        List<FoodItem> allFoodItems = Arrays.asList(foodItem1, foodItem2);
        when(foodItemRepository.findAll()).thenReturn(allFoodItems);

        List<FoodItem> result = foodService.findByNameContaining(null);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(allFoodItems);
        verify(foodItemRepository, times(1)).findAll();
    }

    @Test
    void findByNameContaining_shouldHandleSpecialCharacters() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Food-Item-!");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("AnotherFood");
        List<FoodItem> allFoodItems = Arrays.asList(foodItem1, foodItem2);
        when(foodItemRepository.findAll()).thenReturn(allFoodItems);

        List<FoodItem> result = foodService.findByNameContaining("-");

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(foodItem1);
        verify(foodItemRepository, times(1)).findAll();
    }
}
