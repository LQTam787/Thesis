package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The EReportType enum defines the types of reports that can be generated for a user.
 *
 * Nguyên lý hoạt động:
 * Enum này được sử dụng để xác định loại báo cáo cần được tạo hoặc truy vấn.
 * Nó giúp module tạo báo cáo biết được loại dữ liệu nào cần tổng hợp và định dạng.
 */
public enum EReportType {
    /**
     * Báo cáo tiến độ hàng tuần của người dùng.
     * Vai trò: Kích hoạt việc tổng hợp dữ liệu (cân nặng, calo tiêu thụ, hoạt động) trong 7 ngày gần nhất.
     */
    WEEKLY_PROGRESS,
    /**
     * Báo cáo tiến độ hàng tháng của người dùng.
     * Vai trò: Kích hoạt việc tổng hợp dữ liệu (cân nặng, calo tiêu thụ, hoạt động) trong 30 ngày gần nhất.
     */
    MONTHLY_PROGRESS,
    /**
     * Báo cáo tóm tắt chi tiết về dinh dưỡng.
     * Vai trò: Kích hoạt việc tổng hợp chi tiết về lượng macro và vi chất dinh dưỡng trong một khoảng thời gian.
     */
    NUTRITION_SUMMARY
}