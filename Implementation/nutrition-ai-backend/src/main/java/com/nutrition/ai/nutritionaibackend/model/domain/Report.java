package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `Report` đại diện cho một báo cáo tổng hợp hoặc phân tích được tạo cho người dùng (ví dụ: Tình trạng dinh dưỡng hàng tháng, báo cáo tiến độ tập luyện).
 * Nó là một Entity JPA, ánh xạ tới bảng "reports" trong cơ sở dữ liệu.
 * Mỗi báo cáo có loại, ngày tạo và nội dung chi tiết, đồng thời liên kết với người dùng sở hữu nó.
 *
 * Logic hoạt động:
 * - Tạo và lưu trữ báo cáo:
 *   - Mỗi `Report` có một ID duy nhất (`id`), `reportType` (enum), `generatedDate` và `content` (nội dung chi tiết).
 *   - `content` sử dụng `@Lob` để lưu trữ chuỗi dữ liệu lớn, thường là JSON hoặc văn bản định dạng chứa các phân tích.
 *   - Mục đích là để cung cấp cái nhìn tổng quan và thông tin chi tiết về tiến độ hoặc tình trạng dinh dưỡng của người dùng theo thời gian.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "reports")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `reports`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng:
 *   - `@ManyToOne` với `User`: Một `Report` thuộc về một `User` duy nhất. Khóa ngoại `user_id` được tạo trong bảng `reports`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải đối tượng `User` khi cần.
 * - Xử lý loại báo cáo:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `EReportType` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Report` được tạo định kỳ hoặc theo yêu cầu của người dùng/hệ thống, dựa trên dữ liệu thu thập được từ các nhật ký (FoodLog, ActivityLog), hồ sơ (`Profile`) và mục tiêu (`Goal`).
 *   - `content` được tạo động từ quá trình phân tích dữ liệu.
 * - Truy vấn:
 *   - Khi truy vấn một `Report`, các thuộc tính cơ bản của báo cáo được tải.
 *   - Đối tượng `User` liên quan sẽ được tải `LAZY` khi được yêu cầu.
 *   - `content` của báo cáo được sử dụng để hiển thị các phân tích, biểu đồ và thông tin tổng quan cho người dùng, giúp họ hiểu rõ hơn về tình trạng sức khỏe và tiến độ của mình.
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    /**
     * `id` là khóa chính duy nhất cho mỗi báo cáo. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Report` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `reportType` là loại báo cáo (ví dụ: `NUTRITION_SUMMARY`, `PROGRESS_REPORT`).
     * @Enumerated(EnumType.STRING) đảm bảo rằng giá trị enum `EReportType` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
     * Trường này giúp phân loại và truy xuất các loại báo cáo khác nhau.
     */
    @Enumerated(EnumType.STRING)
    private EReportType reportType;

    /**
     * `generatedDate` là ngày và giờ chính xác khi báo cáo được tạo ra.
     * Trường này quan trọng để theo dõi thời điểm tạo báo cáo và đảm bảo người dùng nhận được thông tin mới nhất.
     */
    private LocalDateTime generatedDate;

    /**
     * `content` lưu trữ nội dung chi tiết của báo cáo dưới dạng một chuỗi lớn (thường là JSON, XML, hoặc văn bản định dạng).
     * @Lob chỉ định rằng trường này là một đối tượng lớn, phù hợp để lưu trữ dữ liệu văn bản dài.
     * Nội dung này bao gồm các phân tích, tóm tắt, biểu đồ và khuyến nghị dựa trên dữ liệu người dùng.
     */
    @Lob
    private String content;

    /**
     * `user` là đối tượng `User` mà báo cáo này được tạo cho.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `Report` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `reports`,
     * liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}