package com.nutrition.ai.nutritionaibackend.dto;

import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing shared content.
 * This object is used to transfer shared content data between the server and clients.
 * Nguyên lý hoạt động: Đóng gói thông tin metadata (siêu dữ liệu) về một nội dung bất kỳ được người dùng chia sẻ (ví dụ: một bữa ăn, một mục tiêu đạt được).
 * Luồng hoạt động: Được sử dụng để hiển thị các bài đăng trên bảng tin xã hội, bao gồm cả số lượng tương tác (like, comment).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedContentDto {
    private Long id; // ID duy nhất của nội dung được chia sẻ.
    private Long userId; // ID của người dùng đã chia sẻ.
    private String username; // Tên người dùng của người đã chia sẻ.
    private Long contentId; // ID của nội dung gốc (ví dụ: MealDto, GoalDto) được chia sẻ.
    private ContentType contentType; // Loại nội dung gốc (ví dụ: MEAL, GOAL).
    private Visibility visibility; // Quyền riêng tư/Khả năng hiển thị (ví dụ: PUBLIC, FOLLOWERS_ONLY).
    private LocalDateTime createdAt; // Thời gian chia sẻ.
    private long likeCount; // Số lượng lượt "Thích". Nguyên lý hoạt động: Thường là một trường tổng hợp (aggregate field) từ bảng ContentLike.
    private long commentCount; // Số lượng bình luận. Nguyên lý hoạt động: Thường là một trường tổng hợp từ bảng ContentComment.
}