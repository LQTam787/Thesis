package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@WithMockUser // Giả lập người dùng đã đăng nhập (cần thiết nếu Controller được bảo mật)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService; // Service giả lập

    @MockBean
    private ModelMapper modelMapper; // ModelMapper giả lập (dùng để chuyển đổi giữa DTO và Entity)

    @Autowired
    private ObjectMapper objectMapper;

    private FoodItem foodItem1;
    private FoodItemDto foodItemDto1;
    private FoodItem foodItem2;
    private FoodItemDto foodItemDto2;

    @BeforeEach // Chạy trước mỗi bài kiểm thử
    void setUp() {
        // Khởi tạo các đối tượng Entity và DTO giả lập để tái sử dụng
        foodItem1 = new FoodItem();
        foodItem1.setId(1L);
        foodItem1.setName("Apple");

        foodItemDto1 = new FoodItemDto(1L, "Apple", 95.0, 0.5, 25.0, 0.3, "1 medium", null);

        foodItem2 = new FoodItem();
        foodItem2.setId(2L);
        foodItem2.setName("Banana");

        foodItemDto2 = new FoodItemDto(2L, "Banana", 105.0, 1.3, 27.0, 0.3, "1 large", null);
    }

    @Test
    void testCreateFoodItem_Success() throws Exception {
        // 1. Mocking ModelMapper: Giả lập việc chuyển đổi DTO -> Entity và Entity -> DTO
        when(modelMapper.map(any(FoodItemDto.class), eq(FoodItem.class))).thenReturn(foodItem1);
        // 2. Mocking Service: Giả lập việc lưu Entity vào DB
        when(foodService.save(any(FoodItem.class))).thenReturn(foodItem1);
        // 3. Mocking ModelMapper: Giả lập việc chuyển đổi kết quả Entity -> DTO phản hồi
        when(modelMapper.map(any(FoodItem.class), eq(FoodItemDto.class))).thenReturn(foodItemDto1);

        // 4. Thực hiện yêu cầu POST /api/foods
        mockMvc.perform(post("/api/foods")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto1)))
                .andExpect(status().isOk()) // 5. Kiểm tra: Trạng thái 200 OK (hoặc 201 CREATED tùy quy ước)
                .andExpect(jsonPath("$.name").value("Apple")); // Kiểm tra nội dung phản hồi
    }

    @Test
    void testGetFoodItem_Found() throws Exception {
        // 1. Mocking Service: Giả lập tìm thấy FoodItem
        when(foodService.findOne(1L)).thenReturn(Optional.of(foodItem1));
        // 2. Mocking ModelMapper: Giả lập chuyển đổi Entity -> DTO
        when(modelMapper.map(foodItem1, FoodItemDto.class)).thenReturn(foodItemDto1);

        // 3. Thực hiện yêu cầu GET /api/foods/1
        mockMvc.perform(get("/api/foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFoodItem_NotFound() throws Exception {
        // 1. Mocking Service: Giả lập không tìm thấy FoodItem (trả về Optional.empty())
        when(foodService.findOne(99L)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET /api/foods/99
        mockMvc.perform(get("/api/foods/99"))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    @Test
    void testGetAllFoodItems_Success() throws Exception {
        List<FoodItem> foodItems = Arrays.asList(foodItem1, foodItem2);
        // 1. Mocking Service: Giả lập trả về danh sách Entity
        when(foodService.findAll()).thenReturn(foodItems);
        // 2. Mocking ModelMapper: Giả lập chuyển đổi từng Entity -> DTO
        when(modelMapper.map(foodItem1, FoodItemDto.class)).thenReturn(foodItemDto1);
        when(modelMapper.map(foodItem2, FoodItemDto.class)).thenReturn(foodItemDto2);

        // 3. Thực hiện yêu cầu GET /api/foods
        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    // Các bài kiểm thử Update/Delete tuân theo nguyên lý tương tự:
    // Update: Tìm kiếm -> Chuyển đổi DTO -> Lưu (Save) -> Chuyển đổi Entity -> Kiểm tra OK/NOT FOUND
    // Delete: Giả lập `doNothing()` trên Service -> Kiểm tra `isNoContent()`
    // ...
}