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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling content sharing, liking, and commenting.
 * Exposes endpoints for clients to interact with the sharing features.
 */
@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    /**
     * Endpoint to share a new piece of content.
     */
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

    /**
     * Endpoint to get all public shared content.
     */
    @GetMapping("/public")
    public ResponseEntity<List<SharedContentDto>> getPublicContent() {
        List<SharedContentDto> publicContent = shareService.getAllPublicContent();
        return ResponseEntity.ok(publicContent);
    }

    /**
     * Endpoint to like a piece of shared content.
     */
    @PostMapping("/{contentId}/like")
    public ResponseEntity<Void> likeContent(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Placeholder
        shareService.likeContent(contentId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to add a comment to a piece of shared content.
     */
    @PostMapping("/{contentId}/comment")
    public ResponseEntity<ContentCommentDto> commentOnContent(@PathVariable Long contentId,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody Map<String, String> payload) {
        Long userId = 1L; // Placeholder
        String text = payload.get("text");
        ContentCommentDto comment = shareService.commentOnContent(contentId, userId, text);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all comments for a piece of shared content.
     */
    @GetMapping("/{contentId}/comments")
    public ResponseEntity<List<ContentCommentDto>> getCommentsForContent(@PathVariable Long contentId) {
        List<ContentCommentDto> comments = shareService.getCommentsForContent(contentId);
        return ResponseEntity.ok(comments);
    }
}
