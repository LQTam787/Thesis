package com.nutrition.ai.nutritionaibackend.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double calories;

    private Double protein;

    private Double carbs;

    private Double fats;

    private String servingSize;

    @OneToOne(mappedBy = "foodItem", fetch = FetchType.LAZY)
    private Recipe recipe;

    @OneToMany(mappedBy = "foodItem")
    private List<MealFoodItem> mealFoodItems;

    @OneToMany(mappedBy = "foodItem")
    private List<FoodLog> foodLogs;
}
