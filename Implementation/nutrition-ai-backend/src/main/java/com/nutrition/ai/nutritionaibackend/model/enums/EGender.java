package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `EGender` định nghĩa các giá trị giới tính khả dụng cho hồ sơ người dùng trong ứng dụng.
 * Việc sử dụng enum đảm bảo tính nhất quán của dữ liệu và giới hạn các lựa chọn hợp lệ.
 *
 * Logic hoạt động:
 * - Định nghĩa các giá trị cố định:
 *   - Enum này cung cấp một tập hợp các giá trị cố định và hợp lệ (`MALE`, `FEMALE`, `OTHER`) cho thuộc tính giới tính của người dùng.
 *   - Điều này giúp duy trì tính toàn vẹn của dữ liệu và đơn giản hóa việc xử lý logic liên quan đến giới tính.
 * - Vai trò trong tính toán:
 *   - Các giá trị giới tính là yếu tố quan trọng trong nhiều công thức tính toán dinh dưỡng và mục tiêu sức khỏe, 
 *     ví dụ như tính toán Tỷ lệ trao đổi chất cơ bản (BMR) hoặc Tổng năng lượng tiêu hao hàng ngày (TDEE).
 * - Mở rộng tính bao hàm:
 *   - Giá trị `OTHER` cung cấp sự linh hoạt và tính bao hàm cho những người dùng không xác định là nam hoặc nữ, 
 *     cho phép hệ thống linh hoạt hơn trong việc thu thập thông tin người dùng.
 *
 * Luồng dữ liệu:
 * - Khởi tạo/Cập nhật `Profile`:
 *   - Khi tạo hoặc cập nhật hồ sơ người dùng (`Profile`), giá trị `EGender` được gán cho trường `gender` của `Profile`.
 *   - Trong cơ sở dữ liệu, giá trị này thường được lưu trữ dưới dạng chuỗi (ví dụ: "MALE", "FEMALE") khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity.
 * - Sử dụng trong logic nghiệp vụ:
 *   - Các dịch vụ (Service layer) sử dụng giá trị `EGender` để thực hiện các phép tính và đề xuất cá nhân hóa dựa trên giới tính.
 *   - Ví dụ: Một thuật toán có thể gợi ý lượng calo khác nhau cho nam và nữ để đạt được cùng một mục tiêu giảm cân.
 */
public enum EGender {
    /**
     * Đại diện cho giới tính nam.
     * Giá trị này được sử dụng trong các tính toán dinh dưỡng, thuật toán đề xuất và cá nhân hóa kế hoạch sức khỏe
     * dựa trên các đặc điểm sinh học và nhu cầu năng lượng của nam giới.
     */
    MALE,
    /**
     * Đại diện cho giới tính nữ.
     * Giá trị này được sử dụng tương tự như `MALE` nhưng áp dụng các công thức và khuyến nghị phù hợp với nữ giới.
     */
    FEMALE,
    /**
     * Đại diện cho các giới tính khác, hoặc khi người dùng không muốn tiết lộ giới tính cụ thể.
     * Việc bao gồm `OTHER` đảm bảo tính linh hoạt và tôn trọng quyền riêng tư của người dùng.
     * Hệ thống có thể cần có logic xử lý đặc biệt cho trường hợp này (ví dụ: sử dụng giá trị trung bình hoặc yêu cầu thêm thông tin).
     */
    OTHER
}