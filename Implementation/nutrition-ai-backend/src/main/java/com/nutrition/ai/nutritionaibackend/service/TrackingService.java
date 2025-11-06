package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;

/**
 * Service interface for handling tracking-related operations such as logging food, activities,
 * and generating progress reports.
 * Định nghĩa các API để người dùng ghi lại lượng ăn vào, hoạt động thể chất và xem báo cáo tiến độ.
 */
public interface TrackingService {

    /**
     * Luồng hoạt động: Ghi lại một món ăn đã tiêu thụ cho người dùng.
     * Nguyên lý hoạt động: Nhận FoodLogDto, chuyển đổi thành Entity, lưu vào cơ sở dữ liệu (thường liên quan đến việc tính toán lại tổng lượng calo/dinh dưỡng trong ngày).
     *
     * @param userId The ID of the user.
     * @param foodLogDto The DTO containing food log details.
     * @return The created FoodLogDto.
     */
    FoodLogDto logFood(String userId, FoodLogDto foodLogDto);

    /**
     * Luồng hoạt động: Ghi lại một hoạt động thể chất đã thực hiện cho người dùng.
     * Nguyên lý hoạt động: Nhận ActivityLogDto, chuyển đổi thành Entity, lưu vào cơ sở dữ liệu (thường liên quan đến việc tính toán lượng calo đã đốt cháy).
     *
     * @param userId The ID of the user.
     * @param activityLogDto The DTO containing activity log details.
     * @return The created ActivityLogDto.
     */
    ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto);

    /**
     * Luồng hoạt động: Tạo báo cáo tiến độ tổng hợp cho người dùng.
     * Nguyên lý hoạt động: Truy vấn dữ liệu FoodLog, ActivityLog và dữ liệu mục tiêu của người dùng trong một khoảng thời gian cụ thể (`reportType`), thực hiện tính toán và tổng hợp kết quả vào ProgressReportDto.
     *
     * @param userId The ID of the user.
     * @param reportType The type of report to generate (e.g., 'weekly', 'monthly').
     * @return A DTO containing the comprehensive progress report.
     */
    ProgressReportDto generateProgressReport(String userId, String reportType);

}