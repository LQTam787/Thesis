package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `EMealType` định nghĩa các loại bữa ăn khác nhau trong suốt một ngày (ví dụ: Bữa sáng, Bữa trưa, Bữa tối, Bữa ăn nhẹ).
 * Việc sử dụng enum này đảm bảo tính nhất quán trong việc phân loại bữa ăn và hỗ trợ việc quản lý thực đơn.
 *
 * Logic hoạt động:
 * - Định nghĩa các loại bữa ăn tiêu chuẩn:
 *   - Enum này cung cấp một tập hợp các giá trị cố định (`BREAKFAST`, `LUNCH`, `DINNER`, `SNACK`) để phân loại các bữa ăn.
 *   - Điều này giúp tổ chức dữ liệu bữa ăn một cách có cấu trúc và dễ dàng truy vấn theo loại bữa ăn.
 * - Vai trò trong quản lý thực đơn:
 *   - `EMealType` được sử dụng để gán loại cho các đối tượng `Meal` trong `DailyMenu`, cho phép hệ thống tạo ra các kế hoạch dinh dưỡng có cấu trúc theo từng bữa trong ngày.
 *
 * Luồng dữ liệu:
 * - Khởi tạo/Cập nhật `Meal`:
 *   - Khi một `Meal` được tạo hoặc cập nhật, một giá trị từ `EMealType` sẽ được gán cho trường `mealType` của `Meal`.
 *   - Trong cơ sở dữ liệu, giá trị này thường được lưu trữ dưới dạng chuỗi khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity.
 * - Sử dụng trong logic nghiệp vụ:
 *   - Các dịch vụ (Service layer) sử dụng `EMealType` để lọc và tổng hợp thông tin dinh dưỡng cho từng loại bữa ăn,
 *     ví dụ, tính tổng calo cho tất cả các bữa sáng hoặc đề xuất món ăn phù hợp với bữa trưa.
 */
public enum EMealType {
    /**
     * Đại diện cho bữa sáng.
     * Được sử dụng để phân loại các món ăn hoặc thực phẩm được tiêu thụ vào buổi sáng, giúp tính toán và phân tích dinh dưỡng theo bữa.
     */
    BREAKFAST,
    /**
     * Đại diện cho bữa trưa.
     * Được sử dụng để phân loại các món ăn hoặc thực phẩm được tiêu thụ vào buổi trưa.
     */
    LUNCH,
    /**
     * Đại diện cho bữa tối.
     * Được sử dụng để phân loại các món ăn hoặc thực phẩm được tiêu thụ vào buổi tối.
     */
    DINNER,
    /**
     * Đại diện cho các bữa ăn nhẹ giữa các bữa chính.
     * Giúp theo dõi lượng calo và dinh dưỡng từ các bữa ăn phụ, quan trọng cho việc quản lý cân nặng và năng lượng.
     */
    SNACK
}