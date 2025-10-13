package com.nutrition.ai.nutritionaibackend.controller;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import com.nutrition.ai.nutritionaibackend.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling content sharing, liking, and commenting.
 * Exposes endpoints for clients to interact with the sharing features.
 */
@RestController
@RequestMapping("/api/share")
@Tag(name = "Content Sharing", description = "Endpoints for sharing, liking, and commenting on content")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @Operation(summary = "Share content", description = "Allows a user to share a piece of content, like a recipe or a nutrition plan.")
    @ApiResponse(responseCode = "201", description = "Content shared successfully")
    @PostMapping
    public ResponseEntity<SharedContentDto> shareContent(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> payload) {
        // This is a simplified way to get user ID. In a real app, you'd have a more robust way.
        Long userId = 1L; // Placeholder
        Long contentId = Long.parseLong(payload.get("contentId"));
        ContentType contentType = ContentType.valueOf(payload.get("contentType"));
        Visibility visibility = Visibility.valueOf(payload.get("visibility"));

        SharedContentDto sharedContent = shareService.shareContent(userId, contentId, contentType, visibility);
        return new ResponseEntity<>(sharedContent, HttpStatus.CREATED);
    }

    @Operation(summary = "Get public content", description = "Retrieves a feed of all publicly shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved public content")
    @GetMapping("/public")
    public ResponseEntity<List<SharedContentDto>> getPublicContent() {
        List<SharedContentDto> publicContent = shareService.getAllPublicContent();
        return ResponseEntity.ok(publicContent);
    }

    @Operation(summary = "Like content", description = "Allows a user to like a piece of shared content.")
    @ApiResponse(responseCode = "200", description = "Content liked successfully")
    @PostMapping("/{contentId}/like")
    public ResponseEntity<Void> likeContent(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Placeholder
        shareService.likeContent(contentId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Comment on content", description = "Allows a user to add a comment to a piece of shared content.")
    @ApiResponse(responseCode = "201", description = "Comment added successfully")
    @PostMapping("/{contentId}/comment")
    public ResponseEntity<ContentCommentDto> commentOnContent(@PathVariable Long contentId,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody Map<String, String> payload) {
        Long userId = 1L; // Placeholder
        String text = payload.get("text");
        ContentCommentDto comment = shareService.commentOnContent(contentId, userId, text);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @Operation(summary = "Get comments for content", description = "Retrieves all comments for a specific piece of shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments")
    @GetMapping("/{contentId}/comments")
    public ResponseEntity<List<ContentCommentDto>> getCommentsForContent(@PathVariable Long contentId) {
        List<ContentCommentDto> comments = shareService.getCommentsForContent(contentId);
        return ResponseEntity.ok(comments);
    }
}
