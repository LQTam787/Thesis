package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing a content like.
 * Used to transfer information about who liked what content and when.
 * Nguyên lý hoạt động: Đóng gói dữ liệu về hành động "Thích" (Like) đối với một nội dung được chia sẻ.
 * Luồng hoạt động: Được sử dụng để gửi một hành động "Thích" mới đến server hoặc đếm/liệt kê số lượt thích.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentLikeDto {
    private Long id; // ID duy nhất của bản ghi "Thích".
    private Long sharedContentId; // ID của nội dung được chia sẻ đã được "Thích".
    private Long userId; // ID của người dùng đã thực hiện hành động "Thích".
    private String username; // Tên người dùng của người đã "Thích" (thường dùng để hiển thị).
    private LocalDateTime createdAt; // Thời gian hành động "Thích" được thực hiện.
}