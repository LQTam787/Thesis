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

@RestController
@RequestMapping("/api/users/{username}/nutrition-plans")
@Tag(name = "Nutrition Plan Management", description = "Endpoints for managing user nutrition plans")
@SecurityRequirement(name = "bearerAuth")
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public NutritionPlanController(NutritionPlanService nutritionPlanService, UserRepository userRepository, ModelMapper modelMapper) {
        this.nutritionPlanService = nutritionPlanService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Create a nutrition plan for a user", description = "Creates a new nutrition plan associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition plan created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@Parameter(description = "Username of the user") @PathVariable String username, @RequestBody NutritionPlanDto nutritionPlanDto) {
        return userRepository.findByUsername(username).map(user -> {
            NutritionPlan nutritionPlan = modelMapper.map(nutritionPlanDto, NutritionPlan.class);
            nutritionPlan.setUser(user);
            NutritionPlan savedPlan = nutritionPlanService.save(nutritionPlan);
            return ResponseEntity.ok(modelMapper.map(savedPlan, NutritionPlanDto.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a specific nutrition plan", description = "Retrieves a single nutrition plan by its ID for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition plan found"),
            @ApiResponse(responseCode = "404", description = "User or plan not found")
    })
    @GetMapping("/{planId}")
    public ResponseEntity<NutritionPlanDto> getNutritionPlan(@Parameter(description = "Username of the user") @PathVariable String username, @Parameter(description = "ID of the nutrition plan") @PathVariable Long planId) {
        return nutritionPlanService.findOne(planId)
                .map(nutritionPlan -> ResponseEntity.ok(modelMapper.map(nutritionPlan, NutritionPlanDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all nutrition plans for a user", description = "Retrieves all nutrition plans associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans(@Parameter(description = "Username of the user") @PathVariable String username) {
        return userRepository.findByUsername(username).map(user -> {
            List<NutritionPlanDto> nutritionPlanDtos = nutritionPlanService.findAllByUser(user).stream()
                    .map(nutritionPlan -> modelMapper.map(nutritionPlan, NutritionPlanDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(nutritionPlanDtos);
        }).orElse(ResponseEntity.notFound().build());
    }
}
