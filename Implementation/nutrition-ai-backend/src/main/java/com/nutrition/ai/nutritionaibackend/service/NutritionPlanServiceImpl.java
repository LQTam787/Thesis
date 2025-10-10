package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.model.domain.NutritionPlan;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.NutritionPlanRepository;
import com.nutrition.ai.nutritionaibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return nutritionPlanRepository.findAll().stream()
            .filter(plan -> plan.getUser().equals(user))
            .collect(Collectors.toList());
    }
}
