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
 * ShareController xử lý các yêu cầu HTTP liên quan đến tính năng chia sẻ nội dung, bao gồm chia sẻ, thích và bình luận.
 * <p>
 * Nguyên lý hoạt động: Controller này cung cấp các RESTful endpoint để người dùng tương tác với các tính năng xã hội của ứng dụng.
 * Nó ủy quyền logic nghiệp vụ cho {@link com.nutrition.ai.nutritionaibackend.service.ShareService}.
 * Lưu ý quan trọng: Hiện tại, {@code userId} đang sử dụng giá trị placeholder ({@code 1L}) thay vì trích xuất từ {@link UserDetails} thực tế.
 * Trong một triển khai sản phẩm, {@code userId} phải được lấy một cách an toàn từ thông tin xác thực của người dùng hiện tại.
 * </p>
 * <p>
 * Luồng hoạt động:
 * <ul>
 *     <li>Nhận các yêu cầu HTTP (POST, GET) từ client tại đường dẫn cơ sở {@code /api/share}.</li>
 *     <li>Trích xuất dữ liệu cần thiết từ {@code @RequestBody} hoặc {@code @PathVariable}.</li>
 *     <li>Sử dụng {@code @AuthenticationPrincipal UserDetails} để có được thông tin người dùng đã xác thực (mặc dù hiện đang dùng placeholder cho userId).</li>
 *     <li>Chuyển đổi các chuỗi thành các kiểu dữ liệu Enum thích hợp như {@link ContentType} và {@link Visibility}.</li>
 *     <li>Ủy quyền các yêu cầu xử lý nghiệp vụ cho {@code ShareService}.</li>
 *     <li>Trả về {@code ResponseEntity} với dữ liệu {@link SharedContentDto}, {@link ContentCommentDto},
 *         hoặc danh sách các DTO, cùng với trạng thái HTTP thích hợp (ví dụ: 200 OK, 201 CREATED).</li>
 * </ul>
 * </p>
 */
@RestController // Đánh dấu lớp này là một Spring REST Controller
@RequestMapping("/api/share") // Ánh xạ với đường dẫn cơ sở /api/share
@Tag(name = "Content Sharing", description = "Endpoints for sharing, liking, and commenting on content") // Tài liệu Swagger
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
@RequiredArgsConstructor // Tạo constructor cho các trường final (ShareService)
public class ShareController {

    private final ShareService shareService; // Dependency Injection

    /**
     * Cho phép người dùng chia sẻ một nội dung (ví dụ: công thức nấu ăn, kế hoạch dinh dưỡng) lên hệ thống.
     * <p>
     * Endpoint này nhận ID nội dung, loại nội dung và mức độ hiển thị từ payload.
     * Nó sử dụng {@link UserDetails} để xác định người dùng đang thực hiện hành động chia sẻ
     * và sau đó gọi {@link ShareService} để lưu thông tin chia sẻ.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/share} với payload chứa {@code contentId}, {@code contentType}, và {@code visibility}.</li>
     *     <li>Trích xuất {@code userId} từ {@code UserDetails} (hiện đang dùng placeholder {@code 1L}).</li>
     *     <li>Phân tích {@code contentId} thành {@code Long}, {@code contentType} thành {@link ContentType}, và {@code visibility} thành {@link Visibility}.</li>
     *     <li>Gọi phương thức {@code shareService.shareContent(userId, contentId, contentType, visibility)}.</li>
     *     <li>Trả về {@link SharedContentDto} của nội dung đã chia sẻ với trạng thái HTTP 201 CREATED.</li>
     * </ol>
     * </p>
     * @param userDetails Chi tiết người dùng đã xác thực, được cung cấp bởi Spring Security.
     * @param payload Map chứa các trường: "contentId" (ID của nội dung), "contentType" (loại nội dung, ví dụ: RECIPE, NUTRITION_PLAN),
     *                và "visibility" (mức độ hiển thị, ví dụ: PUBLIC, PRIVATE).
     * @return ResponseEntity chứa {@link SharedContentDto} của nội dung đã chia sẻ.
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
     * Lấy danh sách tất cả các nội dung được chia sẻ công khai trong hệ thống.
     * <p>
     * Endpoint này cho phép bất kỳ người dùng nào truy xuất danh sách các mục nội dung
     * đã được chia sẻ với mức độ hiển thị {@code PUBLIC}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/share/public}.</li>
     *     <li>Gọi phương thức {@code shareService.getAllPublicContent()}.</li>
     *     <li>Trả về danh sách {@link SharedContentDto} của tất cả nội dung công khai với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @return ResponseEntity chứa danh sách {@link SharedContentDto} của nội dung được chia sẻ công khai.
     */
    @Operation(summary = "Get public content", description = "Retrieves a feed of all publicly shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved public content")
    @GetMapping("/public") // Ánh xạ với GET /api/share/public
    public ResponseEntity<List<SharedContentDto>> getPublicContent() {
        List<SharedContentDto> publicContent = shareService.getAllPublicContent();
        return ResponseEntity.ok(publicContent);
    }

