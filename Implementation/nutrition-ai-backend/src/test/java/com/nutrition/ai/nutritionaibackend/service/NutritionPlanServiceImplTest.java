package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@code NutritionPlanServiceImplTest} là một lớp kiểm thử đơn vị cho {@link NutritionPlanServiceImpl}.
 * Lớp này sử dụng Mockito để mô phỏng các dependencies ({@link NutritionPlanRepository} và {@link UserRepository})
 * và JUnit 5 để thực thi các trường hợp kiểm thử.
 * Mục tiêu là đảm bảo rằng {@link NutritionPlanServiceImpl} hoạt động chính xác trong các tình huống khác nhau,
 * bao gồm lưu, tìm, xóa và tìm kiếm các kế hoạch dinh dưỡng liên quan đến người dùng.
 */
@ExtendWith(MockitoExtension.class)
class NutritionPlanServiceImplTest {

    /**
     * Mô phỏng {@link NutritionPlanRepository} để kiểm soát hành vi lưu trữ của {@link NutritionPlan}.
     */
    @Mock
    private NutritionPlanRepository nutritionPlanRepository;

    /**
     * Mô phỏng {@link UserRepository} để kiểm soát hành vi lưu trữ của {@link User}.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Tiêm {@link NutritionPlanServiceImpl} và tiêm các mock đã tạo ở trên vào đó.
     */
    @InjectMocks
    private NutritionPlanServiceImpl nutritionPlanService;

    private User user;
    private NutritionPlan nutritionPlan;

    /**
     * Thiết lập dữ liệu kiểm thử chung trước mỗi phương thức kiểm thử.
     * Khởi tạo một đối tượng {@link User} và một đối tượng {@link NutritionPlan} để sử dụng trong các kiểm thử.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        nutritionPlan = new NutritionPlan();
        nutritionPlan.setId(1L);
        nutritionPlan.setPlanName("Plan 1");
        nutritionPlan.setUser(user);
    }

    /**
     * Kiểm tra phương thức {@code save} khi người dùng tồn tại.
     * Xác minh rằng {@link NutritionPlanRepository#save(Object)} được gọi và kế hoạch dinh dưỡng được lưu thành công.
     */
    @Test
    void save_shouldSaveNutritionPlan_whenUserExists() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(nutritionPlan);

        // Act
        NutritionPlan savedNutritionPlan = nutritionPlanService.save(nutritionPlan);

        // Assert
        assertNotNull(savedNutritionPlan);
        assertEquals(nutritionPlan.getId(), savedNutritionPlan.getId());
        assertEquals(nutritionPlan.getPlanName(), savedNutritionPlan.getPlanName());
        verify(userRepository, times(1)).findById(user.getId());
        verify(nutritionPlanRepository, times(1)).save(nutritionPlan);
    }

    /**
     * Kiểm tra phương thức {@code save} khi người dùng không tồn tại.
     * Xác minh rằng một {@link RuntimeException} được ném ra và phương thức lưu không được gọi.
     */
    @Test
    void save_shouldThrowRuntimeException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> nutritionPlanService.save(nutritionPlan));
        assertEquals("User not found with ID: " + user.getId(), exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    /**
     * Kiểm tra phương thức {@code findAll} khi có các kế hoạch dinh dưỡng trong kho lưu trữ.
     * Xác minh rằng tất cả các kế hoạch dinh dưỡng được trả về chính xác.
     */
    @Test
    void findAll_shouldReturnAllNutritionPlans() {
        // Arrange
        List<NutritionPlan> nutritionPlans = Arrays.asList(nutritionPlan, new NutritionPlan());
        when(nutritionPlanRepository.findAll()).thenReturn(nutritionPlans);

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(nutritionPlanRepository, times(1)).findAll();
    }

    /**
     * Kiểm tra phương thức {@code findAll} khi không có kế hoạch dinh dưỡng nào trong kho lưu trữ.
     * Xác minh rằng một danh sách trống được trả về.
     */
    @Test
    void findAll_shouldReturnEmptyList_whenNoNutritionPlansExist() {
        // Arrange
        when(nutritionPlanRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nutritionPlanRepository, times(1)).findAll();
    }

    /**
     * Kiểm tra phương thức {@code findOne} khi một kế hoạch dinh dưỡng được tìm thấy bằng ID của nó.
     * Xác minh rằng {@link Optional} chứa kế hoạch dinh dưỡng được trả về.
     */
    @Test
    void findOne_shouldReturnNutritionPlan_whenFound() {
        // Arrange
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.of(nutritionPlan));

        // Act
        Optional<NutritionPlan> result = nutritionPlanService.findOne(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(nutritionPlan.getId(), result.get().getId());
        verify(nutritionPlanRepository, times(1)).findById(1L);
    }

    /**
     * Kiểm tra phương thức {@code findOne} khi không tìm thấy kế hoạch dinh dưỡng bằng ID của nó.
     * Xác minh rằng một {@link Optional#empty()} được trả về.
     */
    @Test
    void findOne_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<NutritionPlan> result = nutritionPlanService.findOne(2L);

        // Assert
        assertFalse(result.isPresent());
        verify(nutritionPlanRepository, times(1)).findById(2L);
    }

    /**
     * Kiểm tra phương thức {@code delete} để đảm bảo nó xóa một kế hoạch dinh dưỡng.
     * Xác minh rằng {@link NutritionPlanRepository#deleteById(Object)} được gọi.
     */
    @Test
    void delete_shouldDeleteNutritionPlan() {
        // Act
        nutritionPlanService.delete(1L);

        // Assert
        verify(nutritionPlanRepository, times(1)).deleteById(1L);
    }

    /**
     * Kiểm tra phương thức {@code findAllByUser} khi người dùng có kế hoạch dinh dưỡng.
     * Xác minh rằng các kế hoạch dinh dưỡng liên quan đến người dùng được trả về chính xác.
     */
    @Test
    void findAllByUser_shouldReturnNutritionPlans_whenUserHasPlans() {
        // Arrange
        List<NutritionPlan> userNutritionPlans = Arrays.asList(nutritionPlan);
        when(nutritionPlanRepository.findByUser(any(User.class))).thenReturn(userNutritionPlans);

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUser().getId());
        verify(nutritionPlanRepository, times(1)).findByUser(user);
    }

    /**
     * Kiểm tra phương thức {@code findAllByUser} khi người dùng không có kế hoạch dinh dưỡng.
     * Xác minh rằng một danh sách trống được trả về.
     */
    @Test
    void findAllByUser_shouldReturnEmptyList_whenUserHasNoPlans() {
        // Arrange
        when(nutritionPlanRepository.findByUser(any(User.class))).thenReturn(Arrays.asList());

        // Act
        List<NutritionPlan> result = nutritionPlanService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nutritionPlanRepository, times(1)).findByUser(user);
    }
}
