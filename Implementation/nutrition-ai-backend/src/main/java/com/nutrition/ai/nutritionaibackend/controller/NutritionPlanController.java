package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.NutritionPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NutritionPlanController xử lý các yêu cầu HTTP liên quan đến việc quản lý các kế hoạch dinh dưỡng của một người dùng cụ thể.
 * <p>
 * Nguyên lý hoạt động: Các endpoint trong controller này được lồng dưới đường dẫn {@code /api/users/{username}/nutrition-plans}
 * để đảm bảo rằng mọi hoạt động trên kế hoạch dinh dưỡng đều được liên kết trực tiếp với một người dùng cụ thể.
 * Controller này sử dụng {@link com.nutrition.ai.nutritionaibackend.service.NutritionPlanService}
 * để xử lý logic nghiệp vụ liên quan đến kế hoạch dinh dưỡng, {@link com.nutrition.ai.nutritionaibackend.repository.UserRepository}
 * để truy xuất thông tin người dùng, và {@link org.modelmapper.ModelMapper} để chuyển đổi giữa các đối tượng DTO và entity.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP (POST, GET) từ client tại đường dẫn cơ sở được lồng.</li>
 *     <li>Trích xuất {@code username} từ đường dẫn để xác định người dùng.</li>
 *     <li>Sử dụng {@code UserRepository} để tìm người dùng. Nếu không tìm thấy người dùng, trả về 404 NOT FOUND.</li>
 *     <li>Nếu tìm thấy người dùng, ủy quyền yêu cầu xử lý nghiệp vụ cho {@code NutritionPlanService}.</li>
 *     <li>Sử dụng {@code ModelMapper} để chuyển đổi dữ liệu giữa {@link NutritionPlanDto} và {@link NutritionPlan} entity.</li>
 *     <li>Trả về {@code ResponseEntity} với dữ liệu {@link NutritionPlanDto} hoặc danh sách các DTO, hoặc thông báo trạng thái HTTP thích hợp.</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/users/{username}/nutrition-plans") // Đường dẫn cơ sở lồng nhau
