package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;

/**
 * Service interface cho việc xử lý các hoạt động theo dõi (tracking) liên quan đến sức khỏe và dinh dưỡng của người dùng.
 * Giao diện này định nghĩa các phương thức cho phép người dùng ghi lại lượng thức ăn đã tiêu thụ, các hoạt động thể chất đã thực hiện,
 * và tạo ra các báo cáo tiến độ tổng hợp dựa trên dữ liệu đã ghi lại. Mục tiêu là cung cấp một tầng trừu tượng
 * cho logic nghiệp vụ liên quan đến việc theo dõi và báo cáo tiến độ cá nhân.
 */
public interface TrackingService {

    /**
     * Ghi lại một nhật ký thức ăn (Food Log) cho một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của người dùng ({@code userId}) và một đối tượng {@link FoodLogDto} chứa chi tiết về món ăn đã tiêu thụ.</li>
     *     <li>Chuyển đổi {@link FoodLogDto} này thành một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.FoodLog} Entity.</li>
     *     <li>Lưu đối tượng {@code FoodLog} mới vào cơ sở dữ liệu.</li>
     *     <li>Trả về một đối tượng {@link FoodLogDto} của nhật ký thức ăn đã được ghi thành công.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Thao tác này thường liên quan đến việc cập nhật hoặc tính toán lại tổng lượng calo/dinh dưỡng
     * đã tiêu thụ trong ngày hoặc một khoảng thời gian cụ thể của người dùng.</p>
     *
     * @param userId ID của người dùng thực hiện ghi nhật ký thức ăn.
     * @param foodLogDto {@link FoodLogDto} chứa thông tin chi tiết về món ăn đã tiêu thụ.
     * @return {@link FoodLogDto} của nhật ký thức ăn đã được ghi.
     */
    FoodLogDto logFood(String userId, FoodLogDto foodLogDto);

    /**
     * Ghi lại một nhật ký hoạt động (Activity Log) cho một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của người dùng ({@code userId}) và một đối tượng {@link ActivityLogDto} chứa chi tiết về hoạt động đã thực hiện.</li>
     *     <li>Chuyển đổi {@link ActivityLogDto} này thành một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.domain.ActivityLog} Entity.</li>
     *     <li>Lưu đối tượng {@code ActivityLog} mới vào cơ sở dữ liệu.</li>
     *     <li>Trả về một đối tượng {@link ActivityLogDto} của nhật ký hoạt động đã được ghi thành công.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Thao tác này thường liên quan đến việc tính toán lượng calo đã đốt cháy
     * và cập nhật các chỉ số hoạt động của người dùng.</p>
     *
     * @param userId ID của người dùng thực hiện ghi nhật ký hoạt động.
     * @param activityLogDto {@link ActivityLogDto} chứa thông tin chi tiết về hoạt động.
     * @return {@link ActivityLogDto} của nhật ký hoạt động đã được ghi.
     */
    ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto);

    /**
     * Tạo báo cáo tiến độ tổng hợp cho người dùng dựa trên các nhật ký thức ăn và hoạt động.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của người dùng ({@code userId}) và loại báo cáo ({@code reportType}, ví dụ: "weekly" hoặc "monthly").</li>
     *     <li>Truy vấn cơ sở dữ liệu để lấy các dữ liệu liên quan như {@link com.nutrition.ai.nutritionaibackend.model.domain.FoodLog},
     *         {@link com.nutrition.ai.nutritionaibackend.model.domain.ActivityLog} và dữ liệu mục tiêu của người dùng trong một khoảng thời gian cụ thể
     *         (được xác định bởi {@code reportType}).</li>
     *     <li>Thực hiện các tính toán và tổng hợp cần thiết từ dữ liệu đã truy vấn.</li>
     *     <li>Tạo một đối tượng {@link ProgressReportDto} chứa dữ liệu báo cáo toàn diện và trả về.</li>
     * </ol>
     * <p>Nguyên lý hoạt động: Phương pháp này tập trung vào việc tổng hợp thông tin từ nhiều nguồn
     * để cung cấp cái nhìn tổng quan về tiến độ sức khỏe và dinh dưỡng của người dùng.</p>
     *
     * @param userId ID của người dùng để tạo báo cáo.
     * @param reportType Loại báo cáo cần tạo (ví dụ: "weekly" cho hàng tuần, "monthly" cho hàng tháng).
     * @return {@link ProgressReportDto} chứa dữ liệu báo cáo tiến độ toàn diện.
     */
    ProgressReportDto generateProgressReport(String userId, String reportType);
}