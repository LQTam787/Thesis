package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Profile entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private EGender gender;
    private Double height;
    private Double weight;
    private String activityLevel;
    private String allergies;
    private String medicalConditions;
    private Long userId;
}