@Tag(name = "Nutrition Plan Management", description = "Endpoints for managing user nutrition plans") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService; // Dependency cho Service
    private final UserRepository userRepository; // Dependency cho Repository để tìm User
    private final ModelMapper modelMapper; // Dependency cho ModelMapper

    /**
     * Constructor để inject {@link NutritionPlanService}, {@link UserRepository}, và {@link ModelMapper} vào NutritionPlanController.
     * <p>
     * Nguyên lý hoạt động: Spring Framework tự động tiêm các thể hiện của các service và repository cần thiết
     * vào constructor này khi tạo bean {@code NutritionPlanController}. Điều này thiết lập các phụ thuộc
     * để controller có thể thực hiện các thao tác quản lý kế hoạch dinh dưỡng.
     * </p>
     * @param nutritionPlanService Service quản lý logic nghiệp vụ cho kế hoạch dinh dưỡng.
     * @param userRepository Repository để truy cập dữ liệu người dùng.
     * @param modelMapper ModelMapper để chuyển đổi giữa các DTO và entity.
     */
    public NutritionPlanController(NutritionPlanService nutritionPlanService, UserRepository userRepository, ModelMapper modelMapper) { // Constructor injection
        this.nutritionPlanService = nutritionPlanService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Tạo một kế hoạch dinh dưỡng mới cho một người dùng cụ thể.
     * <p>
     * Endpoint này cho phép tạo một {@link NutritionPlan} mới, liên kết với một người dùng được chỉ định bởi {@code username}.
     * Dữ liệu kế hoạch được cung cấp dưới dạng {@link NutritionPlanDto}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/users/{username}/nutrition-plans} với {@link NutritionPlanDto} trong body.</li>
     *     <li>Trích xuất {@code username} từ đường dẫn và tìm kiếm người dùng bằng {@code userRepository.findByUsername(username)}.</li>
     *     <li>Nếu tìm thấy người dùng:
     *         <ul>
     *             <li>Ánh xạ {@code nutritionPlanDto} sang {@link NutritionPlan} entity.</li>
     *             <li>Thiết lập mối quan hệ bằng cách gán đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.User} tìm thấy vào {@code nutritionPlan}.</li>
     *             <li>Gọi phương thức {@code nutritionPlanService.save(nutritionPlan)} để lưu kế hoạch.</li>
     *             <li>Ánh xạ {@link NutritionPlan} đã lưu trở lại {@link NutritionPlanDto} và trả về với trạng thái HTTP 200 OK.</li>
     *         </ul>
     *     </li>
     *     <li>Nếu không tìm thấy người dùng, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param username Tên người dùng mà kế hoạch dinh dưỡng sẽ được tạo cho.
     * @param nutritionPlanDto DTO chứa thông tin chi tiết của kế hoạch dinh dưỡng cần tạo.
     * @return ResponseEntity chứa {@link NutritionPlanDto} của kế hoạch đã tạo nếu thành công, hoặc trạng thái 404 NOT FOUND nếu người dùng không tồn tại.
     */
    @Operation(summary = "Create a nutrition plan for a user", description = "Creates a new nutrition plan associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition plan created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping // Ánh xạ với POST /api/users/{username}/nutrition-plans
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@Parameter(description = "Username of the user") @PathVariable String username, @RequestBody NutritionPlanDto nutritionPlanDto) {
        return userRepository.findByUsername(username).map(user -> { // Tìm người dùng
            NutritionPlan nutritionPlan = modelMapper.map(nutritionPlanDto, NutritionPlan.class);
            nutritionPlan.setUser(user); // Gán người dùng
            NutritionPlan savedPlan = nutritionPlanService.save(nutritionPlan); // Lưu
            return ResponseEntity.ok(modelMapper.map(savedPlan, NutritionPlanDto.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy thông tin chi tiết của một kế hoạch dinh dưỡng cụ thể theo ID cho một người dùng.
     * <p>
     * Endpoint này truy xuất một {@link NutritionPlan} bằng {@code planId}. Mặc dù {@code username}
     * có trong đường dẫn URL, nó không được sử dụng trực tiếp để tìm kiếm kế hoạch nhưng ngụ ý rằng
     * kế hoạch thuộc về người dùng đó (kiểm tra quyền sở hữu có thể được thực hiện ở tầng service).
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/users/{username}/nutrition-plans/{planId}}.</li>
     *     <li>Trích xuất {@code planId} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code nutritionPlanService.findOne(planId)}.</li>
     *     <li>Nếu Optional chứa giá trị, ánh xạ {@link NutritionPlan} sang {@link NutritionPlanDto} và trả về với trạng thái HTTP 200 OK.</li>
     *     <li>Nếu Optional rỗng, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param username Tên người dùng liên quan (dùng trong đường dẫn URL).
     * @param planId ID của kế hoạch dinh dưỡng cần lấy thông tin.
     * @return ResponseEntity chứa {@link NutritionPlanDto} của kế hoạch nếu tìm thấy, hoặc trạng thái 404 NOT FOUND.
     */
    @Operation(summary = "Get a specific nutrition plan", description = "Retrieves a single nutrition plan by its ID for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition plan found"),
            @ApiResponse(responseCode = "404", description = "User or plan not found")
    })
    @GetMapping("/{planId}") // Ánh xạ với GET /api/users/{username}/nutrition-plans/{planId}
    public ResponseEntity<NutritionPlanDto> getNutritionPlan(@Parameter(description = "Username of the user") @PathVariable String username, @Parameter(description = "ID of the nutrition plan") @PathVariable Long planId) {
        return nutritionPlanService.findOne(planId)
                .map(nutritionPlan -> ResponseEntity.ok(modelMapper.map(nutritionPlan, NutritionPlanDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách tất cả các kế hoạch dinh dưỡng cho một người dùng cụ thể.
     * <p>
     * Endpoint này truy xuất tất cả các {@link NutritionPlan} được liên kết với một người dùng được chỉ định bởi {@code username}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/users/{username}/nutrition-plans}.</li>
     *     <li>Trích xuất {@code username} từ đường dẫn và tìm kiếm người dùng bằng {@code userRepository.findByUsername(username)}.</li>
     *     <li>Nếu tìm thấy người dùng:
     *         <ul>
     *             <li>Gọi phương thức {@code nutritionPlanService.findAllByUser(user)} để lấy tất cả các kế hoạch dinh dưỡng của người dùng đó.</li>
     *             <li>Sử dụng Stream API và {@code ModelMapper} để chuyển đổi từng {@link NutritionPlan} sang {@link NutritionPlanDto}.</li>
     *             <li>Trả về danh sách {@link NutritionPlanDto} với trạng thái HTTP 200 OK.</li>
     *         </ul>
     *     </li>
     *     <li>Nếu không tìm thấy người dùng, trả về {@code ResponseEntity.notFound().build()} với trạng thái HTTP 404 NOT FOUND.</li>
     * </ol>
     * </p>
     * @param username Tên người dùng cần lấy tất cả kế hoạch dinh dưỡng.
     * @return ResponseEntity chứa danh sách {@link NutritionPlanDto} của người dùng nếu thành công, hoặc trạng thái 404 NOT FOUND nếu người dùng không tồn tại.
     */
    @Operation(summary = "Get all nutrition plans for a user", description = "Retrieves all nutrition plans associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping // Ánh xạ với GET /api/users/{username}/nutrition-plans
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans(@Parameter(description = "Username of the user") @PathVariable String username) {
        return userRepository.findByUsername(username).map(user -> { // Tìm người dùng
            List<NutritionPlanDto> nutritionPlanDtos = nutritionPlanService.findAllByUser(user).stream()
                    .map(nutritionPlan -> modelMapper.map(nutritionPlan, NutritionPlanDto.class)) // Ánh xạ qua DTO
                    .collect(Collectors.toList());
            return ResponseEntity.ok(nutritionPlanDtos);
        }).orElse(ResponseEntity.notFound().build());
    }
}