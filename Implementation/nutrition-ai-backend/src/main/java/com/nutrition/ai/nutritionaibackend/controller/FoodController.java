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
 * FoodController xử lý các yêu cầu HTTP liên quan đến việc quản lý các mục thực phẩm trong hệ thống.
 * <p>
 * Nguyên lý hoạt động: Controller này cung cấp các RESTful endpoint cho phép thực hiện các hoạt động CRUD
 * (Tạo, Đọc, Cập nhật, Xóa) trên tài nguyên {@code FoodItem}. Các hoạt động tạo, cập nhật và xóa
 * thường được giới hạn cho người dùng có vai trò quản trị viên để duy trì tính toàn vẹn của dữ liệu.
 * Controller này sử dụng {@link com.nutrition.ai.nutritionaibackend.service.FoodService}
 * để xử lý logic nghiệp vụ và tương tác với cơ sở dữ liệu, và {@link org.modelmapper.ModelMapper}
 * để chuyển đổi giữa các đối tượng DTO và entity.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP (POST, GET, PUT, DELETE) từ client tại đường dẫn cơ sở {@code /api/foods}.</li>
 *     <li>Sử dụng {@code ModelMapper} để chuyển đổi dữ liệu giữa {@link FoodItemDto} và {@link FoodItem} entity.</li>
 *     <li>Ủy quyền các yêu cầu xử lý nghiệp vụ cho {@code FoodService}.</li>
 *     <li>Trả về {@code ResponseEntity} với dữ liệu {@link FoodItemDto} hoặc thông báo trạng thái HTTP thích hợp (ví dụ: 200 OK, 204 NO CONTENT, 404 NOT FOUND).</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/foods") // Ánh xạ với đường dẫn cơ sở /api/foods
