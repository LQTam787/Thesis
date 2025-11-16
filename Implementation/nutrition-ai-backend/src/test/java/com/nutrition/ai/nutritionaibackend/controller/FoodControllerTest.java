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

/**
 * Lớp kiểm thử `FoodControllerTest` chịu trách nhiệm kiểm thử các điểm cuối API liên quan đến quản lý các mục thực phẩm
 * trong `FoodController`.
 * Nó sử dụng `@WebMvcTest` để tải ngữ cảnh ứng dụng Spring MVC tối thiểu, chỉ tập trung vào `FoodController`.
 * `@WithMockUser` giả lập một người dùng đã đăng nhập, cho phép kiểm thử các điểm cuối được bảo mật.
 * `MockMvc` được sử dụng để mô phỏng các yêu cầu HTTP và `Mockito` để giả lập (mock) `FoodService` và `ModelMapper`,
 * cho phép kiểm soát chặt chẽ hành vi của các phụ thuộc và cô lập logic của controller để kiểm thử.
 */
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

    /**
     * Phương thức thiết lập ban đầu, chạy trước mỗi bài kiểm thử.
     * Khởi tạo các đối tượng `FoodItem` và `FoodItemDto` giả lập để tái sử dụng trong các bài kiểm thử.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi tạo một mục thực phẩm mới.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `ModelMapper` để chuyển đổi từ `FoodItemDto` sang `FoodItem` và ngược lại.
     * 2. Giả lập `foodService.save()` để trả về `FoodItem` đã lưu.
     * 3. Thực hiện yêu cầu POST đến `/api/foods` với `FoodItemDto` dưới dạng JSON và token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `name` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi tìm thấy một mục thực phẩm theo ID.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `foodService.findOne()` để trả về `Optional.of(foodItem1)`.
     * 2. Giả lập `ModelMapper` để chuyển đổi từ `FoodItem` sang `FoodItemDto`.
     * 3. Thực hiện yêu cầu GET đến `/api/foods/{id}`.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `name` trong JSON phản hồi khớp với dữ liệu giả lập.
     */
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

    /**
     * Kiểm thử kịch bản không tìm thấy mục thực phẩm theo ID.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `foodService.findOne()` để trả về `Optional.empty()`.
     * 2. Thực hiện yêu cầu GET đến `/api/foods/{id}` với một ID không tồn tại.
     * 3. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     */
    @Test
    void testGetFoodItem_NotFound() throws Exception {
        // 1. Mocking Service: Giả lập không tìm thấy FoodItem (trả về Optional.empty())
        when(foodService.findOne(99L)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu GET /api/foods/99
        mockMvc.perform(get("/api/foods/99"))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
    }

    /**
     * Kiểm thử kịch bản thành công khi lấy tất cả các mục thực phẩm.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị một danh sách các `FoodItem` giả lập.
     * 2. Giả lập `foodService.findAll()` để trả về danh sách `FoodItem` giả lập.
     * 3. Giả lập `ModelMapper` để chuyển đổi từng `FoodItem` trong danh sách sang `FoodItemDto`.
     * 4. Thực hiện yêu cầu GET đến `/api/foods`.
     * 5. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và nội dung JSON chứa tên của các mục thực phẩm mong muốn.
     */
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

    /**
     * Kiểm thử kịch bản thành công khi cập nhật một mục thực phẩm hiện có.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị `FoodItemDto` với dữ liệu cập nhật và các đối tượng `FoodItem` giả lập tương ứng.
     * 2. Giả lập `foodService.findOne()` để trả về `FoodItem` hiện có.
     * 3. Giả lập `ModelMapper` để chuyển đổi DTO cập nhật sang Entity và Entity đã lưu sang DTO phản hồi.
     * 4. Giả lập `foodService.save()` để trả về `FoodItem` đã cập nhật.
     * 5. Thực hiện yêu cầu PUT đến `/api/foods/{id}` với dữ liệu cập nhật và token CSRF.
     * 6. Xác minh rằng phản hồi có trạng thái HTTP 200 OK và trường `name` trong JSON phản hồi đã được cập nhật.
     * 7. Xác minh rằng `foodService.findOne()` và `foodService.save()` đã được gọi chính xác một lần.
     */
    @Test
    void testUpdateFoodItem_Success() throws Exception {
        // Chuẩn bị DTO với dữ liệu cập nhật
        FoodItemDto updatedFoodItemDto = new FoodItemDto(1L, "Updated Apple", 100.0, 0.6, 26.0, 0.4, "1 large", null);
        FoodItem updatedFoodItem = new FoodItem();
        updatedFoodItem.setId(1L);
        updatedFoodItem.setName("Updated Apple");

        // 1. Mocking Service: Giả lập tìm thấy FoodItem hiện có
        when(foodService.findOne(1L)).thenReturn(Optional.of(foodItem1));
        // 2. Mocking ModelMapper: Giả lập việc chuyển đổi DTO -> Entity cho FoodItem đã cập nhật
        when(modelMapper.map(any(FoodItemDto.class), eq(FoodItem.class))).thenReturn(updatedFoodItem);
        // 3. Mocking Service: Giả lập việc lưu FoodItem đã cập nhật
        when(foodService.save(any(FoodItem.class))).thenReturn(updatedFoodItem);
        // 4. Mocking ModelMapper: Giả lập việc chuyển đổi kết quả Entity -> DTO phản hồi
        when(modelMapper.map(any(FoodItem.class), eq(FoodItemDto.class))).thenReturn(updatedFoodItemDto);

        // 5. Thực hiện yêu cầu PUT /api/foods/1
        mockMvc.perform(put("/api/foods/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFoodItemDto)))
                .andExpect(status().isOk()) // 6. Kiểm tra: Trạng thái 200 OK
                .andExpect(jsonPath("$.name").value("Updated Apple")); // Kiểm tra nội dung phản hồi
        
        verify(foodService, times(1)).findOne(1L); // Xác minh rằng findOne được gọi
        verify(foodService, times(1)).save(any(FoodItem.class)); // Xác minh rằng save được gọi
    }

    /**
     * Kiểm thử kịch bản không tìm thấy mục thực phẩm khi cập nhật.
     * <p>
     * Luồng hoạt động:
     * 1. Chuẩn bị `FoodItemDto` với dữ liệu cập nhật và một ID không tồn tại.
     * 2. Giả lập `foodService.findOne()` để trả về `Optional.empty()`.
     * 3. Thực hiện yêu cầu PUT đến `/api/foods/{id}` với dữ liệu cập nhật và token CSRF.
     * 4. Xác minh rằng phản hồi có trạng thái HTTP 404 NOT FOUND.
     * 5. Xác minh rằng `foodService.findOne()` được gọi nhưng `foodService.save()` không được gọi.
     */
    @Test
    void testUpdateFoodItem_NotFound() throws Exception {
        // Chuẩn bị DTO với dữ liệu cập nhật
        FoodItemDto updatedFoodItemDto = new FoodItemDto(99L, "NonExistent Food", 100.0, 0.6, 26.0, 0.4, "1 large", null);

        // 1. Mocking Service: Giả lập không tìm thấy FoodItem
        when(foodService.findOne(99L)).thenReturn(Optional.empty());

        // 2. Thực hiện yêu cầu PUT /api/foods/99
        mockMvc.perform(put("/api/foods/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFoodItemDto)))
                .andExpect(status().isNotFound()); // 3. Kiểm tra: Trạng thái 404 NOT FOUND
        
        verify(foodService, times(1)).findOne(99L); // Xác minh rằng findOne được gọi
        verify(foodService, never()).save(any(FoodItem.class)); // Xác minh rằng save không được gọi
    }

    /**
     * Kiểm thử kịch bản thành công khi xóa một mục thực phẩm.
     * <p>
     * Luồng hoạt động:
     * 1. Giả lập `foodService.delete()` để không làm gì (doNothing) khi được gọi.
     * 2. Thực hiện yêu cầu DELETE đến `/api/foods/{id}` với token CSRF.
     * 3. Xác minh rằng phản hồi có trạng thái HTTP 204 No Content.
     * 4. Xác minh rằng `foodService.delete()` đã được gọi chính xác một lần.
     */
    @Test
    void testDeleteFoodItem_Success() throws Exception {
        // 1. Mocking Service: Giả lập hành vi xóa
        doNothing().when(foodService).delete(1L);

        // 2. Thực hiện yêu cầu DELETE /api/foods/1
        mockMvc.perform(delete("/api/foods/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // 3. Kiểm tra: Trạng thái 204 No Content
        
        verify(foodService, times(1)).delete(1L); // Xác minh rằng delete được gọi
    }

    // Các bài kiểm thử Update/Delete tuân theo nguyên lý tương tự:
    // Update: Tìm kiếm -> Chuyển đổi DTO -> Lưu (Save) -> Chuyển đổi Entity -> Kiểm tra OK/NOT FOUND
    // Delete: Giả lập `doNothing()` trên Service -> Kiểm tra `isNoContent()`
    // ...
}