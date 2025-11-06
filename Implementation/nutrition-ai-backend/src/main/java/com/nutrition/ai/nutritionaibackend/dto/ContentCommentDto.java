package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing a comment on shared content.
 * Transfers comment data, including the author and the comment text.
 * Nguyên lý hoạt động: Đóng gói dữ liệu bình luận trên nội dung được chia sẻ.
 * Luồng hoạt động: Được sử dụng để gửi bình luận mới đến server hoặc hiển thị các bình luận hiện có cho người dùng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCommentDto {
    private Long id; // ID duy nhất của bình luận.
    private Long sharedContentId; // ID của nội dung được chia sẻ mà bình luận này thuộc về.
    private Long userId; // ID của người dùng đã tạo bình luận.
    private String username; // Tên người dùng của tác giả bình luận (thường dùng để hiển thị).
    private String text; // Nội dung văn bản của bình luận.
    private LocalDateTime createdAt; // Thời gian bình luận được tạo.
}