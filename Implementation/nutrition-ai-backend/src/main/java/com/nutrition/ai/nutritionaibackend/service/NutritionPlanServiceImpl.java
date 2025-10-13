package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link NutritionPlan}.
 */
@Service
public class NutritionPlanServiceImpl implements NutritionPlanService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserRepository userRepository;

    public NutritionPlanServiceImpl(NutritionPlanRepository nutritionPlanRepository, UserRepository userRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NutritionPlan save(NutritionPlan nutritionPlan) {
        // Ensure the user associated with the nutrition plan exists
        User user = userRepository.findById(nutritionPlan.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + nutritionPlan.getUser().getId()));
        nutritionPlan.setUser(user); // Re-associate managed user entity
        return nutritionPlanRepository.save(nutritionPlan);
    }

    @Override
    public List<NutritionPlan> findAll() {
        return nutritionPlanRepository.findAll();
    }

    @Override
    public Optional<NutritionPlan> findOne(Long id) {
        return nutritionPlanRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        nutritionPlanRepository.deleteById(id);
    }

    @Override
    public List<NutritionPlan> findAllByUser(User user) {
        return nutritionPlanRepository.findByUser(user);
    }
}
