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

/**
 * {@code FoodServiceImplTest} là một lớp kiểm thử đơn vị cho {@link FoodServiceImpl}.
 * Lớp này sử dụng Mockito để mô phỏng các dependencies ({@link FoodItemRepository} và {@link RecipeRepository})
 * và JUnit 5 để thực thi các trường hợp kiểm thử.
 * Mục tiêu là đảm bảo rằng {@link FoodServiceImpl} hoạt động chính xác trong các tình huống khác nhau,
 * bao gồm lưu, tìm, xóa và tìm kiếm các mặt hàng thực phẩm theo tên.
 */
@ExtendWith(MockitoExtension.class)
class FoodServiceImplTest {

    /**
     * Mô phỏng {@link FoodItemRepository} để kiểm soát hành vi lưu trữ của {@link FoodItem}.
     */
    @Mock
    private FoodItemRepository foodItemRepository;

    /**
     * Mô phỏng {@link RecipeRepository} để kiểm soát hành vi lưu trữ của {@link Recipe}.
     */
    @Mock
    private RecipeRepository recipeRepository;

    /**
     * Tiêm {@link FoodServiceImpl} và tiêm các mock đã tạo ở trên vào đó.
     */
    @InjectMocks
    private FoodServiceImpl foodService;

    private FoodItem foodItem;
    private Recipe recipe;

    /**
     * Thiết lập dữ liệu kiểm thử chung trước mỗi phương thức kiểm thử.
     * Khởi tạo một đối tượng {@link FoodItem} và một đối tượng {@link Recipe} để sử dụng trong các kiểm thử.
     */
    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(2L);

        foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Test Food Item");
        foodItem.setCalories(100.0);
        foodItem.setRecipe(null); // Ban đầu không có công thức
    }

    /**
     * Kiểm tra phương thức {@code save} khi không có công thức đi kèm với mặt hàng thực phẩm.
     * Xác minh rằng {@link FoodItemRepository#save(Object)} được gọi và {@link RecipeRepository#save(Object)}
     * không được gọi.
     */
    @Test
    void save_shouldSaveFoodItem_whenNoRecipe() {
        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        FoodItem savedFoodItem = foodService.save(foodItem);

        assertThat(savedFoodItem).isEqualTo(foodItem);
        verify(foodItemRepository, times(1)).save(foodItem);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    /**
     * Kiểm tra phương thức {@code save} khi một công thức mới được cung cấp cùng với mặt hàng thực phẩm.
     * Xác minh rằng cả {@link RecipeRepository#save(Object)} và {@link FoodItemRepository#save(Object)}
     * đều được gọi.
     */
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

    /**
     * Kiểm tra phương thức {@code save} khi một công thức hiện có được cung cấp cùng với mặt hàng thực phẩm.
     * Xác minh rằng {@link RecipeRepository#save(Object)} và {@link FoodItemRepository#save(Object)}
     * đều được gọi và công thức hiện có được cập nhật.
     */
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

    /**
     * Kiểm tra phương thức {@code findAll} khi có các mặt hàng thực phẩm trong kho lưu trữ.
     * Xác minh rằng tất cả các mặt hàng thực phẩm được trả về chính xác.
     */
    @Test
    void findAll_shouldReturnAllFoodItems() {
        List<FoodItem> foodItemList = Arrays.asList(foodItem, new FoodItem());
        when(foodItemRepository.findAll()).thenReturn(foodItemList);

        List<FoodItem> result = foodService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(foodItemList);
        verify(foodItemRepository, times(1)).findAll();
    }

    /**
     * Kiểm tra phương thức {@code findAll} khi không có mặt hàng thực phẩm nào trong kho lưu trữ.
     * Xác minh rằng một danh sách trống được trả về.
     */
    @Test
    void findAll_shouldReturnEmptyList_whenNoFoodItems() {
        when(foodItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<FoodItem> result = foodService.findAll();

        assertThat(result).isEmpty();
        verify(foodItemRepository, times(1)).findAll();
    }

    /**
     * Kiểm tra phương thức {@code findOne} khi một mặt hàng thực phẩm được tìm thấy bằng ID của nó.
     * Xác minh rằng {@link Optional} chứa mặt hàng thực phẩm được trả về.
     */
    @Test
    void findOne_shouldReturnFoodItem_whenFound() {
        when(foodItemRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        Optional<FoodItem> result = foodService.findOne(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(foodItem);
        verify(foodItemRepository, times(1)).findById(1L);
    }

    /**
     * Kiểm tra phương thức {@code findOne} khi không tìm thấy mặt hàng thực phẩm bằng ID của nó.
     * Xác minh rằng một {@link Optional#empty()} được trả về.
     */
    @Test
    void findOne_shouldReturnEmpty_whenNotFound() {
        when(foodItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<FoodItem> result = foodService.findOne(99L);

        assertThat(result).isNotPresent();
        verify(foodItemRepository, times(1)).findById(99L);
    }

    /**
     * Kiểm tra phương thức {@code delete} khi mặt hàng thực phẩm tồn tại.
     * Xác minh rằng {@link FoodItemRepository#deleteById(Object)} được gọi.
     */
    @Test
    void delete_shouldDeleteFoodItem_whenExists() {
        doNothing().when(foodItemRepository).deleteById(1L);

        foodService.delete(1L);

        verify(foodItemRepository, times(1)).deleteById(1L);
    }

    /**
     * Kiểm tra phương thức {@code delete} khi mặt hàng thực phẩm không tồn tại.
     * Xác minh rằng không có ngoại lệ nào được ném ra và {@link FoodItemRepository#deleteById(Object)}
     * vẫn được gọi (mặc dù nó không có tác dụng).
     */
    @Test
    void delete_shouldNotThrowException_whenFoodItemDoesNotExist() {
        // Mocking behavior for a non-existent ID. deleteById typically doesn't throw an exception
        // if the entity doesn't exist, it just completes without action.
        doNothing().when(foodItemRepository).deleteById(99L);

        foodService.delete(99L);

        verify(foodItemRepository, times(1)).deleteById(99L);
    }

    /**
     * Kiểm tra phương thức {@code findByNameContaining} khi có các mặt hàng thực phẩm khớp với tên đã cho (không phân biệt chữ hoa chữ thường).
     * Xác minh rằng chỉ các mặt hàng thực phẩm khớp được trả về.
     */
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

    /**
     * Kiểm tra phương thức {@code findByNameContaining} khi không có mặt hàng thực phẩm nào khớp với tên đã cho.
     * Xác minh rằng một danh sách trống được trả về.
     */
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

    /**
     * Kiểm tra phương thức {@code findByNameContaining} khi tên tìm kiếm là một chuỗi rỗng.
     * Xác minh rằng tất cả các mặt hàng thực phẩm được trả về.
     */
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

    /**
     * Kiểm tra phương thức {@code findByNameContaining} khi tên tìm kiếm là null.
     * Xác minh rằng tất cả các mặt hàng thực phẩm được trả về.
     */
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

    /**
     * Kiểm tra phương thức {@code findByNameContaining} để đảm bảo nó có thể xử lý các ký tự đặc biệt trong tên.
     * Xác minh rằng các mặt hàng thực phẩm có chứa ký tự đặc biệt được tìm thấy chính xác.
     */
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
