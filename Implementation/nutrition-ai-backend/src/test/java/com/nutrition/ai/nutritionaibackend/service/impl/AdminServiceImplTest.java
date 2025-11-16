package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.*;
import com.nutrition.ai.nutritionaibackend.exception.ResourceNotFoundException;
import com.nutrition.ai.nutritionaibackend.model.domain.*;
import com.nutrition.ai.nutritionaibackend.model.enums.EGoalType;
import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import com.nutrition.ai.nutritionaibackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@code AdminServiceImplTest} là một lớp kiểm thử đơn vị cho {@link AdminServiceImpl}.
 * Lớp này sử dụng JUnit 5 và Mockito để kiểm thử các phương thức logic nghiệp vụ của dịch vụ quản trị,
 * cô lập {@link AdminServiceImpl} khỏi các phụ thuộc bên ngoài như cơ sở dữ liệu thông qua các repository mock
 * ({@link UserRepository}, {@link RoleRepository}, {@link RecipeRepository}, {@link GoalRepository}, {@link NutritionPlanRepository})
 * và ánh xạ đối tượng thông qua {@link ModelMapper}.
 * Mục tiêu chính là đảm bảo rằng các chức năng quản trị người dùng, công thức, mục tiêu và kế hoạch dinh dưỡng
 * hoạt động chính xác trong các tình huống khác nhau, bao gồm tạo, đọc, cập nhật, xóa (CRUD) và xử lý các trường hợp biên như không tìm thấy tài nguyên.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Admin Service Impl Unit Tests")
class AdminServiceImplTest {

