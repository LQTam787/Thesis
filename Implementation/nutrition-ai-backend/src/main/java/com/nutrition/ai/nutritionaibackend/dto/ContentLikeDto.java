package com.nutrition.ai.nutritionaibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for representing a content like.
 * Used to transfer information about who liked what content and when.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentLikeDto {
    private Long id;
    private Long sharedContentId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}
