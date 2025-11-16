package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * Enum `EGoalType` định nghĩa các loại mục tiêu sức khỏe và dinh dưỡng mà người dùng có thể đặt ra.
 * Việc sử dụng enum này đảm bảo tính nhất quán của dữ liệu và cho phép các thuật toán AI/NLP 
 * dễ dàng hiểu và xử lý mục tiêu của người dùng.
 *
 * Logic hoạt động:
 * - Định nghĩa các loại mục tiêu cố định:
 *   - Enum này cung cấp một tập hợp các giá trị cố định và hợp lệ (`WEIGHT_LOSS`, `WEIGHT_GAIN`, `MUSCLE_GAIN`, `MAINTAIN_WEIGHT`, `RECOVERY`).
 *   - Điều này giúp tiêu chuẩn hóa các mục tiêu và tránh dữ liệu không nhất quán.
 * - Đầu vào cho thuật toán AI:
 *   - `EGoalType` là một thông số đầu vào chính cho các thuật toán AI khi tính toán và đưa ra các khuyến nghị dinh dưỡng và hoạt động phù hợp.
 *   - Ví dụ: đối với `WEIGHT_LOSS`, AI sẽ đề xuất một kế hoạch dinh dưỡng với thâm hụt calo; đối với `MUSCLE_GAIN`, AI sẽ tập trung vào dư thừa calo và tăng lượng protein.
 *
 * Luồng dữ liệu:
 * - Khởi tạo/Cập nhật `Goal`:
 *   - Khi người dùng tạo hoặc cập nhật mục tiêu (`Goal`) của mình, một giá trị từ `EGoalType` sẽ được gán cho trường `goalType` của `Goal`.
 *   - Trong cơ sở dữ liệu, giá trị này thường được lưu trữ dưới dạng chuỗi khi sử dụng `@Enumerated(EnumType.STRING)` trong Entity.
 * - Sử dụng trong logic nghiệp vụ:
 *   - Các dịch vụ (Service layer) và thuật toán AI sử dụng `EGoalType` để điều chỉnh các tính toán nhu cầu calo, phân bổ macro dinh dưỡng, 
 *     và gợi ý các hoạt động thể chất phù hợp để giúp người dùng đạt được mục tiêu của họ.
 */
public enum EGoalType {
    /**
     * Đại diện cho mục tiêu giảm cân.
     * Khi người dùng chọn mục tiêu này, hệ thống sẽ đề xuất một kế hoạch dinh dưỡng tạo ra sự thâm hụt calo
     * và các hoạt động thể chất giúp đốt cháy calo hiệu quả.
     */
    WEIGHT_LOSS,
    /**
     * Đại diện cho mục tiêu tăng cân.
     * Hệ thống sẽ đề xuất một kế hoạch dinh dưỡng với sự dư thừa calo và tập trung vào các loại thực phẩm giàu năng lượng,
     * cũng như các hoạt động hỗ trợ tăng khối lượng cơ bắp.
     */
    WEIGHT_GAIN,
    /**
     * Đại diện cho mục tiêu tăng cơ bắp.
     * Kế hoạch dinh dưỡng sẽ tập trung vào lượng protein cao và calo dư thừa để hỗ trợ phát triển cơ bắp,
     * kết hợp với các khuyến nghị về luyện tập sức mạnh.
     */
    MUSCLE_GAIN,
    /**
     * Đại diện cho mục tiêu duy trì cân nặng hiện tại.
     * Hệ thống sẽ tạo ra một kế hoạch dinh dưỡng cân bằng, cung cấp đủ calo để duy trì cân nặng mà không tăng hoặc giảm,
     * và các hoạt động thể chất để duy trì sức khỏe tổng thể.
     */
    MAINTAIN_WEIGHT,
    /**
     * Đại diện cho mục tiêu phục hồi sức khỏe sau ốm, phẫu thuật hoặc tình trạng suy nhược.
     * Kế hoạch dinh dưỡng sẽ ưu tiên các thực phẩm giàu dưỡng chất, dễ tiêu hóa và hỗ trợ hệ miễn dịch,
     * cùng với các hoạt động nhẹ nhàng để tăng cường sức khỏe dần dần.
     */
    RECOVERY
}