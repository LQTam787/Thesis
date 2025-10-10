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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedContentDto {
    private Long id;
    private Long userId;
    private String username;
    private Long contentId;
    private ContentType contentType;
    private Visibility visibility;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
}
