package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import java.util.List;

/**
 * Service interface for handling content sharing functionalities.
 * Defines methods for sharing, retrieving, liking, and commenting on content.
 * Cho phép người dùng chia sẻ các nội dung như công thức hoặc kế hoạch dinh dưỡng.
 */
public interface ShareService {

    /**
     * Luồng hoạt động: Tạo một mục nội dung được chia sẻ mới.
     * Nguyên lý hoạt động: Lấy thông tin nội dung gốc (`contentId`, `contentType`), liên kết với người dùng (`userId`) và cấp độ hiển thị (`visibility`), sau đó lưu đối tượng SharedContent mới vào cơ sở dữ liệu.
     *
     * @param userId The ID of the user sharing the content.
     * @param contentId The ID of the content being shared.
     * @param contentType The type of the content.
     * @param visibility The visibility level of the shared content.
     * @return The DTO of the newly shared content.
     */
    SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility);

    /**
     * Luồng hoạt động: Truy xuất tất cả nội dung được chia sẻ công khai.
     * Nguyên lý hoạt động: Truy vấn cơ sở dữ liệu tìm các bản ghi SharedContent với `visibility` là PUBLIC.
     *
     * @return A list of shared content DTOs.
     */
    List<SharedContentDto> getAllPublicContent();

    /**
     * Luồng hoạt động: Ghi lại hành động "Thích" (Like) của người dùng đối với nội dung được chia sẻ.
     * Nguyên lý hoạt động: Tạo hoặc cập nhật một bản ghi Like liên kết `sharedContentId` và `userId`.
     *
     * @param sharedContentId The ID of the shared content to like.
     * @param userId The ID of the user who is liking the content.
     */
    void likeContent(Long sharedContentId, Long userId);

    /**
     * Luồng hoạt động: Thêm một bình luận vào nội dung được chia sẻ.
     * Nguyên lý hoạt động: Tạo một bản ghi Comment mới với `sharedContentId`, `userId` và `text`, sau đó lưu vào cơ sở dữ liệu.
     *
     * @param sharedContentId The ID of the shared content to comment on.
     * @param userId The ID of the user who is commenting.
     * @param text The comment text.
     * @return The DTO of the newly created comment.
     */
    ContentCommentDto commentOnContent(Long sharedContentId, Long userId, String text);

    /**
     * Luồng hoạt động: Truy xuất tất cả bình luận cho một nội dung được chia sẻ cụ thể.
     * Nguyên lý hoạt động: Truy vấn các bản ghi Comment được liên kết với `sharedContentId`.
     *
     * @param sharedContentId The ID of the shared content.
     * @return A list of comment DTOs.
     */
    List<ContentCommentDto> getCommentsForContent(Long sharedContentId);
}