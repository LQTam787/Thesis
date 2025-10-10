package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Report entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private EReportType reportType;
    private LocalDateTime generatedDate;
    private String content;
    private Long userId;
}
