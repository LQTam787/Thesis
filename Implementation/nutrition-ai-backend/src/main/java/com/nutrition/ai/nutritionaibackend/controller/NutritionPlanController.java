package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.NutritionPlanDto;
import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import com.nutrition.ai.nutritionaibackend.service.NutritionPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{username}/nutrition-plans")
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public NutritionPlanController(NutritionPlanService nutritionPlanService, UserRepository userRepository, ModelMapper modelMapper) {
        this.nutritionPlanService = nutritionPlanService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<NutritionPlanDto> createNutritionPlan(@PathVariable String username, @RequestBody NutritionPlanDto nutritionPlanDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        NutritionPlan nutritionPlan = modelMapper.map(nutritionPlanDto, NutritionPlan.class);
        nutritionPlan.setUser(user);
        NutritionPlan savedPlan = nutritionPlanService.save(nutritionPlan);
        return ResponseEntity.ok(modelMapper.map(savedPlan, NutritionPlanDto.class));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<NutritionPlanDto> getNutritionPlan(@PathVariable String username, @PathVariable Long planId) {
        return nutritionPlanService.findOne(planId)
                .map(nutritionPlan -> ResponseEntity.ok(modelMapper.map(nutritionPlan, NutritionPlanDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NutritionPlanDto>> getAllNutritionPlans(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        List<NutritionPlanDto> nutritionPlanDtos = nutritionPlanService.findAllByUser(user).stream()
                .map(nutritionPlan -> modelMapper.map(nutritionPlan, NutritionPlanDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(nutritionPlanDtos);
    }
}
