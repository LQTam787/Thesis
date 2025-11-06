package com.nutrition.ai.nutritionaibackend.model.shared;

import com.nutrition.ai.nutritionaibackend.model.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a shared content item in the system.
 * This entity stores information about the content being shared, its owner, visibility, and type.
 *
 * LUỒNG HOẠT ĐỘNG VÀ NGUYÊN LÝ HOẠT ĐỘNG:
 * 1. Đại diện cho bản ghi chính về một nội dung được chia sẻ (ví dụ: một bài đăng).
 * 2. Được sử dụng để theo dõi ai (user) đã chia sẻ cái gì (contentId và contentType) và phạm vi hiển thị (visibility).
 * 3. Các thực thể khác như ContentLike và ContentComment sẽ tham chiếu đến thực thể này.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedContent {

    // Thuộc tính: id
    // Nguyên lý: Khóa chính (Primary Key). Chiến lược tạo giá trị (GenerationType.IDENTITY) để cơ sở dữ liệu tự động tăng.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thuộc tính: user (Chủ sở hữu nội dung)
    // Nguyên lý: Quan hệ nhiều-một (ManyToOne) với thực thể User.
    // user_id là khóa ngoại (JoinColumn) trong bảng SharedContent, không được null.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Thuộc tính: contentId
    // Nguyên lý: ID thực của nội dung gốc (ví dụ: ID của NutritionPlan, ActivityLog, hoặc Recipe).
    // Dùng để liên kết bài chia sẻ này với dữ liệu nghiệp vụ chính.
    @Column(nullable = false)
    private Long contentId;

    // Thuộc tính: contentType
    // Nguyên lý: Định nghĩa loại nội dung được chia sẻ (Enum ContentType).
    // @Enumerated(EnumType.STRING) đảm bảo tên Enum được lưu trữ dưới dạng chuỗi trong CSDL, dễ đọc hơn.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    // Thuộc tính: visibility
    // Nguyên lý: Định nghĩa phạm vi hiển thị của nội dung (Enum Visibility: PUBLIC, FRIENDS).
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    // Thuộc tính: createdAt
    // Nguyên lý: Lưu trữ thời điểm nội dung được tạo/chia sẻ.
    private LocalDateTime createdAt;

    // Vòng đời: PrePersist
    // Nguyên lý: Phương thức @PrePersist được gọi TỰ ĐỘNG trước khi thực thể được lưu lần đầu tiên (insert) vào CSDL.
    // Luồng: Đảm bảo trường createdAt luôn được điền giá trị thời gian hiện tại lúc tạo.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}