    /**
     * Cho phép người dùng đã xác thực thích một nội dung đã được chia sẻ.
     * <p>
     * Endpoint này ghi lại một lượt thích cho nội dung được chỉ định bởi {@code contentId},
     * liên kết với người dùng đang thực hiện yêu cầu.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/share/{contentId}/like}.</li>
     *     <li>Trích xuất {@code contentId} từ đường dẫn và {@code userId} từ {@code UserDetails} (hiện đang dùng placeholder {@code 1L}).</li>
     *     <li>Gọi phương thức {@code shareService.likeContent(contentId, userId)}.</li>
     *     <li>Trả về {@code ResponseEntity} rỗng với trạng thái HTTP 200 OK để xác nhận lượt thích.</li>
     * </ol>
     * </p>
     * @param contentId ID của nội dung được chia sẻ mà người dùng muốn thích.
     * @param userDetails Chi tiết người dùng đã xác thực.
     * @return ResponseEntity rỗng với trạng thái HTTP 200 OK.
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
     * Cho phép người dùng đã xác thực bình luận về một nội dung đã được chia sẻ.
     * <p>
     * Endpoint này nhận nội dung bình luận từ payload và tạo một bình luận mới
     * cho nội dung được chỉ định bởi {@code contentId}, liên kết với người dùng đang bình luận.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu POST đến {@code /api/share/{contentId}/comment} với payload chứa trường "text".</li>
     *     <li>Trích xuất {@code contentId} từ đường dẫn và {@code userId} từ {@code UserDetails} (hiện đang dùng placeholder {@code 1L}).</li>
     *     <li>Trích xuất nội dung bình luận ({@code text}) từ payload.</li>
     *     <li>Gọi phương thức {@code shareService.commentOnContent(contentId, userId, text)}.</li>
     *     <li>Trả về {@link ContentCommentDto} của bình luận đã tạo với trạng thái HTTP 201 CREATED.</li>
     * </ol>
     * </p>
     * @param contentId ID của nội dung được chia sẻ mà người dùng muốn bình luận.
     * @param userDetails Chi tiết người dùng đã xác thực.
     * @param payload Map chứa trường "text" với nội dung bình luận.
     * @return ResponseEntity chứa {@link ContentCommentDto} của bình luận đã tạo.
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
     * Lấy tất cả các bình luận cho một nội dung đã được chia sẻ cụ thể.
     * <p>
     * Endpoint này truy xuất danh sách tất cả các bình luận liên quan đến một mục nội dung
     * được chia sẻ, được xác định bởi {@code contentId}.
     * </p>
     * <p>
     * Luồng hoạt động:
     * <ol>
     *     <li>Nhận yêu cầu GET đến {@code /api/share/{contentId}/comments}.</li>
     *     <li>Trích xuất {@code contentId} từ đường dẫn.</li>
     *     <li>Gọi phương thức {@code shareService.getCommentsForContent(contentId)}.</li>
     *     <li>Trả về danh sách {@link ContentCommentDto} của các bình luận với trạng thái HTTP 200 OK.</li>
     * </ol>
     * </p>
     * @param contentId ID của nội dung được chia sẻ để lấy bình luận.
     * @return ResponseEntity chứa danh sách {@link ContentCommentDto} của các bình luận.
     */
    @Operation(summary = "Get comments for content", description = "Retrieves all comments for a specific piece of shared content.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments")
    @GetMapping("/{contentId}/comments") // Ánh xạ với GET /api/share/{contentId}/comments
    public ResponseEntity<List<ContentCommentDto>> getCommentsForContent(@PathVariable Long contentId) {
        List<ContentCommentDto> comments = shareService.getCommentsForContent(contentId);
        return ResponseEntity.ok(comments);
    }
}