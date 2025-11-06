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
 * NutritionPlanController xử lý các yêu cầu liên quan đến quản lý các kế hoạch dinh dưỡng của người dùng.
 * Nguyên lý hoạt động: Các endpoint được lồng dưới '/api/users/{username}/nutrition-plans' để đảm bảo rằng
 * các kế hoạch dinh dưỡng luôn được liên kết với một người dùng cụ thể.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/users/{username}/nutrition-plans") // Đường dẫn cơ sở lồng nhau
@Tag(name = "Nutrition Plan Management", description = "Endpoints for managing user nutrition plans") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService; // Dependency cho Service
    private final UserRepository userRepository; // Dependency cho Repository để tìm User
    private final ModelMapper modelMapper; // Dependency cho ModelMapper

    public NutritionPlanController(NutritionPlanService nutritionPlanService, UserRepository userRepository, ModelMapper modelMapper) { // Constructor injection
        this.nutritionPlanService = nutritionPlanService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Tạo một kế hoạch dinh dưỡng mới cho một người dùng.
     * Luồng hoạt động:
     * 1. Tìm User bằng 'username' từ path variable.
     * 2. Nếu User tồn tại:
     * - Ánh xạ NutritionPlanDto thành NutritionPlan entity.
     * - Thiết lập mối quan hệ: Gán đối tượng User tìm thấy vào NutritionPlan.
     * - Lưu NutritionPlan bằng Service.
     * - Ánh xạ NutritionPlan đã lưu thành NutritionPlanDto và trả về HTTP status 200 (OK).
     * 3. Nếu User không tồn tại: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param username Tên người dùng.
     * @param nutritionPlanDto DTO chứa dữ liệu kế hoạch.
     * @return ResponseEntity chứa NutritionPlanDto hoặc status 404.
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
     * Lấy một kế hoạch dinh dưỡng cụ thể theo ID.
     * Luồng hoạt động:
     * 1. Nhận 'planId' từ path variable. (Endpoint này không sử dụng 'username' cho logic tìm kiếm, nhưng có thể sử dụng để kiểm tra quyền sở hữu.)
     * 2. Gọi nutritionPlanService.findOne(planId) để tìm Kế hoạch.
     * 3. Nếu tìm thấy: Ánh xạ thành DTO và trả về HTTP status 200 (OK).
     * 4. Nếu không tìm thấy: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param username Tên người dùng (dùng trong đường dẫn URL).
     * @param planId ID của kế hoạch.
     * @return ResponseEntity chứa NutritionPlanDto hoặc status 404.
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
     * Lấy tất cả kế hoạch dinh dưỡng cho một người dùng.
     * Luồng hoạt động:
     * 1. Tìm User bằng 'username' từ path variable.
     * 2. Nếu User tồn tại:
     * - Gọi nutritionPlanService.findAllByUser() để lấy danh sách các kế hoạch của User đó.
     * - Sử dụng **Stream API** để ánh xạ từng NutritionPlan entity thành NutritionPlanDto.
     * - Trả về danh sách DTO với HTTP status 200 (OK).
     * 3. Nếu User không tồn tại: Trả về HTTP status 404 (NOT_FOUND).
     *
     * @param username Tên người dùng.
     * @return ResponseEntity chứa danh sách NutritionPlanDto hoặc status 404.
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