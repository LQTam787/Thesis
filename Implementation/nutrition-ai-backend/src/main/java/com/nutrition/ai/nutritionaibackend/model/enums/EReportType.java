package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `EReportType` định nghĩa các loại báo cáo khác nhau có thể được tạo ra cho người dùng trong ứng dụng.
 * Việc sử dụng enum này đảm bảo tính nhất quán trong việc xác định và tạo các loại báo cáo khác nhau, 
 * phục vụ cho việc theo dõi tiến độ và cung cấp thông tin chi tiết cho người dùng.
 *
 * Logic hoạt động:
 * - Định nghĩa các loại báo cáo cố định:
 *   - Enum này cung cấp một tập hợp các giá trị cố định (`WEEKLY_PROGRESS`, `MONTHLY_PROGRESS`, `NUTRITION_SUMMARY`) 
 *     để phân loại các báo cáo.
 *   - Điều này giúp hệ thống và người dùng dễ dàng nhận biết mục đích và nội dung của từng báo cáo.
 * - Vai trò trong việc tạo báo cáo:
 *   - `EReportType` là một yếu tố chính để module tạo báo cáo biết được loại dữ liệu nào cần tổng hợp, 
 *     khoảng thời gian cần xem xét và định dạng đầu ra của báo cáo.
 *
 * Luồng dữ liệu:
 * - Khởi tạo `Report`:
 *   - Khi một báo cáo (`Report`) được tạo, một giá trị từ `EReportType` sẽ được gán cho trường `reportType` của `Report`.
 *   - Trong cơ sở dữ liệu, giá trị này thường được lưu trữ dưới dạng chuỗi khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity.
 * - Sử dụng trong logic nghiệp vụ:
 *   - Các dịch vụ tạo báo cáo sử dụng `EReportType` để kích hoạt các quy trình tổng hợp dữ liệu khác nhau.
 *   - Ví dụ: `WEEKLY_PROGRESS` sẽ yêu cầu tổng hợp dữ liệu từ `FoodLog`, `ActivityLog` và thay đổi cân nặng trong 7 ngày gần nhất, 
 *     trong khi `NUTRITION_SUMMARY` sẽ tập trung vào phân tích chi tiết macro và vi chất dinh dưỡng.
 */
public enum EReportType {
    /**
     * Đại diện cho báo cáo tiến độ hàng tuần của người dùng.
     * Loại báo cáo này kích hoạt việc tổng hợp dữ liệu về cân nặng, lượng calo tiêu thụ, 
     * và các hoạt động thể chất trong 7 ngày gần nhất để đánh giá tiến độ ngắn hạn.
     */
    WEEKLY_PROGRESS,
    /**
     * Đại diện cho báo cáo tiến độ hàng tháng của người dùng.
     * Loại báo cáo này tổng hợp dữ liệu trong 30 ngày gần nhất, cung cấp cái nhìn dài hạn hơn
     * về xu hướng cân nặng, thói quen ăn uống và mức độ hoạt động.
     */
    MONTHLY_PROGRESS,
    /**
     * Đại diện cho báo cáo tóm tắt chi tiết về dinh dưỡng.
     * Loại báo cáo này tập trung vào việc phân tích chi tiết các macro (protein, carbs, fats)
     * và có thể cả vi chất dinh dưỡng trong một khoảng thời gian cụ thể, giúp người dùng hiểu rõ
     * hơn về chất lượng chế độ ăn của mình.
     */
    NUTRITION_SUMMARY
}