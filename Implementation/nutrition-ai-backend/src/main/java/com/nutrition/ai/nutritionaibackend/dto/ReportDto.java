package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Report entity.
 * Nguyên lý hoạt động: Đóng gói dữ liệu cơ bản của một báo cáo đã được lưu trữ (thường là một báo cáo đơn giản, không cần chi tiết FoodLogs/ActivityLogs).
 * Luồng hoạt động: Được sử dụng để liệt kê các báo cáo đã tạo cho người dùng, hoặc khi server tạo và lưu trữ một báo cáo văn bản đơn giản.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id; // ID duy nhất của báo cáo.
    private EReportType reportType; // Loại báo cáo.
    private LocalDateTime generatedDate; // Ngày và thời gian tạo.
    private String content; // Nội dung văn bản thô của báo cáo.
    private Long userId; // ID của người dùng sở hữu báo cáo.
}