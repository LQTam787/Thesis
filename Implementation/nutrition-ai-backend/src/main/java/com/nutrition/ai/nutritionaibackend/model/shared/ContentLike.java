package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp `ContentLike` đại diện cho hành động "Thích" (Like) của một người dùng đối với một nội dung được chia sẻ (`SharedContent`).
 * Nó là một Entity JPA, ánh xạ tới bảng "content_likes" trong cơ sở dữ liệu.
 * Entity này ghi lại mối quan hệ Many-to-Many giữa `User` và `SharedContent` với thông tin về thời điểm thích.
 *
 * Logic hoạt động:
 * - Ghi lại hành động "Thích":
 *   - Mỗi `ContentLike` có một ID duy nhất (`id`) và thời điểm tạo (`createdAt`).
 *   - Mục đích chính là theo dõi ai đã thích nội dung nào và ngăn chặn việc thích nhiều lần cùng một nội dung bởi cùng một người dùng.
 * - Cơ chế lưu trữ:
 *   - `@Entity` chỉ định lớp này là một thực thể cơ sở dữ liệu.
 *   - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` từ Lombok giúp giảm boilerplate code.
 * - Mối quan hệ với `SharedContent` và `User`:
 *   - `@ManyToOne` với `SharedContent`: Một lượt thích thuộc về một `SharedContent` duy nhất. Khóa ngoại `shared_content_id` được tạo.
 *   - `@ManyToOne` với `User`: Một lượt thích được tạo bởi một `User` duy nhất. Khóa ngoại `user_id` được tạo.
 *   - `nullable = false` đảm bảo rằng một lượt thích luôn phải liên kết với một nội dung chia sẻ và một người dùng.
 * - Ràng buộc duy nhất (`@UniqueConstraint`):
 *   - `@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"shared_content_id", "user_id"}) })`
 *     đảm bảo rằng mỗi người dùng (`user_id`) chỉ có thể thích một `SharedContent` (`shared_content_id`) một lần duy nhất.
 *     Nếu một người dùng cố gắng thích lại cùng một nội dung, cơ sở dữ liệu sẽ ngăn chặn điều này.
 * - Vòng đời (`@PrePersist`):
 *   - Phương thức `onCreate()` được đánh dấu `@PrePersist` tự động thiết lập `createdAt` là thời điểm hiện tại khi `ContentLike` được lưu vào cơ sở dữ liệu lần đầu.
 *
 * Luồng dữ liệu:
 * - Tạo lượt thích:
 *   - Khi người dùng nhấn nút "Thích" trên một `SharedContent`, một đối tượng `ContentLike` mới được tạo và liên kết với `SharedContent` và `User` hiện có.
 *   - `createdAt` được tự động gán. Đối tượng này sau đó được lưu vào cơ sở dữ liệu. Nếu ràng buộc duy nhất bị vi phạm, hệ thống sẽ xử lý lỗi.
 * - Truy vấn lượt thích:
 *   - Khi truy vấn các lượt thích, các thuộc tính của lượt thích được tải. Các đối tượng `SharedContent` và `User` liên quan được tải khi cần.
 *   - Dữ liệu này được sử dụng để hiển thị số lượt thích cho một `SharedContent` và để xác định liệu người dùng hiện tại đã thích nội dung đó hay chưa.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
    /**
     * Ràng buộc duy nhất trên cặp (`shared_content_id`, `user_id`).
     * Điều này đảm bảo rằng mỗi người dùng chỉ có thể thích một nội dung được chia sẻ một lần duy nhất.
     * Nếu một hành động "Thích" trùng lặp xảy ra, cơ sở dữ liệu sẽ từ chối thao tác này, duy trì tính toàn vẹn của dữ liệu.
     */
    @UniqueConstraint(columnNames = {"shared_content_id", "user_id"})
})
public class ContentLike {

    /**
     * `id` là khóa chính duy nhất cho mỗi lượt thích. Nó được tạo tự động (IDENTITY) bởi cơ sở dữ liệu
     * khi một thực thể `ContentLike` mới được lưu.
     * @Id đánh dấu trường này là khóa chính.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) chỉ định cơ chế tạo khóa chính tự động.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * `sharedContent` là đối tượng `SharedContent` mà lượt thích này thuộc về.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ContentLike` có thể thuộc về một `SharedContent` duy nhất.
     * `@JoinColumn(name = "shared_content_id", nullable = false)` tạo khóa ngoại `shared_content_id` 
     * và đảm bảo rằng mỗi lượt thích phải liên kết với một `SharedContent`.
     */
    @ManyToOne
    @JoinColumn(name = "shared_content_id", nullable = false)
    private SharedContent sharedContent;

    /**
     * `user` là đối tượng `User` đã tạo lượt thích này.
     * Mối quan hệ `@ManyToOne` chỉ ra rằng nhiều `ContentLike` có thể được tạo bởi một `User` duy nhất.
     * `@JoinColumn(name = "user_id", nullable = false)` tạo khóa ngoại `user_id` và đảm bảo rằng 
     * mỗi lượt thích phải có một người dùng tạo ra.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * `createdAt` là thời điểm (ngày và giờ) lượt thích được tạo. 
     * Giá trị này được tự động thiết lập khi đối tượng được lưu vào cơ sở dữ liệu lần đầu tiên thông qua `@PrePersist`.
     */
    private LocalDateTime createdAt;

    /**
     * Phương thức `onCreate()` được gọi tự động trước khi một thực thể `ContentLike` được lưu vào cơ sở dữ liệu lần đầu tiên (`@PrePersist`).
     * Nó thiết lập trường `createdAt` với thời điểm hiện tại của hệ thống, đảm bảo mỗi lượt thích đều có dấu thời gian chính xác.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}