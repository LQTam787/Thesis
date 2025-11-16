package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `SharedContent` đại diện cho một mục nội dung được người dùng chia sẻ trong hệ thống. (ví dụ: một bài đăng về kế hoạch dinh dưỡng, nhật ký hoạt động).
 * Nó là một Entity JPA, ánh xạ tới bảng "shared_contents" trong cơ sở dữ liệu.
 * Entity này lưu trữ thông tin về nội dung được chia sẻ, chủ sở hữu, khả năng hiển thị và loại nội dung.
 *
 * Logic hoạt động:
 * - Định nghĩa nội dung được chia sẻ:
 *   - Mỗi `SharedContent` có một ID duy nhất (`id`), người dùng chia sẻ (`user`), ID của nội dung gốc (`contentId`),
 *     loại nội dung (`contentType` - enum), phạm vi hiển thị (`visibility` - enum) và thời điểm tạo (`createdAt`).
 *   - Mục đích là cung cấp một cơ chế chung để người dùng có thể chia sẻ các dữ liệu liên quan đến sức khỏe/dinh dưỡng của họ.
 * - Cơ chế lưu trữ:
 *   - `@Entity` chỉ định lớp này là một thực thể cơ sở dữ liệu.
 *   - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với `User`:
 *   - `@ManyToOne` với `User`: Một `SharedContent` thuộc về một `User` duy nhất (người tạo/chủ sở hữu).
 *   - `@JoinColumn(name = "user_id", nullable = false)` tạo khóa ngoại `user_id` và đảm bảo rằng 
 * mỗi nội dung chia sẻ phải có một người dùng liên kết.
 * - Liên kết đến nội dung gốc:
 *   - `contentId` và `contentType` hoạt động như một cặp khóa ngoại tổng quát để trỏ đến các entity khác (ví dụ: `NutritionPlan`, `ActivityLog`, `Recipe`).
 *   - `contentType` sử dụng `@Enumerated(EnumType.STRING)` để lưu trữ tên enum dưới dạng chuỗi, dễ đọc và linh hoạt.
 * - Quản lý khả năng hiển thị:
 *   - `visibility` sử dụng `@Enumerated(EnumType.STRING)` để xác định phạm vi mà nội dung được hiển thị (ví dụ: PUBLIC, PRIVATE, FRIENDS).
 * - Vòng đời (`@PrePersist`):
 *   - Phương thức `onCreate()` được đánh dấu `@PrePersist` tự động thiết lập `createdAt` là thời điểm hiện tại khi `SharedContent` được lưu vào cơ sở dữ liệu lần đầu.
 *
 * Luồng dữ liệu:
 * - Tạo nội dung chia sẻ:
 *   - Khi người dùng quyết định chia sẻ một thông tin (ví dụ: một kế hoạch dinh dưỡng), một đối tượng `SharedContent` mới được tạo.
 *   - Các thuộc tính như `user`, `contentId`, `contentType`, `visibility` được thiết lập và `createdAt` được tự động gán.
 *   - Đối tượng này sau đó được lưu vào cơ sở dữ liệu, tạo ra một bản ghi có thể được người dùng khác bình luận (`ContentComment`) hoặc thích (`ContentLike`).
 * - Truy vấn nội dung chia sẻ:
 *   - Khi truy vấn `SharedContent`, các thuộc tính của nội dung được tải. Đối tượng `User` liên quan được tải khi cần.
 *   - Hệ thống có thể sử dụng `contentId` và `contentType` để tải nội dung gốc chi tiết (ví dụ: tải một `NutritionPlan` cụ thể).
 *   - Khả năng hiển thị (`visibility`) được sử dụng để lọc các nội dung phù hợp với người dùng đang xem.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedContent {

    /**
     * `id` là khóa chính duy nhất cho mỗi mục nội dung được chia sẻ. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `SharedContent` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `user` là đối tượng `User` đã chia sẻ hoặc là chủ sở hữu của nội dung này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `SharedContent` có thể được tạo bởi một `User` duy nhất.
     * `@JoinColumn(name = "user_id", nullable = false)` tạo khóa ngoại `user_id` và đảm bảo rằng 
     * mỗi nội dung chia sẻ phải có một người dùng liên kết.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * `contentId` là ID của nội dung thực tế được chia sẻ (ví dụ: ID của `NutritionPlan`, `ActivityLog` hoặc `Recipe`).
     * Nó hoạt động cùng với `contentType` để xác định duy nhất nội dung gốc được tham chiếu.
     * `@Column(nullable = false)` đảm bảo trường này luôn phải có giá trị.
     */
    @Column(nullable = false)
    private Long contentId;

    /**
     * `contentType` định nghĩa loại nội dung được chia sẻ, sử dụng enum `ContentType`.
     * `@Enumerated(EnumType.STRING)` đảm bảo tên enum được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu, 
     * giúp dễ đọc và linh hoạt hơn. `@Column(nullable = false)` đảm bảo trường này luôn phải có giá trị.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    /**
     * `visibility` định nghĩa phạm vi hiển thị của nội dung được chia sẻ, sử dụng enum `Visibility`.
     * `@Enumerated(EnumType.STRING)` đảm bảo tên enum được lưu trữ dưới dạng chuỗi trong cơ sở dữ liệu, 
     * cho phép kiểm soát quyền riêng tư (ví dụ: PUBLIC, PRIVATE, FRIENDS). 
     * `@Column(nullable = false)` đảm bảo trường này luôn phải có giá trị.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    /**
     * `createdAt` là thời điểm (ngày và giờ) nội dung được chia sẻ.
     * Giá trị này được tự động thiết lập khi đối tượng được lưu vào cơ sở dữ liệu lần đầu tiên thông qua `@PrePersist`.
     */
    private LocalDateTime createdAt;

    /**
     * Phương thức `onCreate()` được gọi tự động trước khi một thực thể `SharedContent` được lưu vào cơ sở dữ liệu lần đầu tiên (`@PrePersist`).
     * Nó thiết lập trường `createdAt` với thời điểm hiện tại của hệ thống, đảm bảo mỗi nội dung chia sẻ đều có dấu thời gian chính xác.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}