package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a "like" on a shared content item.
 * This entity links a user to a shared content they have liked.
 *
 * LUỒNG HOẠT ĐỘNG VÀ NGUYÊN LÝ HOẠT ĐỘNG:
 * 1. Ghi lại hành động "Thích" (Like) của một User đối với một SharedContent cụ thể.
 * 2. Nguyên lý: Đảm bảo MỘT người dùng chỉ có thể Thích MỘT SharedContent MỘT lần duy nhất.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
    // Ràng buộc duy nhất (Unique Constraint)
    // Nguyên lý: Đảm bảo không có hai bản ghi nào có cùng cặp (shared_content_id, user_id).
    // Luồng: Nếu User A cố gắng thích SharedContent B lần thứ hai, CSDL sẽ báo lỗi (hoặc cần được xử lý ở tầng dịch vụ).
    @UniqueConstraint(columnNames = {"shared_content_id", "user_id"})
})
public class ContentLike {

    // Thuộc tính: id (Khóa chính)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thuộc tính: sharedContent
    // Nguyên lý: Khóa ngoại trỏ đến SharedContent được Thích (ManyToOne).
    @ManyToOne
    @JoinColumn(name = "shared_content_id", nullable = false)
    private SharedContent sharedContent;

    // Thuộc tính: user (Người Thích)
    // Nguyên lý: Khóa ngoại trỏ đến User đã thực hiện hành động Thích (ManyToOne).
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Thuộc tính: createdAt (Thời điểm Thích)
    private LocalDateTime createdAt;

    // Vòng đời: PrePersist
    // Luồng: Tự động đặt thời gian tạo khi bản ghi Like được lưu lần đầu.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}