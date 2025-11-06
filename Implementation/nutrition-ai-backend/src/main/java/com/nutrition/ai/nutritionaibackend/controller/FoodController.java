package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FoodController xử lý các yêu cầu liên quan đến quản lý các mục thực phẩm (ví dụ: cơ sở dữ liệu thành phần).
 * Nguyên lý hoạt động: Cung cấp các REST endpoint CRUD cơ bản cho FoodItem. Các hoạt động tạo, cập nhật, xóa
 * thường được giới hạn cho vai trò Admin (như được ghi chú trong Swagger).
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/foods") // Ánh xạ với đường dẫn cơ sở /api/foods
@Tag(name = "Food Management", description = "Endpoints for managing food items") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class FoodController {

    private final FoodService foodService; // Dependency cho Service
    private final ModelMapper modelMapper; // Dependency cho ModelMapper

    public FoodController(FoodService foodService, ModelMapper modelMapper) { // Constructor injection
        this.foodService = foodService;
        this.modelMapper = modelMapper;
    }

    /**
     * Tạo một mục thực phẩm mới.
     * Luồng hoạt động:
     * 1. Nhận FoodItemDto từ body của request.
     * 2. Ánh xạ DTO thành FoodItem entity.
     * 3. Gọi foodService.save() để lưu FoodItem.
     * 4. Ánh xạ FoodItem đã lưu thành FoodItemDto và trả về HTTP status 200 (OK).
     *
     * @param foodItemDto DTO chứa dữ liệu mục thực phẩm.
     * @return ResponseEntity chứa FoodItemDto đã tạo.
     */
    @Operation(summary = "Create a new food item", description = "Adds a new food item to the database. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping // Ánh xạ với POST /api/foods
    public ResponseEntity<FoodItemDto> createFoodItem(@RequestBody FoodItemDto foodItemDto) {
        FoodItem foodItem = modelMapper.map(foodItemDto, FoodItem.class);
        FoodItem savedFoodItem = foodService.save(foodItem);
        return ResponseEntity.ok(modelMapper.map(savedFoodItem, FoodItemDto.class));
    }

    /**
     * Lấy một mục thực phẩm theo ID.
     * Luồng hoạt động:
     * 1. Nhận 'id' từ path variable.
     * 2. Gọi foodService.findOne(id).
     * 3. Nếu tìm thấy: Ánh xạ thành DTO và trả về HTTP status 200 (OK).
     * 4. Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param id ID của mục thực phẩm.
     * @return ResponseEntity chứa FoodItemDto hoặc status 404.
     */
    @Operation(summary = "Get a food item by ID", description = "Retrieves details of a specific food item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item found"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @GetMapping("/{id}") // Ánh xạ với GET /api/foods/{id}
    public ResponseEntity<FoodItemDto> getFoodItem(@PathVariable Long id) {
        return foodService.findOne(id)
                .map(foodItem -> ResponseEntity.ok(modelMapper.map(foodItem, FoodItemDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy tất cả các mục thực phẩm.
     * Luồng hoạt động:
     * 1. Gọi foodService.findAll() để lấy tất cả FoodItem entities.
     * 2. Sử dụng Stream API để ánh xạ từng entity thành FoodItemDto.
     * 3. Trả về danh sách DTO với HTTP status 200 (OK).
     *
     * @return ResponseEntity chứa danh sách FoodItemDto.
     */
    @Operation(summary = "Get all food items", description = "Retrieves a list of all available food items.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping // Ánh xạ với GET /api/foods
    public ResponseEntity<List<FoodItemDto>> getAllFoodItems() {
        List<FoodItemDto> foodItemDtos = foodService.findAll().stream()
                .map(foodItem -> modelMapper.map(foodItem, FoodItemDto.class)) // Ánh xạ qua DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemDtos);
    }

    /**
     * Cập nhật một mục thực phẩm hiện có.
     * Luồng hoạt động:
     * 1. Tìm FoodItem hiện có bằng 'id' từ path variable.
     * 2. Nếu tìm thấy:
     * - Đảm bảo ID được đặt trong DTO để ModelMapper cập nhật đúng đối tượng.
     * - Ánh xạ DTO (với ID) thành FoodItem entity.
     * - Gọi foodService.save() để cập nhật FoodItem.
     * - Ánh xạ FoodItem đã cập nhật thành FoodItemDto và trả về HTTP status 200 (OK).
     * 3. Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param id ID của mục thực phẩm.
     * @param foodItemDto DTO chứa dữ liệu cập nhật.
     * @return ResponseEntity chứa FoodItemDto đã cập nhật hoặc status 404.
     */
    @Operation(summary = "Update a food item", description = "Updates the details of an existing food item. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @PutMapping("/{id}") // Ánh xạ với PUT /api/foods/{id}
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @RequestBody FoodItemDto foodItemDto) {
        return foodService.findOne(id) // Tìm mục hiện có
                .map(existingFoodItem -> {
                    foodItemDto.setId(id); // Đặt ID từ URL vào DTO
                    FoodItem updatedFoodItem = foodService.save(modelMapper.map(foodItemDto, FoodItem.class)); // Lưu cập nhật
                    return ResponseEntity.ok(modelMapper.map(updatedFoodItem, FoodItemDto.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Xóa một mục thực phẩm.
     * Luồng hoạt động:
     * 1. Nhận 'id' từ path variable.
     * 2. Gọi foodService.delete(id) để xóa mục thực phẩm (hầu hết các Service/Repository sẽ xử lý việc kiểm tra xem có tồn tại hay không).
     * 3. Trả về HTTP status 204 (NO_CONTENT) để biểu thị việc xóa thành công mà không có body trả về.
     *
     * @param id ID của mục thực phẩm.
     * @return ResponseEntity rỗng với status 204.
     */
    @Operation(summary = "Delete a food item", description = "Deletes a food item from the database. Typically an admin-only function.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Food item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @DeleteMapping("/{id}") // Ánh xạ với DELETE /api/foods/{id}
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodService.delete(id); // Xóa
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
}