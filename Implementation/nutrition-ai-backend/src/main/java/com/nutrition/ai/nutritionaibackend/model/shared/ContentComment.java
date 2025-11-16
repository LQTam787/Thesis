package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `ContentComment` đại diện cho một bình luận của người dùng trên một nội dung được chia sẻ (`SharedContent`).
 * Nó là một Entity JPA, ánh xạ tới bảng "content_comments" trong cơ sở dữ liệu.
 * Entity này lưu trữ nội dung bình luận, người bình luận (`User`), và `SharedContent` mà bình luận đó thuộc về.
 *
 * Logic hoạt động:
 * - Ghi lại bình luận:
 *   - Mỗi `ContentComment` có một ID duy nhất (`id`), nội dung văn bản (`text`) và thời điểm tạo (`createdAt`).
 *   - `text` được lưu trữ dưới dạng TEXT (`columnDefinition = "TEXT"`) để chứa chuỗi dài.
 *   - Mục đích là để người dùng có thể tương tác và đưa ra phản hồi về các nội dung được chia sẻ.
 * - Cơ chế lưu trữ:
 *   - `@Entity` chỉ định lớp này là một thực thể cơ sở dữ liệu.
 *   - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code và cung cấp cách tạo đối tượng linh hoạt.
 * - Mối quan hệ với `SharedContent` và `User`:
 *   - `@ManyToOne` với `SharedContent`: Một bình luận thuộc về một `SharedContent` duy nhất. Khóa ngoại `shared_content_id` được tạo.
 *   - `@ManyToOne` với `User`: Một bình luận được tạo bởi một `User` duy nhất. Khóa ngoại `user_id` được tạo.
 *   - `nullable = false` đảm bảo rằng một bình luận luôn phải liên kết với một nội dung chia sẻ và một người dùng.
 * - Vòng đời (`@PrePersist`):
 *   - Phương thức `onCreate()` được đánh dấu `@PrePersist` tự động thiết lập `createdAt` là thời điểm hiện tại khi `ContentComment` được lưu vào cơ sở dữ liệu lần đầu.
 *
 * Luồng dữ liệu:
 * - Tạo bình luận:
 *   - Khi người dùng gửi một bình luận trên `SharedContent`, một đối tượng `ContentComment` mới được tạo với `text` và liên kết với `SharedContent` và `User` hiện có.
 *   - `createdAt` được tự động gán. Sau đó, đối tượng này được lưu vào cơ sở dữ liệu.
 * - Truy vấn bình luận:
 *   - Khi truy vấn các bình luận, các thuộc tính của bình luận được tải. Các đối tượng `SharedContent` và `User` liên quan được tải khi cần.
 *   - Dữ liệu này được sử dụng để hiển thị các bình luận dưới nội dung được chia sẻ, cho phép người dùng xem và tương tác với các bình luận khác.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentComment {

    /**
     * `id` là khóa chính duy nhất cho mỗi bình luận. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `ContentComment` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `sharedContent` là đối tượng `SharedContent` mà bình luận này được gửi lên.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ContentComment` có thể thuộc về một `SharedContent` duy nhất.
     * `@JoinColumn(name = "shared_content_id", nullable = false)` tạo khóa ngoại `shared_content_id` 
     * và đảm bảo rằng mỗi bình luận phải liên kết với một `SharedContent`.
     */
    @ManyToOne
    @JoinColumn(name = "shared_content_id", nullable = false)
    private SharedContent sharedContent;

    /**
     * `user` là đối tượng `User` đã tạo bình luận này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ContentComment` có thể được tạo bởi một `User` duy nhất.
     * `@JoinColumn(name = "user_id", nullable = false)` tạo khóa ngoại `user_id` và đảm bảo rằng 
     * mỗi bình luận phải có một người dùng tạo ra.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * `text` là nội dung văn bản của bình luận. 
     * `@Column(nullable = false, columnDefinition = "TEXT")` chỉ định rằng trường này không được null 
     * và được lưu trữ dưới dạng kiểu TEXT trong cơ sở dữ liệu để hỗ trợ chuỗi dài.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * `createdAt` là thời điểm (ngày và giờ) bình luận được tạo. 
     * Giá trị này được tự động thiết lập khi đối tượng được lưu vào cơ sở dữ liệu lần đầu tiên thông qua `@PrePersist`.
     */
    private LocalDateTime createdAt;

    /**
     * Phương thức `onCreate()` được gọi tự động trước khi một thực thể `ContentComment` được lưu vào cơ sở dữ liệu lần đầu tiên (`@PrePersist`).
     * Nó thiết lập trường `createdAt` với thời điểm hiện tại của hệ thống, đảm bảo mỗi bình luận đều có dấu thời gian chính xác.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}