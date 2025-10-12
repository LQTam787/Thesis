package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing a comment on shared content.
 * Transfers comment data, including the author and the comment text.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCommentDto {
    private Long id;
    private Long sharedContentId;
    private Long userId;
    private String username;
    private String text;
    private LocalDateTime createdAt;
}