@Tag(name = "Food Management", description = "Endpoints for managing food items") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class FoodController {

    private final FoodService foodService; // Dependency cho Service
    private final ModelMapper modelMapper; // Dependency cho ModelMapper

    /**
     * Constructor để inject {@link FoodService} và {@link ModelMapper} vào FoodController.
     * <p>
     * Nguyên lý hoạt động: Spring Framework tự động tiêm các thể hiện của {@code FoodService}
     * và {@code ModelMapper} vào constructor này khi tạo bean {@code FoodController}.
     * Điều này cho phép controller truy cập các dịch vụ và tiện ích cần thiết để xử lý yêu cầu.
     * </p>
     * @param foodService Service quản lý logic nghiệp vụ liên quan đến các mục thực phẩm.
     * @param modelMapper ModelMapper để chuyển đổi giữa các DTO và entity.
     */
    public FoodController(FoodService foodService, ModelMapper modelMapper) { // Constructor injection
        this.foodService = foodService;
        this.modelMapper = modelMapper;
    }

    /**
     * Tạo một mục thực phẩm mới trong hệ thống.
     * <p>
     * Endpoint này nhận dữ liệu của một mục thực phẩm mới dưới dạng {@link FoodItemDto},
     * chuyển đổi nó thành {@link FoodItem} entity, và lưu vào cơ sở dữ liệu thông qua {@link FoodService}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/foods} với {@link FoodItemDto} trong body.</li>
     *     <li>Ánh xạ {@code foodItemDto} sang {@link FoodItem} entity.</li>
     *     <li>Gọi phương thức {@code foodService.save(foodItem)} để lưu entity.</li>
     *     <li>Ánh xạ {@link FoodItem} đã lưu trở lại {@link FoodItemDto} và trả về với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param foodItemDto DTO chứa thông tin chi tiết của mục thực phẩm cần tạo.
     * @return ResponseEntity chứa {@link FoodItemDto} của mục thực phẩm đã tạo.
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
     * Lấy thông tin chi tiết của một mục thực phẩm cụ thể dựa trên ID.
     * <p>
     * Endpoint này tìm kiếm một {@link FoodItem} bằng ID được cung cấp.
     * Nếu tìm thấy, nó sẽ trả về thông tin dưới dạng {@link FoodItemDto};
     * nếu không tìm thấy, nó sẽ trả về trạng thái 404 NOT FOUND.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/foods/{id}}.</li>
     *     <li>Trích xuất {@code id} của mục thực phẩm từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code foodService.findOne(id)}.</li>
     *     <li>Nếu Optional chứa giá trị, ánh xạ {@link FoodItem} sang {@link FoodItemDto} và trả với trạng thái HTTP 200 OK.</li>
     *     <li>Nếu Optional rỗng, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param id ID của mục thực phẩm cần lấy thông tin.
     * @return ResponseEntity chứa {@link FoodItemDto} của mục thực phẩm nếu tìm thấy, hoặc trạng thái 404 NOT FOUND.
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
     * Lấy danh sách tất cả các mục thực phẩm có sẵn trong hệ thống.
     * <p>
     * Endpoint này truy xuất tất cả các {@link FoodItem} từ cơ sở dữ liệu và chuyển đổi chúng
     * thành danh sách {@link FoodItemDto} để trả về cho client.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/foods}.</li>
     *     <li>Gọi phương thức {@code foodService.findAll()} để lấy tất cả các {@link FoodItem} entity.</li>
     *     <li>Sử dụng Stream API và {@code ModelMapper} để chuyển đổi từng {@link FoodItem} sang {@link FoodItemDto}.</li>
     *     <li>Trả về danh sách {@link FoodItemDto} với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @return ResponseEntity chứa danh sách {@link FoodItemDto} của tất cả các mục thực phẩm.
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
     * Cập nhật thông tin chi tiết của một mục thực phẩm hiện có.
     * <p>
     * Endpoint này nhận ID của mục thực phẩm cần cập nhật và dữ liệu cập nhật dưới dạng {@link FoodItemDto}.
     * Nó tìm kiếm mục thực phẩm hiện có, áp dụng các thay đổi và lưu chúng vào cơ sở dữ liệu.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu PUT đến {@code /api/foods/{id}} với {@link FoodItemDto} trong body.</li>
     *     <li>Trích xuất {@code id} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code foodService.findOne(id)} để tìm mục thực phẩm hiện có.</li>
     *     <li>Nếu tìm thấy:
     *         <ul>
     *             <li>Đặt {@code id} từ đường dẫn vào {@code foodItemDto} để đảm bảo cập nhật đúng đối tượng.</li>
     *             <li>Ánh xạ {@code foodItemDto} sang {@link FoodItem} entity.</li>
     *             <li>Gọi phương thức {@code foodService.save(updatedFoodItem)} để lưu các thay đổi.</li>
     *             <li>Ánh xạ {@link FoodItem} đã cập nhật trở lại {@link FoodItemDto} và trả với trạng thái HTTP 200 OK.</li>
     *         </ul>
     *     </li>
     *     <li>Nếu không tìm thấy, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param id ID của mục thực phẩm cần cập nhật.
     * @param foodItemDto DTO chứa dữ liệu cập nhật cho mục thực phẩm.
     * @return ResponseEntity chứa {@link FoodItemDto} của mục thực phẩm đã cập nhật nếu thành công, hoặc trạng thái 404 NOT FOUND.
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
     * Xóa một mục thực phẩm khỏi hệ thống dựa trên ID.
     * <p>
     * Endpoint này thực hiện xóa một {@link FoodItem} khỏi cơ sở dữ liệu.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu DELETE đến {@code /api/foods/{id}}.</li>
     *     <li>Trích xuất {@code id} của mục thực phẩm từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code foodService.delete(id)} để xóa mục thực phẩm.</li>
     *     <li>Trả về {@code ResponseEntity.noContent().build()} với trạng thái HTTP 204 NO CONTENT, cho biết việc xóa đã thành công mà không có nội dung phản hồi.</li>
     * </ol>
     * </p>
     * @param id ID của mục thực phẩm cần xóa.
     * @return ResponseEntity rỗng với trạng thái HTTP 204 NO CONTENT.
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