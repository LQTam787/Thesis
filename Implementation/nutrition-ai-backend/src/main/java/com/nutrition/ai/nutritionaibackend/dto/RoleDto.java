package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Role entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Integer id;
    private ERole name;
}
