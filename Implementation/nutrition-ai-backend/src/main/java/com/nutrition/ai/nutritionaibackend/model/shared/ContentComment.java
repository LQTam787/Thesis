package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a comment on a shared content item.
 * This entity stores the comment text, the author, and the associated shared content.
 *
 * LUỒNG HOẠT ĐỘNG VÀ NGUYÊN LÝ HOẠT ĐỘNG:
 * 1. Ghi lại nội dung bình luận (text) của một User đối với một SharedContent.
 * 2. Nguyên lý: Khác với ContentLike, một User có thể bình luận nhiều lần trên cùng một SharedContent.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentComment {

    // Thuộc tính: id (Khóa chính)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thuộc tính: sharedContent
    // Nguyên lý: Khóa ngoại trỏ đến SharedContent được Bình luận (ManyToOne).
    @ManyToOne
    @JoinColumn(name = "shared_content_id", nullable = false)
    private SharedContent sharedContent;

    // Thuộc tính: user (Người Bình luận)
    // Nguyên lý: Khóa ngoại trỏ đến User đã thực hiện hành động Bình luận (ManyToOne).
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Thuộc tính: text
    // Nguyên lý: Nội dung của bình luận. columnDefinition = "TEXT" cho phép lưu trữ chuỗi dài hơn.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    // Thuộc tính: createdAt (Thời điểm Bình luận)
    private LocalDateTime createdAt;

    // Vòng đời: PrePersist
    // Luồng: Tự động đặt thời gian tạo khi bản ghi Comment được lưu lần đầu.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}