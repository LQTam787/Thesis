package com.nutrition.ai.nutritionaibackend.model.domain;

import com.nutrition.ai.nutritionaibackend.model.enums.ESuggestionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `Suggestion` đại diện cho một đề xuất hoặc khuyến nghị được tạo bởi hệ thống AI cho người dùng.
 * Nó là một Entity JPA, ánh xạ tới bảng "suggestions" trong cơ sở dữ liệu.
 * Mỗi đề xuất có nội dung, ngày tạo, thể loại và liên kết với người dùng nhận được đề xuất đó.
 *
 * Logic hoạt động:
 * - Tạo và lưu trữ đề xuất:
 *   - Mỗi `Suggestion` có một ID duy nhất (`id`), `suggestionText` (nội dung đề xuất), `createdDate` và `category` (loại `ESuggestionCategory` enum).
 *   - `suggestionText` sử dụng `@Lob` để lưu trữ chuỗi văn bản dài.
 *   - Mục đích là để cung cấp các gợi ý cá nhân hóa về dinh dưỡng, hoạt động hoặc các khía cạnh sức khỏe khác dựa trên dữ liệu người dùng và mô hình AI.
 * - Cơ chế lưu trữ:
 *   - `@Entity` và `@Table(name = "suggestions")` chỉ định rằng lớp này là một thực thể cơ sở dữ liệu và được ánh xạ tới bảng `suggestions`.
 *   - `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với người dùng:
 *   - `@ManyToOne` với `User`: Một `Suggestion` được tạo cho một `User` duy nhất. Khóa ngoại `user_id` được tạo trong bảng `suggestions`.
 *   - `fetch = FetchType.LAZY` để tối ưu hóa hiệu suất, chỉ tải đối tượng `User` khi cần.
 * - Xử lý thể loại đề xuất:
 *   - `@Enumerated(EnumType.STRING)` đảm bảo rằng `ESuggestionCategory` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
 *
 * Luồng dữ liệu:
 * - Tạo/Cập nhật:
 *   - `Suggestion` được tạo bởi các dịch vụ AI hoặc logic nghiệp vụ dựa trên thông tin hồ sơ, nhật ký và mục tiêu của người dùng.
 *   - `suggestionText`, `createdDate` và `category` được thiết lập khi đề xuất được tạo.
 * - Truy vấn:
 *   - Khi truy vấn một `Suggestion`, các thuộc tính của đề xuất được tải.
 *   - Đối tượng `User` liên quan sẽ được tải `LAZY` khi được yêu cầu, cho phép hệ thống biết đề xuất này dành cho ai.
 *   - Các đề xuất này được hiển thị cho người dùng để giúp họ đưa ra các quyết định tốt hơn về sức khỏe và lối sống.
 */
@Entity
@Table(name = "suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    /**
     * `id` là khóa chính duy nhất cho mỗi đề xuất. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `Suggestion` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `suggestionText` là nội dung chi tiết của đề xuất hoặc khuyến nghị.
     * @Lob chỉ định rằng trường này là một đối tượng lớn, phù hợp để lưu trữ dữ liệu văn bản dài.
     * Nội dung này có thể bao gồm gợi ý thực phẩm, lịch trình tập luyện, mẹo sức khỏe, v.v.
     */
    @Lob
    private String suggestionText;

    /**
     * `createdDate` là ngày và giờ chính xác khi đề xuất này được tạo bởi hệ thống.
     * Trường này giúp theo dõi thời gian tạo đề xuất và có thể được sử dụng để sắp xếp hoặc lọc các gợi ý mới nhất.
     */
    private LocalDateTime createdDate;

    /**
     * `category` là thể loại của đề xuất (ví dụ: `NUTRITION`, `ACTIVITY`, `GENERAL_HEALTH`).
     * @Enumerated(EnumType.STRING) đảm bảo rằng giá trị enum `ESuggestionCategory` được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu.
     * Trường này giúp phân loại và lọc các đề xuất theo chủ đề.
     */
    @Enumerated(EnumType.STRING)
    private ESuggestionCategory category;

    /**
     * `user` là đối tượng `User` mà đề xuất này được tạo cho.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `Suggestion` có thể thuộc về một `User` duy nhất.
     * `@JoinColumn(name = "user_id")` tạo một khóa ngoại `user_id` trong bảng `suggestions`,
     * liên kết đến bảng `users`.
     * `fetch = FetchType.LAZY` đảm bảo rằng đối tượng `User` chỉ được tải từ cơ sở dữ liệu khi nó được truy cập lần đầu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}