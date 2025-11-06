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
 * ShareController xử lý việc chia sẻ nội dung, thích và bình luận.
 * Nguyên lý hoạt động: Cung cấp các REST endpoint để người dùng tương tác với các tính năng xã hội của ứng dụng.
 * Lưu ý: 'userId' đang sử dụng giá trị placeholder (1L) thay vì trích xuất từ UserDetails thực tế.
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/share") // Ánh xạ với đường dẫn cơ sở /api/share
@Tag(name = "Content Sharing", description = "Endpoints for sharing, liking, and commenting on content") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
@RequiredArgsConstructor // Tạo constructor cho các trường final (ShareService)
public class ShareController {

    private final ShareService shareService; // Dependency Injection

    /**
     * Cho phép người dùng chia sẻ một nội dung (ví dụ: công thức, kế hoạch).
     * Luồng hoạt động:
     * 1. Nhận UserDetails từ token (AuthenticationPrincipal) và payload (contentId, contentType, visibility).
     * 2. (Giả định) Lấy userId từ UserDetails.
     * 3. Chuyển đổi các chuỗi trong payload thành các kiểu dữ liệu thích hợp (Long, Enum).
     * 4. Gọi shareService.shareContent() để xử lý logic chia sẻ.
     * 5. Trả về SharedContentDto đã tạo với HTTP status 201 (CREATED).
     *
     * @param userDetails Chi tiết người dùng đã xác thực.
     * @param payload Map chứa contentId, contentType, visibility.
     * @return ResponseEntity chứa SharedContentDto.
     */
    @Operation(summary = "Share content", description = "Allows a user to share a piece of content, like a recipe or a nutrition plan.")
    @ApiResponse(responseCode = "201", description = "Content shared successfully")
    @PostMapping // Ánh xạ với POST /api/share
    public ResponseEntity<SharedContentDto> shareContent(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> payload) {
        // Đây là một placeholder, trong thực tế sẽ lấy từ UserDetails
        Long userId = 1L;
        Long contentId = Long.parseLong(payload.get("contentId"));
        ContentType contentType = ContentType.valueOf(payload.get("contentType"));
        Visibility visibility = Visibility.valueOf(payload.get("visibility"));

        SharedContentDto sharedContent = shareService.shareContent(userId, contentId, contentType, visibility);
        return new ResponseEntity<>(sharedContent, HttpStatus.CREATED);
    }

    /**
     * Lấy danh sách tất cả nội dung được chia sẻ công khai.
     * Luồng hoạt động:
     * 1. Gọi shareService.getAllPublicContent().
     * 2. Trả về danh sách SharedContentDto với HTTP status 200 (OK).
     *
     * @return ResponseEntity chứa danh sách SharedContentDto.
     */
    @Operation(summary = "Get public content", description = "Retrieves a feed of all publicly shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved public content")
    @GetMapping("/public") // Ánh xạ với GET /api/share/public
    public ResponseEntity<List<SharedContentDto>> getPublicContent() {
        List<SharedContentDto> publicContent = shareService.getAllPublicContent();
        return ResponseEntity.ok(publicContent);
    }

    /**
     * Cho phép người dùng thích một nội dung đã chia sẻ.
     * Luồng hoạt động:
     * 1. Nhận 'contentId' từ path variable và UserDetails.
     * 2. (Giả định) Lấy userId từ UserDetails.
     * 3. Gọi shareService.likeContent(contentId, userId) để ghi lại lượt thích.
     * 4. Trả về HTTP status 200 (OK) rỗng.
     *
     * @param contentId ID của nội dung được thích.
     * @param userDetails Chi tiết người dùng đã xác thực.
     * @return ResponseEntity rỗng với status 200.
     */
    @Operation(summary = "Like content", description = "Allows a user to like a piece of shared content.")
    @ApiResponse(responseCode = "200", description = "Content liked successfully")
    @PostMapping("/{contentId}/like") // Ánh xạ với POST /api/share/{contentId}/like
    public ResponseEntity<Void> likeContent(@PathVariable Long contentId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Placeholder
        shareService.likeContent(contentId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Cho phép người dùng bình luận về một nội dung đã chia sẻ.
     * Luồng hoạt động:
     * 1. Nhận 'contentId', UserDetails và payload (text).
     * 2. (Giả định) Lấy userId và text.
     * 3. Gọi shareService.commentOnContent() để lưu bình luận.
     * 4. Trả về ContentCommentDto đã tạo với HTTP status 201 (CREATED).
     *
     * @param contentId ID của nội dung.
     * @param userDetails Chi tiết người dùng đã xác thực.
     * @param payload Map chứa nội dung bình luận ('text').
     * @return ResponseEntity chứa ContentCommentDto.
     */
    @Operation(summary = "Comment on content", description = "Allows a user to add a comment to a piece of shared content.")
    @ApiResponse(responseCode = "201", description = "Comment added successfully")
    @PostMapping("/{contentId}/comment") // Ánh xạ với POST /api/share/{contentId}/comment
    public ResponseEntity<ContentCommentDto> commentOnContent(@PathVariable Long contentId,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody Map<String, String> payload) {
        Long userId = 1L; // Placeholder
        String text = payload.get("text");
        ContentCommentDto comment = shareService.commentOnContent(contentId, userId, text);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Lấy tất cả bình luận cho một nội dung đã chia sẻ.
     * Luồng hoạt động:
     * 1. Nhận 'contentId'.
     * 2. Gọi shareService.getCommentsForContent(contentId).
     * 3. Trả về danh sách ContentCommentDto với HTTP status 200 (OK).
     *
     * @param contentId ID của nội dung.
     * @return ResponseEntity chứa danh sách ContentCommentDto.
     */
    @Operation(summary = "Get comments for content", description = "Retrieves all comments for a specific piece of shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments")
    @GetMapping("/{contentId}/comments") // Ánh xạ với GET /api/share/{contentId}/comments
    public ResponseEntity<List<ContentCommentDto>> getCommentsForContent(@PathVariable Long contentId) {
        List<ContentCommentDto> comments = shareService.getCommentsForContent(contentId);
        return ResponseEntity.ok(comments);
    }
}