    /**
     * Mô phỏng {@link UserRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link User}.
     */
    @Mock
    private UserRepository userRepository;
    /**
     * Mô phỏng {@link RoleRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link Role}.
     */
    @Mock
    private RoleRepository roleRepository;
    /**
     * Mô phỏng {@link RecipeRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link Recipe}.
     */
    @Mock
    private RecipeRepository recipeRepository;
    /**
     * Mô phỏng {@link GoalRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link Goal}.
     */
    @Mock
    private GoalRepository goalRepository;
    /**
     * Mô phỏng {@link NutritionPlanRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link NutritionPlan}.
     */
    @Mock
    private NutritionPlanRepository nutritionPlanRepository;
    /**
     * Mô phỏng {@link ModelMapper} để kiểm soát hành vi ánh xạ giữa các đối tượng DTO và Domain.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Tiêm {@link AdminServiceImpl} và tự động tiêm các đối tượng {@code @Mock} vào các trường tương ứng của nó.
     * Đây là đối tượng thực sẽ được kiểm thử.
     */
    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private UserResponseDto userResponseDto;
    private Role adminRole;
    private Role userRole;
    private Recipe recipe;
    private RecipeDto recipeDto;
    private Goal goal;
    private GoalDto goalDto;
    private NutritionPlan nutritionPlan;
    private NutritionPlanDto nutritionPlanDto;

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (phương thức được chú thích bởi {@code @Test}).
     * Phương thức này chịu trách nhiệm khởi tạo các đối tượng dữ liệu giả định như {@link User}, {@link Recipe},
     * {@link Goal}, {@link NutritionPlan} và các DTO tương ứng để sử dụng trong các kiểm thử.
     * Đồng thời, nó cũng khởi tạo các đối tượng {@link Role} cho vai trò ADMIN và USER.
     */
    @BeforeEach
    void setUp() {
        adminRole = new Role(ERole.ROLE_ADMIN);
        userRole = new Role(ERole.ROLE_USER);

        user = new User(1L, "testuser", "test@example.com", "password", new HashSet<>(Arrays.asList(userRole)));
        userResponseDto = new UserResponseDto(1L, "testuser", "test@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));

        FoodItem setupFoodItem = new FoodItem();
        setupFoodItem.setId(1L);
        setupFoodItem.setName("Apple");
        setupFoodItem.setCalories(52.0);
        setupFoodItem.setProtein(0.3);
        setupFoodItem.setCarbs(14.0);
        setupFoodItem.setFats(0.2);
        setupFoodItem.setServingSize("1 medium");

        recipe = new Recipe(1L, setupFoodItem, "Bake it", 60);
        recipeDto = new RecipeDto(1L, "Bake it", 60, 1L);

        goal = new Goal(1L, 70.0, LocalDate.now().plusMonths(3), EGoalType.WEIGHT_LOSS, "ACTIVE", user, "Maintain good health");
        goalDto = new GoalDto(1L, 70.0, LocalDate.now().plusMonths(3), EGoalType.WEIGHT_LOSS, "ACTIVE", 1L, "Maintain good health");

        nutritionPlan = new NutritionPlan(1L, "Weight Loss Plan", LocalDate.now(), LocalDate.now().plusMonths(1), "Reduce calories", user, new java.util.ArrayList<>());
        nutritionPlanDto = new NutritionPlanDto(1L, "Weight Loss Plan", LocalDate.now(), LocalDate.now().plusMonths(1), "Reduce calories", 1L, new java.util.ArrayList<>());
    }

    /**
     * Kiểm thử phương thức {@code getAllUsers} của {@link AdminServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findAll()} được gọi, nó sẽ trả về
     *    một danh sách chứa đối tượng {@link User} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link User} thành {@link UserResponseDto},
     *    nó sẽ trả về đối tượng {@link UserResponseDto} giả định.
     * 3. Gọi phương thức {@code getAllUsers} thực tế của {@code adminService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 1.
     *    - Kiểm tra xem đối tượng {@link UserResponseDto} trong danh sách có khớp với đối tượng giả định hay không.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findAll()} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(user, UserResponseDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getAllUsers - Success")
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        List<UserResponseDto> result = adminService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponseDto, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getUserById} của {@link AdminServiceImpl} trong trường hợp tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(1L)} được gọi, nó sẽ trả về
     *    một {@link Optional} chứa đối tượng {@link User} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link User} thành {@link UserResponseDto},
     *    nó sẽ trả về đối tượng {@link UserResponseDto} giả định.
     * 3. Gọi phương thức {@code getUserById} thực tế của {@code adminService} với ID 1L.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link UserResponseDto} trả về không null và khớp với đối tượng giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById(1L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(user, UserResponseDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getUserById - Success")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userResponseDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getUserById} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code getUserById} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById(99L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map} KHÔNG BAO GIỜ được gọi, vì không có người dùng để ánh xạ.
     */
    @Test
    @DisplayName("Test getUserById - Not Found")
    void getUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getUserById(99L));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code updateUser} của {@link AdminServiceImpl} trong trường hợp cập nhật thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link UpdateUserRequestDto} với thông tin người dùng được cập nhật (bao gồm vai trò ADMIN).
     * 2. Tạo các đối tượng {@link User} và {@link UserResponseDto} giả định sau khi cập nhật.
     * 3. Thiết lập hành vi giả lập cho các repository:
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} ban đầu.
     *    - {@code roleRepository.findByName(ERole.ROLE_ADMIN)} trả về đối tượng {@link Role} ADMIN.
     *    - {@code userRepository.save(any(User.class))} trả về đối tượng {@link User} đã cập nhật.
     * 4. Thiết lập hành vi giả lập cho {@code modelMapper}: ánh xạ {@link User} đã cập nhật thành {@link UserResponseDto} đã cập nhật.
     * 5. Gọi phương thức {@code updateUser} thực tế của {@code adminService}.
     * 6. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link UserResponseDto} trả về không null và khớp với đối tượng giả định đã cập nhật.
     * 7. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code findByName} và {@code save} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link User} được lưu trữ, đảm bảo
     *      rằng tên người dùng, email và vai trò đã được cập nhật chính xác.
     */
    @Test
    @DisplayName("Test updateUser - Success")
    void updateUser_Success() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_ADMIN")));
        User updatedUserEntity = new User(1L, "updateduser", "updated@example.com", "password", new HashSet<>(Arrays.asList(adminRole)));
        UserResponseDto updatedUserResponseDto = new UserResponseDto(1L, "updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_ADMIN")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserEntity);
        when(modelMapper.map(updatedUserEntity, UserResponseDto.class)).thenReturn(updatedUserResponseDto);

        UserResponseDto result = adminService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedUserResponseDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName(ERole.ROLE_ADMIN);
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(updatedUserEntity, UserResponseDto.class);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("updateduser", capturedUser.getUsername());
        assertEquals("updated@example.com", capturedUser.getEmail());
        assertTrue(capturedUser.getRoles().contains(adminRole));
    }

    /**
     * Kiểm thử phương thức {@code updateUser} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Tạo một {@link UpdateUserRequestDto}.
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code updateUser} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findByName}, {@code save} và {@code map} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @Test
    @DisplayName("Test updateUser - User Not Found")
    void updateUser_UserNotFound() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateUser(99L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any(User.class));
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code updateUser} của {@link AdminServiceImpl} trong trường hợp không tìm thấy vai trò.
     * Luồng hoạt động:
     * 1. Tạo một {@link UpdateUserRequestDto} với vai trò "ROLE_USER".
     * 2. Thiết lập hành vi giả lập cho các repository:
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} ban đầu.
     *    - {@code roleRepository.findByName(ERole.ROLE_USER)} trả về {@link Optional#empty()} (không tìm thấy vai trò).
     * 3. Gọi phương thức {@code updateUser} thực tế của {@code adminService} với ID 1L.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng {@code userRepository.save} và {@code modelMapper.map} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @Test
    @DisplayName("Test updateUser - Role Not Found")
    void updateUser_RoleNotFound() {
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("updateduser", "updated@example.com", new HashSet<>(Arrays.asList("ROLE_USER")));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateUser(1L, updateRequest));

        assertEquals("Role not found with name : 'ROLE_USER'", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(userRepository, never()).save(any(User.class));
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code deleteUser} của {@link AdminServiceImpl} trong trường hợp xóa thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}:
     *    - {@code findById(1L)} trả về đối tượng {@link User} giả định.
     *    - {@code delete(user)} không làm gì cả (doNothing).
     * 2. Gọi phương thức {@code deleteUser} thực tế của {@code adminService} với ID 1L.
     * 3. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById(1L)} và {@code userRepository.delete(user)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test deleteUser - Success")
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        adminService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Kiểm thử phương thức {@code deleteUser} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code deleteUser} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code userRepository.delete} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test deleteUser - Not Found")
    void deleteUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteUser(99L));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).delete(any(User.class));
    }

    /**
     * Kiểm thử phương thức {@code getAllRecipes} của {@link AdminServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code recipeRepository}: khi {@code findAll()} được gọi, nó sẽ trả về
     *    một danh sách chứa đối tượng {@link Recipe} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link Recipe} thành {@link RecipeDto},
     *    nó sẽ trả về đối tượng {@link RecipeDto} giả định.
     * 3. Gọi phương thức {@code getAllRecipes} thực tế của {@code adminService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 1.
     *    - Kiểm tra xem đối tượng {@link RecipeDto} trong danh sách có khớp với đối tượng giả định hay không.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code recipeRepository.findAll()} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(recipe, RecipeDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getAllRecipes - Success")
    void getAllRecipes_Success() {
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe));
        when(modelMapper.map(recipe, RecipeDto.class)).thenReturn(recipeDto);

        List<RecipeDto> result = adminService.getAllRecipes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipeDto, result.get(0));
        verify(recipeRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(recipe, RecipeDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getRecipeById} của {@link AdminServiceImpl} trong trường hợp tìm thấy công thức.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code recipeRepository}: khi {@code findById(1L)} được gọi, nó sẽ trả về
     *    một {@link Optional} chứa đối tượng {@link Recipe} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link Recipe} thành {@link RecipeDto},
     *    nó sẽ trả về đối tượng {@link RecipeDto} giả định.
     * 3. Gọi phương thức {@code getRecipeById} thực tế của {@code adminService} với ID 1L.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link RecipeDto} trả về không null và khớp với đối tượng giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code recipeRepository.findById(1L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(recipe, RecipeDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getRecipeById - Success")
    void getRecipeById_Success() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(modelMapper.map(recipe, RecipeDto.class)).thenReturn(recipeDto);

        RecipeDto result = adminService.getRecipeById(1L);

        assertNotNull(result);
        assertEquals(recipeDto, result);
        verify(recipeRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(recipe, RecipeDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getRecipeById} của {@link AdminServiceImpl} trong trường hợp không tìm thấy công thức.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code recipeRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy công thức).
     * 2. Gọi phương thức {@code getRecipeById} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code recipeRepository.findById(99L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map} KHÔNG BAO GIỜ được gọi, vì không có công thức để ánh xạ.
     */
    @Test
    @DisplayName("Test getRecipeById - Not Found")
    void getRecipeById_NotFound() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getRecipeById(99L));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code createRecipe} của {@link AdminServiceImpl} trong trường hợp tạo công thức thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link RecipeRequestDto} chứa thông tin công thức mới.
     * 2. Tạo các đối tượng {@link FoodItem} và {@link Recipe} giả định để mô phỏng dữ liệu đã lưu.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code recipeRepository.save(any(Recipe.class))} trả về đối tượng {@link Recipe} đã lưu.
     *    - {@code modelMapper.map(any(Recipe.class), eq(RecipeDto.class))} trả về đối tượng {@link RecipeDto} đã lưu.
     * 4. Gọi phương thức {@code createRecipe} thực tế của {@code adminService} với {@link RecipeRequestDto}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link RecipeDto} trả về không null và khớp với đối tượng giả định đã lưu.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code save} và {@code map} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link Recipe} được lưu trữ, đảm bảo
     *      rằng tên, hướng dẫn và thời gian chuẩn bị của công thức đã được thiết lập chính xác.
     */
    @Test
    @DisplayName("Test createRecipe - Success")
    void createRecipe_Success() {
        RecipeRequestDto createRequest = new RecipeRequestDto("New Recipe", 100.0, 10.0, 20.0, 5.0, "1 serving", "New instructions", 30);
        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setName("New Recipe");
        newFoodItem.setCalories(100.0);
        newFoodItem.setProtein(10.0);
        newFoodItem.setCarbs(20.0);
        newFoodItem.setFats(5.0);
        newFoodItem.setServingSize("1 serving");

        Recipe savedRecipe = new Recipe(2L, newFoodItem, "New instructions", 30);
        RecipeDto savedRecipeDto = new RecipeDto(2L, "New instructions", 30, 2L);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
        when(modelMapper.map(any(Recipe.class), eq(RecipeDto.class))).thenReturn(savedRecipeDto);

        RecipeDto result = adminService.createRecipe(createRequest);

        assertNotNull(result);
        assertEquals(savedRecipeDto, result);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(modelMapper, times(1)).map(any(Recipe.class), eq(RecipeDto.class));

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();
        assertEquals("New Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals("New instructions", capturedRecipe.getInstructions());
        assertEquals(30, capturedRecipe.getPreparationTime());
        assertNotNull(capturedRecipe.getFoodItem());
        assertEquals("New Recipe", capturedRecipe.getFoodItem().getName());
    }

    /**
     * Kiểm thử phương thức {@code updateRecipe} của {@link AdminServiceImpl} trong trường hợp cập nhật thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link RecipeRequestDto} chứa thông tin công thức được cập nhật.
     * 2. Tạo các đối tượng {@link Recipe} và {@link RecipeDto} giả định sau khi cập nhật.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code recipeRepository.findById(1L)} trả về đối tượng {@link Recipe} ban đầu.
     *    - {@code recipeRepository.save(any(Recipe.class))} trả về đối tượng {@link Recipe} đã cập nhật.
     *    - {@code modelMapper.map(updatedRecipeEntity, RecipeDto.class))} trả về đối tượng {@link RecipeDto} đã cập nhật.
     * 4. Gọi phương thức {@code updateRecipe} thực tế của {@code adminService}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link RecipeDto} trả về không null và khớp với đối tượng giả định đã cập nhật.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code save} và {@code map} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link Recipe} được lưu trữ, đảm bảo
     *      rằng tên, hướng dẫn, thời gian chuẩn bị và thông tin dinh dưỡng của công thức đã được cập nhật chính xác.
     */
    @Test
    @DisplayName("Test updateRecipe - Success")
    void updateRecipe_Success() {
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Recipe", 150.0, 15.0, 25.0, 10.0, "2 servings", "Updated instructions", 45);
        FoodItem originalFoodItem = recipe.getFoodItem();
        Recipe updatedRecipeEntity = new Recipe(1L, originalFoodItem, "Updated instructions", 45);
        RecipeDto updatedRecipeDto = new RecipeDto(1L, "Updated instructions", 45, originalFoodItem.getId());

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updatedRecipeEntity);
        when(modelMapper.map(updatedRecipeEntity, RecipeDto.class)).thenReturn(updatedRecipeDto);

        RecipeDto result = adminService.updateRecipe(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedRecipeDto, result);
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(modelMapper, times(1)).map(updatedRecipeEntity, RecipeDto.class);

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();
        assertEquals("Updated Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals("Updated instructions", capturedRecipe.getInstructions());
        assertEquals(45, capturedRecipe.getPreparationTime());
        assertNotNull(capturedRecipe.getFoodItem());
        assertEquals("Updated Recipe", capturedRecipe.getFoodItem().getName());
        assertEquals(150.0, capturedRecipe.getFoodItem().getCalories());
    }

    /**
     * Kiểm thử phương thức {@code updateRecipe} của {@link AdminServiceImpl} trong trường hợp không tìm thấy công thức.
     * Luồng hoạt động:
     * 1. Tạo một {@link RecipeRequestDto}.
     * 2. Thiết lập hành vi giả lập cho {@code recipeRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy công thức).
     * 3. Gọi phương thức {@code updateRecipe} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code save} và {@code map} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test updateRecipe - Not Found")
    void updateRecipe_NotFound() {
        RecipeRequestDto updateRequest = new RecipeRequestDto("Updated Recipe", 150.0, 15.0, 25.0, 10.0, "2 servings", "Updated instructions", 45);
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateRecipe(99L, updateRequest));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).findById(99L);
        verify(recipeRepository, never()).save(any(Recipe.class));
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code deleteRecipe} của {@link AdminServiceImpl} trong trường hợp xóa thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code recipeRepository}:
     *    - {@code existsById(1L)} trả về {@code true} (công thức tồn tại).
     *    - {@code deleteById(1L)} không làm gì cả (doNothing).
     * 2. Gọi phương thức {@code deleteRecipe} thực tế của {@code adminService} với ID 1L.
     * 3. Xác minh tương tác:
     *    - Đảm bảo rằng {@code recipeRepository.existsById(1L)} và {@code recipeRepository.deleteById(1L)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test deleteRecipe - Success")
    void deleteRecipe_Success() {
        when(recipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(1L);

        adminService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).existsById(1L);
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    /**
     * Kiểm thử phương thức {@code deleteRecipe} của {@link AdminServiceImpl} trong trường hợp không tìm thấy công thức.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code recipeRepository}: khi {@code existsById(anyLong())} được gọi,
     *    nó sẽ trả về {@code false} (không tìm thấy công thức).
     * 2. Gọi phương thức {@code deleteRecipe} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code recipeRepository.deleteById} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test deleteRecipe - Not Found")
    void deleteRecipe_NotFound() {
        when(recipeRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteRecipe(99L));

        assertEquals("Recipe not found with id : '99'", exception.getMessage());
        verify(recipeRepository, times(1)).existsById(99L);
        verify(recipeRepository, never()).deleteById(anyLong());
    }

    /**
     * Kiểm thử phương thức {@code getAllGoals} của {@link AdminServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code goalRepository}: khi {@code findAll()} được gọi, nó sẽ trả về
     *    một danh sách chứa đối tượng {@link Goal} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link Goal} thành {@link GoalDto},
     *    nó sẽ trả về đối tượng {@link GoalDto} giả định.
     * 3. Gọi phương thức {@code getAllGoals} thực tế của {@code adminService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 1.
     *    - Kiểm tra xem đối tượng {@link GoalDto} trong danh sách có khớp với đối tượng giả định hay không.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code goalRepository.findAll()} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(goal, GoalDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getAllGoals - Success")
    void getAllGoals_Success() {
        when(goalRepository.findAll()).thenReturn(Arrays.asList(goal));
        when(modelMapper.map(goal, GoalDto.class)).thenReturn(goalDto);

        List<GoalDto> result = adminService.getAllGoals();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(goalDto, result.get(0));
        verify(goalRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(goal, GoalDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getGoalById} của {@link AdminServiceImpl} trong trường hợp tìm thấy mục tiêu.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code goalRepository}: khi {@code findById(1L)} được gọi, nó sẽ trả về
     *    một {@link Optional} chứa đối tượng {@link Goal} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link Goal} thành {@link GoalDto},
     *    nó sẽ trả về đối tượng {@link GoalDto} giả định.
     * 3. Gọi phương thức {@code getGoalById} thực tế của {@code adminService} với ID 1L.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link GoalDto} trả về không null và khớp với đối tượng giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code goalRepository.findById(1L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(goal, GoalDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getGoalById - Success")
    void getGoalById_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(modelMapper.map(goal, GoalDto.class)).thenReturn(goalDto);

        GoalDto result = adminService.getGoalById(1L);

        assertNotNull(result);
        assertEquals(goalDto, result);
        verify(goalRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(goal, GoalDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getGoalById} của {@link AdminServiceImpl} trong trường hợp không tìm thấy mục tiêu.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code goalRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy mục tiêu).
     * 2. Gọi phương thức {@code getGoalById} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code goalRepository.findById(99L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map} KHÔNG BAO GIỜ được gọi, vì không có mục tiêu để ánh xạ.
     */
    @Test
    @DisplayName("Test getGoalById - Not Found")
    void getGoalById_NotFound() {
        when(goalRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getGoalById(99L));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code createGoal} của {@link AdminServiceImpl} trong trường hợp tạo mục tiêu thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link GoalDto} chứa thông tin mục tiêu mới và {@link User} ID.
     * 2. Tạo các đối tượng {@link Goal} giả định để mô phỏng dữ liệu đã lưu.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} giả định.
     *    - {@code modelMapper.map(createRequest, Goal.class)} trả về đối tượng {@link Goal} mới.
     *    - {@code goalRepository.save(any(Goal.class))} trả về đối tượng {@link Goal} đã lưu.
     *    - {@code modelMapper.map(savedGoal, GoalDto.class)} trả về đối tượng {@link GoalDto} đã lưu.
     * 4. Gọi phương thức {@code createGoal} thực tế của {@code adminService} với {@link GoalDto}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link GoalDto} trả về không null và khớp với đối tượng giả định đã lưu.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code map} và {@code save} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link Goal} được lưu trữ, đảm bảo
     *      rằng mục tiêu trọng lượng, loại mục tiêu, trạng thái và người dùng của mục tiêu đã được thiết lập chính xác.
     */
    @Test
    @DisplayName("Test createGoal - Success")
    void createGoal_Success() {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 1L, "Gain more health");
        Goal newGoal = new Goal(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", user, "Gain more health");
        Goal savedGoal = new Goal(2L, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", user, "Gain more health");
        GoalDto savedGoalDto = new GoalDto(2L, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 1L, "Gain more health");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(createRequest, Goal.class)).thenReturn(newGoal);
        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);
        when(modelMapper.map(savedGoal, GoalDto.class)).thenReturn(savedGoalDto);

        GoalDto result = adminService.createGoal(createRequest);

        assertNotNull(result);
        assertEquals(savedGoalDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(createRequest, Goal.class);
        verify(goalRepository, times(1)).save(any(Goal.class));
        verify(modelMapper, times(1)).map(savedGoal, GoalDto.class);

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(goalCaptor.capture());
        Goal capturedGoal = goalCaptor.getValue();
        assertEquals(65.0, capturedGoal.getTargetWeight());
        assertEquals(EGoalType.WEIGHT_GAIN, capturedGoal.getGoalType());
        assertEquals("ACTIVE", capturedGoal.getStatus());
        assertEquals(user, capturedGoal.getUser());
    }

    /**
     * Kiểm thử phương thức {@code createGoal} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng khi tạo mục tiêu.
     * Luồng hoạt động:
     * 1. Tạo một {@link GoalDto} với một {@link User} ID không tồn tại (99L).
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code createGoal} thực tế của {@code adminService} với {@link GoalDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code map} và {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test createGoal - User Not Found")
    void createGoal_UserNotFound() {
        GoalDto createRequest = new GoalDto(null, 65.0, LocalDate.now().plusMonths(2), EGoalType.WEIGHT_GAIN, "ACTIVE", 99L, "Gain more health");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.createGoal(createRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
        verify(goalRepository, never()).save(any(Goal.class));
    }

    /**
     * Kiểm thử phương thức {@code updateGoal} của {@link AdminServiceImpl} trong trường hợp cập nhật thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link GoalDto} chứa thông tin mục tiêu được cập nhật và {@link User} ID.
     * 2. Tạo các đối tượng {@link Goal} và {@link GoalDto} giả định sau khi cập nhật.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code goalRepository.findById(1L)} trả về đối tượng {@link Goal} ban đầu.
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} giả định.
     *    - {@code goalRepository.save(any(Goal.class))} trả về đối tượng {@link Goal} đã cập nhật.
     *    - {@code modelMapper.map(updatedGoalEntity, GoalDto.class))} trả về đối tượng {@link GoalDto} đã cập nhật.
     * 4. Gọi phương thức {@code updateGoal} thực tế của {@code adminService}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link GoalDto} trả về không null và khớp với đối tượng giả định đã cập nhật.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code save} và {@code map} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link Goal} được lưu trữ, đảm bảo
     *      rằng mục tiêu trọng lượng, loại mục tiêu, trạng thái và người dùng của mục tiêu đã được cập nhật chính xác.
     */
    @Test
    @DisplayName("Test updateGoal - Success")
    void updateGoal_Success() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");
        Goal updatedGoalEntity = new Goal(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", user, "Maintain good health");
        GoalDto updatedGoalDto = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.save(any(Goal.class))).thenReturn(updatedGoalEntity);
        when(modelMapper.map(updatedGoalEntity, GoalDto.class)).thenReturn(updatedGoalDto);

        GoalDto result = adminService.updateGoal(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedGoalDto, result);
        verify(goalRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(goalRepository, times(1)).save(any(Goal.class));
        verify(modelMapper, times(1)).map(updatedGoalEntity, GoalDto.class);

        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(goalCaptor.capture());
        Goal capturedGoal = goalCaptor.getValue();
        assertEquals(75.0, capturedGoal.getTargetWeight());
        assertEquals(EGoalType.MAINTAIN_WEIGHT, capturedGoal.getGoalType());
        assertEquals("COMPLETED", capturedGoal.getStatus());
        assertEquals(user, capturedGoal.getUser());
    }

    /**
     * Kiểm thử phương thức {@code updateGoal} của {@link AdminServiceImpl} trong trường hợp không tìm thấy mục tiêu.
     * Luồng hoạt động:
     * 1. Tạo một {@link GoalDto} chứa thông tin mục tiêu được cập nhật.
     * 2. Thiết lập hành vi giả lập cho {@code goalRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy mục tiêu).
     * 3. Gọi phương thức {@code updateGoal} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findById(user)}, {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test updateGoal - Goal Not Found")
    void updateGoal_GoalNotFound() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 1L, "Maintain good health");
        when(goalRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateGoal(99L, updateRequest));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(99L);
        verify(userRepository, never()).findById(anyLong());
        verify(goalRepository, never()).save(any(Goal.class));
    }

    /**
     * Kiểm thử phương thức {@code updateGoal} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Tạo một {@link GoalDto} với một {@link User} ID không tồn tại (99L).
     * 2. Thiết lập hành vi giả lập cho các repository:
     *    - {@code goalRepository.findById(1L)} trả về đối tượng {@link Goal} ban đầu.
     *    - {@code userRepository.findById(anyLong())} trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code updateGoal} thực tế của {@code adminService} với ID mục tiêu 1L và {@link GoalDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng phương thức {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test updateGoal - User Not Found")
    void updateGoal_UserNotFound() {
        GoalDto updateRequest = new GoalDto(1L, 75.0, LocalDate.now().plusMonths(6), EGoalType.MAINTAIN_WEIGHT, "COMPLETED", 99L, "Maintain good health");
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateGoal(1L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(99L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    /**
     * Kiểm thử phương thức {@code deleteGoal} của {@link AdminServiceImpl} trong trường hợp xóa thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code goalRepository}:
     *    - {@code existsById(1L)} trả về {@code true} (mục tiêu tồn tại).
     *    - {@code doNothing().when(goalRepository).deleteById(1L)} không làm gì cả.
     * 2. Gọi phương thức {@code deleteGoal} thực tế của {@code adminService} với ID 1L.
     * 3. Xác minh tương tác:
     *    - Đảm bảo rằng {@code goalRepository.existsById(1L)} và {@code goalRepository.deleteById(1L)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test deleteGoal - Success")
    void deleteGoal_Success() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(goalRepository).deleteById(1L);

        adminService.deleteGoal(1L);

        verify(goalRepository, times(1)).existsById(1L);
        verify(goalRepository, times(1)).deleteById(1L);
    }

    /**
     * Kiểm thử phương thức {@code deleteGoal} của {@link AdminServiceImpl} trong trường hợp không tìm thấy mục tiêu.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code goalRepository}: khi {@code existsById(anyLong())} được gọi,
     *    nó sẽ trả về {@code false} (không tìm thấy mục tiêu).
     * 2. Gọi phương thức {@code deleteGoal} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code goalRepository.deleteById} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test deleteGoal - Not Found")
    void deleteGoal_NotFound() {
        when(goalRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteGoal(99L));

        assertEquals("Goal not found with id : '99'", exception.getMessage());
        verify(goalRepository, times(1)).existsById(99L);
        verify(goalRepository, never()).deleteById(anyLong());
    }

    /**
     * Kiểm thử phương thức {@code getAllNutritionPlans} của {@link AdminServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}: khi {@code findAll()} được gọi, nó sẽ trả về
     *    một danh sách chứa đối tượng {@link NutritionPlan} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link NutritionPlan} thành {@link NutritionPlanDto},
     *    nó sẽ trả về đối tượng {@link NutritionPlanDto} giả định.
     * 3. Gọi phương thức {@code getAllNutritionPlans} thực tế của {@code adminService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 1.
     *    - Kiểm tra xem đối tượng {@link NutritionPlanDto} trong danh sách có khớp với đối tượng giả định hay không.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code nutritionPlanRepository.findAll()} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(nutritionPlan, NutritionPlanDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getAllNutritionPlans - Success")
    void getAllNutritionPlans_Success() {
        when(nutritionPlanRepository.findAll()).thenReturn(Arrays.asList(nutritionPlan));
        when(modelMapper.map(nutritionPlan, NutritionPlanDto.class)).thenReturn(nutritionPlanDto);

        List<NutritionPlanDto> result = adminService.getAllNutritionPlans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(nutritionPlanDto, result.get(0));
        verify(nutritionPlanRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(nutritionPlan, NutritionPlanDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getNutritionPlanById} của {@link AdminServiceImpl} trong trường hợp tìm thấy kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}: khi {@code findById(1L)} được gọi, nó sẽ trả về
     *    một {@link Optional} chứa đối tượng {@link NutritionPlan} giả định.
     * 2. Thiết lập hành vi giả lập cho {@code modelMapper}: khi ánh xạ đối tượng {@link NutritionPlan} thành {@link NutritionPlanDto},
     *    nó sẽ trả về đối tượng {@link NutritionPlanDto} giả định.
     * 3. Gọi phương thức {@code getNutritionPlanById} thực tế của {@code adminService} với ID 1L.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link NutritionPlanDto} trả về không null và khớp với đối tượng giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code nutritionPlanRepository.findById(1L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map(nutritionPlan, NutritionPlanDto.class)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test getNutritionPlanById - Success")
    void getNutritionPlanById_Success() {
        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(modelMapper.map(nutritionPlan, NutritionPlanDto.class)).thenReturn(nutritionPlanDto);

        NutritionPlanDto result = adminService.getNutritionPlanById(1L);

        assertNotNull(result);
        assertEquals(nutritionPlanDto, result);
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(nutritionPlan, NutritionPlanDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getNutritionPlanById} của {@link AdminServiceImpl} trong trường hợp không tìm thấy kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy kế hoạch dinh dưỡng).
     * 2. Gọi phương thức {@code getNutritionPlanById} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code nutritionPlanRepository.findById(99L)} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map} KHÔNG BAO GIỜ được gọi, vì không có kế hoạch dinh dưỡng để ánh xạ.
     */
    @Test
    @DisplayName("Test getNutritionPlanById - Not Found")
    void getNutritionPlanById_NotFound() {
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.getNutritionPlanById(99L));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code createNutritionPlan} của {@link AdminServiceImpl} trong trường hợp tạo kế hoạch dinh dưỡng thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link NutritionPlanDto} chứa thông tin kế hoạch dinh dưỡng mới và {@link User} ID.
     * 2. Tạo các đối tượng {@link NutritionPlan} giả định để mô phỏng dữ liệu đã lưu.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} giả định.
     *    - {@code modelMapper.map(createRequest, NutritionPlan.class)} trả về đối tượng {@link NutritionPlan} mới.
     *    - {@code nutritionPlanRepository.save(any(NutritionPlan.class))} trả về đối tượng {@link NutritionPlan} đã lưu.
     *    - {@code modelMapper.map(savedPlan, NutritionPlanDto.class)} trả về đối tượng {@link NutritionPlanDto} đã lưu.
     * 4. Gọi phương thức {@code createNutritionPlan} thực tế của {@code adminService} với {@link NutritionPlanDto}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link NutritionPlanDto} trả về không null và khớp với đối tượng giả định đã lưu.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code map} và {@code save} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link NutritionPlan} được lưu trữ, đảm bảo
     *      rằng tên kế hoạch và người dùng của kế hoạch dinh dưỡng đã được thiết lập chính xác.
     */
    @Test
    @DisplayName("Test createNutritionPlan - Success")
    void createNutritionPlan_Success() {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 1L, new java.util.ArrayList<>());
        NutritionPlan newPlan = new NutritionPlan(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", user, new java.util.ArrayList<>());
        NutritionPlan savedPlan = new NutritionPlan(2L, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", user, new java.util.ArrayList<>());
        NutritionPlanDto savedPlanDto = new NutritionPlanDto(2L, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 1L, new java.util.ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(createRequest, NutritionPlan.class)).thenReturn(newPlan);
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(savedPlan);
        when(modelMapper.map(savedPlan, NutritionPlanDto.class)).thenReturn(savedPlanDto);

        NutritionPlanDto result = adminService.createNutritionPlan(createRequest);

        assertNotNull(result);
        assertEquals(savedPlanDto, result);
        verify(userRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(createRequest, NutritionPlan.class);
        verify(nutritionPlanRepository, times(1)).save(any(NutritionPlan.class));
        verify(modelMapper, times(1)).map(savedPlan, NutritionPlanDto.class);

        ArgumentCaptor<NutritionPlan> planCaptor = ArgumentCaptor.forClass(NutritionPlan.class);
        verify(nutritionPlanRepository).save(planCaptor.capture());
        NutritionPlan capturedPlan = planCaptor.getValue();
        assertEquals("Maintenance Plan", capturedPlan.getPlanName());
        assertEquals(user, capturedPlan.getUser());
    }

    /**
     * Kiểm thử phương thức {@code createNutritionPlan} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng khi tạo kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Tạo một {@link NutritionPlanDto} với một {@link User} ID không tồn tại (99L).
     * 2. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code createNutritionPlan} thực tế của {@code adminService} với {@link NutritionPlanDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code map} và {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test createNutritionPlan - User Not Found")
    void createNutritionPlan_UserNotFound() {
        NutritionPlanDto createRequest = new NutritionPlanDto(null, "Maintenance Plan", LocalDate.now(), LocalDate.now().plusMonths(3), "Maintain weight", 99L, new java.util.ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.createNutritionPlan(createRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(modelMapper, never()).map(any(), any());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    /**
     * Kiểm thử phương thức {@code updateNutritionPlan} của {@link AdminServiceImpl} trong trường hợp cập nhật thành công.
     * Luồng hoạt động:
     * 1. Tạo một {@link NutritionPlanDto} chứa thông tin kế hoạch dinh dưỡng được cập nhật và {@link User} ID.
     * 2. Tạo các đối tượng {@link NutritionPlan} và {@link NutritionPlanDto} giả định sau khi cập nhật.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code nutritionPlanRepository.findById(1L)} trả về đối tượng {@link NutritionPlan} ban đầu.
     *    - {@code userRepository.findById(1L)} trả về đối tượng {@link User} giả định.
     *    - {@code nutritionPlanRepository.save(any(NutritionPlan.class))} trả về đối tượng {@link NutritionPlan} đã cập nhật.
     *    - {@code modelMapper.map(updatedPlanEntity, NutritionPlanDto.class))} trả về đối tượng {@link NutritionPlanDto} đã cập nhật.
     * 4. Gọi phương thức {@code updateNutritionPlan} thực tế của {@code adminService}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link NutritionPlanDto} trả về không null và khớp với đối tượng giả định đã cập nhật.
     * 6. Xác minh tương tác:
     *    - Đảm bảo các phương thức {@code findById}, {@code save} và {@code map} đã được gọi đúng số lần.
     *    - Sử dụng {@link ArgumentCaptor} để kiểm tra các giá trị của đối tượng {@link NutritionPlan} được lưu trữ, đảm bảo
     *      rằng tên kế hoạch, mục tiêu dinh dưỡng và người dùng của kế hoạch dinh dưỡng đã được cập nhật chính xác.
     */
    @Test
    @DisplayName("Test updateNutritionPlan - Success")
    void updateNutritionPlan_Success() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());
        NutritionPlan updatedPlanEntity = new NutritionPlan(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", user, new java.util.ArrayList<>());
        NutritionPlanDto updatedPlanDto = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());

        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(nutritionPlanRepository.save(any(NutritionPlan.class))).thenReturn(updatedPlanEntity);
        when(modelMapper.map(updatedPlanEntity, NutritionPlanDto.class)).thenReturn(updatedPlanDto);

        NutritionPlanDto result = adminService.updateNutritionPlan(1L, updateRequest);

        assertNotNull(result);
        assertEquals(updatedPlanDto, result);
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(nutritionPlanRepository, times(1)).save(any(NutritionPlan.class));
        verify(modelMapper, times(1)).map(updatedPlanEntity, NutritionPlanDto.class);

        ArgumentCaptor<NutritionPlan> planCaptor = ArgumentCaptor.forClass(NutritionPlan.class);
        verify(nutritionPlanRepository).save(planCaptor.capture());
        NutritionPlan capturedPlan = planCaptor.getValue();
        assertEquals("Bulk Up Plan", capturedPlan.getPlanName());
        assertEquals("Gain muscle", capturedPlan.getNutritionGoal());
        assertEquals(user, capturedPlan.getUser());
    }

    /**
     * Kiểm thử phương thức {@code updateNutritionPlan} của {@link AdminServiceImpl} trong trường hợp không tìm thấy kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Tạo một {@link NutritionPlanDto} chứa thông tin kế hoạch dinh dưỡng được cập nhật.
     * 2. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}: khi {@code findById(anyLong())} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy kế hoạch dinh dưỡng).
     * 3. Gọi phương thức {@code updateNutritionPlan} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findById(user)} và {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test updateNutritionPlan - NutritionPlan Not Found")
    void updateNutritionPlan_NutritionPlanNotFound() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 1L, new java.util.ArrayList<>());
        when(nutritionPlanRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateNutritionPlan(99L, updateRequest));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(99L);
        verify(userRepository, never()).findById(anyLong());
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    /**
     * Kiểm thử phương thức {@code updateNutritionPlan} của {@link AdminServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Tạo một {@link NutritionPlanDto} với một {@link User} ID không tồn tại (99L).
     * 2. Thiết lập hành vi giả lập cho các repository:
     *    - {@code nutritionPlanRepository.findById(1L)} trả về đối tượng {@link NutritionPlan} ban đầu.
     *    - {@code userRepository.findById(anyLong())} trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code updateNutritionPlan} thực tế của {@code adminService} với ID kế hoạch 1L và {@link NutritionPlanDto}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng phương thức {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test updateNutritionPlan - User Not Found")
    void updateNutritionPlan_UserNotFound() {
        NutritionPlanDto updateRequest = new NutritionPlanDto(1L, "Bulk Up Plan", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(2), "Gain muscle", 99L, new java.util.ArrayList<>());
        when(nutritionPlanRepository.findById(1L)).thenReturn(Optional.of(nutritionPlan));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateNutritionPlan(1L, updateRequest));

        assertEquals("User not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(99L);
        verify(nutritionPlanRepository, never()).save(any(NutritionPlan.class));
    }

    /**
     * Kiểm thử phương thức {@code deleteNutritionPlan} của {@link AdminServiceImpl} trong trường hợp xóa thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}:
     *    - {@code existsById(1L)} trả về {@code true} (kế hoạch dinh dưỡng tồn tại).
     *    - {@code doNothing().when(nutritionPlanRepository).deleteById(1L)} không làm gì cả.
     * 2. Gọi phương thức {@code deleteNutritionPlan} thực tế của {@code adminService} với ID 1L.
     * 3. Xác minh tương tác:
     *    - Đảm bảo rằng {@code nutritionPlanRepository.existsById(1L)} và {@code nutritionPlanRepository.deleteById(1L)} đã được gọi đúng 1 lần.
     */
    @Test
    @DisplayName("Test deleteNutritionPlan - Success")
    void deleteNutritionPlan_Success() {
        when(nutritionPlanRepository.existsById(1L)).thenReturn(true);
        doNothing().when(nutritionPlanRepository).deleteById(1L);

        adminService.deleteNutritionPlan(1L);

        verify(nutritionPlanRepository, times(1)).existsById(1L);
        verify(nutritionPlanRepository, times(1)).deleteById(1L);
    }

    /**
     * Kiểm thử phương thức {@code deleteNutritionPlan} của {@link AdminServiceImpl} trong trường hợp không tìm thấy kế hoạch dinh dưỡng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code nutritionPlanRepository}: khi {@code existsById(anyLong())} được gọi,
     *    nó sẽ trả về {@code false} (không tìm thấy kế hoạch dinh dưỡng).
     * 2. Gọi phương thức {@code deleteNutritionPlan} thực tế của {@code adminService} với ID 99L (không tồn tại).
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link ResourceNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng {@code nutritionPlanRepository.deleteById} KHÔNG BAO GIỜ được gọi.
     */
    @Test
    @DisplayName("Test deleteNutritionPlan - Not Found")
    void deleteNutritionPlan_NotFound() {
        when(nutritionPlanRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteNutritionPlan(99L));

        assertEquals("NutritionPlan not found with id : '99'", exception.getMessage());
        verify(nutritionPlanRepository, times(1)).existsById(99L);
        verify(nutritionPlanRepository, never()).deleteById(anyLong());
    }
}
