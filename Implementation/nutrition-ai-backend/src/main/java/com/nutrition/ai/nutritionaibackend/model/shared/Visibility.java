package com.nutrition.ai.nutritionaibackend.model.shared;

/**
 * Enum `Visibility` định nghĩa các cấp độ hiển thị khác nhau cho nội dung được chia sẻ (`SharedContent`).
 * Việc sử dụng enum này đảm bảo tính nhất quán trong việc quản lý quyền riêng tư và kiểm soát ai có thể xem nội dung đã chia sẻ.
 *
 * Logic hoạt động:
 * - Định nghĩa các cấp độ hiển thị cố định:
 *   - Enum này cung cấp một tập hợp các giá trị cố định và hợp lệ (`PUBLIC`, `FRIENDS`).
 *   - Điều này giúp tiêu chuẩn hóa các tùy chọn quyền riêng tư và đảm bảo rằng chỉ các cấp độ hiển thị được xác định trước mới có thể được áp dụng.
 * - Vai trò trong `SharedContent`:
 *   - `Visibility` được sử dụng trong entity `SharedContent` để kiểm soát phạm vi tiếp cận của nội dung được chia sẻ.
 *   - Ví dụ, nếu một `SharedContent` có `visibility` là `FRIENDS`, chỉ những người dùng có trong danh sách bạn bè của người chia sẻ mới có thể xem nội dung đó.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật `SharedContent`:
 *   - Khi người dùng tạo hoặc cập nhật một `SharedContent`, một giá trị từ `Visibility` sẽ được gán cho trường `visibility` của `SharedContent`.
 *   - Trong cơ sở dữ liệu, giá trị này được lưu trữ dưới dạng chuỗi (ví dụ: "PUBLIC", "FRIENDS") khi sử dụng `@Enumerated(EnumType.STRING)`.
 * - Kiểm soát quyền truy cập:
 *   - Khi một người dùng cố gắng xem một `SharedContent`, hệ thống sẽ kiểm tra giá trị `visibility` của nội dung đó.
 *   - Dựa trên `visibility` và mối quan hệ của người dùng với chủ sở hữu nội dung (ví dụ: có phải là bạn bè hay không), hệ thống sẽ quyết định liệu người dùng đó có quyền xem nội dung hay không.
 */
public enum Visibility {
    /**
     * Đại diện cho nội dung công khai. Bất kỳ ai cũng có thể xem nội dung này.
     * Khi một `SharedContent` có `visibility` là `PUBLIC`, nó sẽ hiển thị cho tất cả người dùng hệ thống (và có thể cả khách truy cập tùy thuộc vào cấu hình).
     */
    PUBLIC,
    /**
     * Đại diện cho nội dung chỉ dành cho bạn bè.
     * Chỉ những người dùng có trong danh sách bạn bè của người chia sẻ mới có quyền truy cập nội dung này.
     * Điều này cung cấp một cấp độ quyền riêng tư cao hơn so với `PUBLIC`.
     */
    FRIENDS
}