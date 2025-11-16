package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `ESuggestionCategory` phân loại các loại đề xuất hoặc khuyến nghị được tạo bởi hệ thống AI.
 * Việc sử dụng enum này giúp hệ thống tổ chức, lọc và xử lý các đề xuất một cách có cấu trúc,
 * đồng thời giúp người dùng dễ dàng hiểu bản chất của từng gợi ý.
 *
 * Logic hoạt động:
 * - Phân loại đề xuất của AI:
 *   - Enum này định nghĩa các loại đề xuất cố định (`DIET_ADJUSTMENT`, `ACTIVITY_INCREASE`).
 *   - Điều này cho phép các mô hình AI tạo ra các gợi ý có mục tiêu rõ ràng và giúp người dùng 
 *     nhận biết ngay đề xuất đó thuộc về lĩnh vực nào (dinh dưỡng hay hoạt động thể chất).
 * - Hỗ trợ lọc và hiển thị:
 *   - Các module khác trong hệ thống có thể sử dụng enum này để lọc và hiển thị các đề xuất 
 *     dựa trên thể loại, ví dụ: chỉ hiển thị các gợi ý về chế độ ăn uống hoặc chỉ hiển thị các gợi ý về hoạt động thể chất.
 *
 * Luồng dữ liệu:
 * - Tạo `Suggestion`:
 *   - Khi một đề xuất (`Suggestion`) được tạo bởi hệ thống AI, một giá trị từ `ESuggestionCategory` 
 *     sẽ được gán cho trường `category` của `Suggestion`.
 *   - Trong cơ sở dữ liệu, giá trị này thường được lưu trữ dưới dạng chuỗi khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity.
 * - Sử dụng trong logic nghiệp vụ:
 *   - Các giao diện người dùng có thể sử dụng `ESuggestionCategory` để hiển thị các biểu tượng hoặc nhãn khác nhau cho từng loại đề xuất.
 *   - Các dịch vụ xử lý đề xuất có thể thực hiện các hành động khác nhau tùy thuộc vào thể loại của đề xuất, 
 *     ví dụ: cập nhật kế hoạch bữa ăn nếu là `DIET_ADJUSTMENT` hoặc gợi ý bài tập mới nếu là `ACTIVITY_INCREASE`.
 */
public enum ESuggestionCategory {
    /**
     * Đại diện cho các gợi ý liên quan đến việc điều chỉnh chế độ ăn uống.
     * Các ví dụ bao gồm: giảm lượng calo tổng thể, tăng lượng protein, giảm carbohydrate, 
     * thay thế thực phẩm không lành mạnh bằng các lựa chọn tốt hơn.
     */
    DIET_ADJUSTMENT,
    /**
     * Đại diện cho các gợi ý liên quan đến việc tăng cường hoạt động thể chất.
     * Các ví dụ bao gồm: tăng thời gian đi bộ hàng ngày, đề xuất các bài tập cụ thể, 
     * gợi ý tham gia các lớp học thể dục.
     */
    ACTIVITY_INCREASE
}