package com.nutrition.ai.nutritionaibackend.model.shared;

/**
 * Enum `ContentType` định nghĩa các loại nội dung nghiệp vụ khác nhau có thể được chia sẻ trong hệ thống.
 * Việc sử dụng enum này đảm bảo tính nhất quán trong việc xác định loại nội dung và hỗ trợ liên kết với các entity gốc.
 *
 * Logic hoạt động:
 * - Định nghĩa các loại nội dung cố định:
 *   - Enum này cung cấp một tập hợp các giá trị cố định và hợp lệ (`NUTRITION_PLAN`, `ACTIVITY_LOG`, `RECIPE`).
 *   - Điều này giúp tiêu chuẩn hóa các loại nội dung có thể được chia sẻ và tránh dữ liệu không nhất quán.
 * - Vai trò trong `SharedContent`:
 *   - `ContentType` được sử dụng trong entity `SharedContent` để xác định chính xác loại dữ liệu nghiệp vụ
 *     mà `SharedContent` đang tham chiếu đến thông qua `contentId`.
 *   - Nó cho phép hệ thống truy xuất và hiển thị nội dung gốc một cách chính xác.
 *
 * Luồng dữ liệu:
 * - Tạo `SharedContent`:
 *   - Khi một `SharedContent` được tạo, một giá trị từ `ContentType` sẽ được gán cho trường `contentType` của `SharedContent`.
 *   - Trong cơ sở dữ liệu, giá trị này được lưu trữ dưới dạng chuỗi (ví dụ: "NUTRITION_PLAN") khi sử dụng `@Enumerated(EnumType.STRING)`.
 * - Truy vấn và xử lý nội dung:
 *   - Khi hệ thống cần truy xuất nội dung gốc từ một `SharedContent` (sử dụng `contentId`),
 *     `ContentType` sẽ được dùng để xác định Repository hoặc Service nào cần được gọi để tải dữ liệu phù hợp.
 *   - Ví dụ: nếu `contentType` là `NUTRITION_PLAN`, hệ thống sẽ biết phải tải một `NutritionPlan` dựa trên `contentId`.
 */
public enum ContentType {
    /**
     * Đại diện cho một Kế hoạch Dinh dưỡng (`NutritionPlan`) được chia sẻ.
     * Khi `SharedContent` có `contentType` này, `contentId` của nó sẽ trỏ đến ID của một `NutritionPlan`.
     */
    NUTRITION_PLAN, 
    /**
     * Đại diện cho một Nhật ký Hoạt động (`ActivityLog`) được chia sẻ.
     * Khi `SharedContent` có `contentType` này, `contentId` của nó sẽ trỏ đến ID của một `ActivityLog`.
     */
    ACTIVITY_LOG,   
    /**
     * Đại diện cho một Công thức nấu ăn (`Recipe`) được chia sẻ.
     * Khi `SharedContent` có `contentType` này, `contentId` của nó sẽ trỏ đến ID của một `Recipe`.
     */
    RECIPE          
}