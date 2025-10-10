package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import java.util.List;

/**
 * Service interface for handling content sharing functionalities.
 * Defines methods for sharing, retrieving, liking, and commenting on content.
 */
public interface ShareService {

    /**
     * Shares a new piece of content.
     *
     * @param userId The ID of the user sharing the content.
     * @param contentId The ID of the content being shared.
     * @param contentType The type of the content.
     * @param visibility The visibility level of the shared content.
     * @return The DTO of the newly shared content.
     */
    SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility);

    /**
     * Retrieves all shared content with public visibility.
     *
     * @return A list of shared content DTOs.
     */
    List<SharedContentDto> getAllPublicContent();

    /**
     * Likes a piece of shared content.
     *
     * @param sharedContentId The ID of the shared content to like.
     * @param userId The ID of the user who is liking the content.
     */
    void likeContent(Long sharedContentId, Long userId);

    /**
     * Adds a comment to a piece of shared content.
     *
     * @param sharedContentId The ID of the shared content to comment on.
     * @param userId The ID of the user who is commenting.
     * @param text The comment text.
     * @return The DTO of the newly created comment.
     */
    ContentCommentDto commentOnContent(Long sharedContentId, Long userId, String text);

    /**
     * Retrieves all comments for a specific shared content.
     *
     * @param sharedContentId The ID of the shared content.
     * @return A list of comment DTOs.
     */
    List<ContentCommentDto> getCommentsForContent(Long sharedContentId);
}
