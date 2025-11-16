package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.ContentType;
import com.nutrition.ai.nutritionaibackend.model.shared.Visibility;
import java.util.List;

/**
 * Service interface cho việc xử lý các chức năng chia sẻ nội dung trong ứng dụng.
 * Giao diện này định nghĩa các phương thức cho phép người dùng thực hiện các hành động
 * như chia sẻ nội dung (công thức, kế hoạch dinh dưỡng), xem các nội dung được chia sẻ,
 * thích (like) một nội dung, và thêm bình luận (comment) vào nội dung đó.
 * Nó cung cấp một tầng trừu tượng cho logic nghiệp vụ liên quan đến các tính năng xã hội này.
 */
public interface ShareService {

    /**
     * Chia sẻ một nội dung mới bởi một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của người dùng ({@code userId}), ID của nội dung gốc ({@code contentId}),
     *         loại nội dung ({@code contentType}), và độ hiển thị ({@code visibility}).</li>
     *     <li>Tạo một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.shared.SharedContent} mới
     *         với các thông tin này và liên kết nó với người dùng.</li>
     *     <li>Lưu đối tượng {@code SharedContent} mới vào cơ sở dữ liệu.</li>
     *     <li>Trả về một đối tượng {@link SharedContentDto} của nội dung đã được chia sẻ thành công.</li>
     * </ol>
     *
     * @param userId ID của người dùng chia sẻ nội dung.
     * @param contentId ID của nội dung gốc đang được chia sẻ (ví dụ: ID của một Recipe hoặc NutritionPlan).
     * @param contentType Loại nội dung được chia sẻ (ví dụ: {@code ContentType.RECIPE}, {@code ContentType.PLAN}).
     * @param visibility Mức độ hiển thị của nội dung chia sẻ (ví dụ: {@code Visibility.PUBLIC}, {@code Visibility.PRIVATE}).
     * @return {@link SharedContentDto} đại diện cho nội dung đã được chia sẻ.
     */
    SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility);

    /**
     * Truy xuất tất cả nội dung được chia sẻ có độ hiển thị là {@code PUBLIC}.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận yêu cầu để lấy danh sách tất cả nội dung công khai.</li>
     *     <li>Truy vấn cơ sở dữ liệu để tìm tất cả các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.shared.SharedContent}
     *         mà có thuộc tính {@code visibility} là {@code PUBLIC}.</li>
     *     <li>Chuyển đổi các đối tượng Entity này thành danh sách các {@link SharedContentDto} và trả về.</li>
     * </ol>
     *
     * @return Danh sách các {@link SharedContentDto} chứa tất cả nội dung công khai.
     */
    List<SharedContentDto> getAllPublicContent();

    /**
     * Ghi lại hành động "Thích" (Like) của một người dùng đối với một nội dung đã chia sẻ cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của nội dung được chia sẻ ({@code sharedContentId}) và ID của người dùng ({@code userId}).</li>
     *     <li>Kiểm tra sự tồn tại của {@code SharedContent} và {@code User} tương ứng.</li>
     *     <li>Tạo một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.shared.ContentLike} mới,
     *         liên kết nó với {@code SharedContent} và {@code User}.</li>
     *     <li>Lưu đối tượng {@code ContentLike} vào cơ sở dữ liệu.</li>
     * </ol>
     * <p>Lưu ý: Triển khai cụ thể cần xử lý trường hợp người dùng đã thích nội dung này trước đó để tránh trùng lặp.</p>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ mà người dùng muốn thích.
     * @param userId ID của người dùng thực hiện thao tác thích.
     */
    void likeContent(Long sharedContentId, Long userId);

    /**
     * Thêm một bình luận vào một nội dung đã chia sẻ bởi một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của nội dung được chia sẻ ({@code sharedContentId}), ID của người dùng ({@code userId}),
     *         và nội dung văn bản của bình luận ({@code text}).</li>
     *     <li>Kiểm tra sự tồn tại của {@code SharedContent} và {@code User} tương ứng.</li>
     *     <li>Tạo một đối tượng {@link com.nutrition.ai.nutritionaibackend.model.shared.ContentComment} mới
     *         với các thông tin này và liên kết nó với {@code SharedContent} và {@code User}.</li>
     *     <li>Lưu đối tượng {@code ContentComment} mới vào cơ sở dữ liệu.</li>
     *     <li>Trả về một đối tượng {@link ContentCommentDto} của bình luận đã được tạo.</li>
     * </ol>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ mà người dùng muốn bình luận.
     * @param userId ID của người dùng thực hiện bình luận.
     * @param text Nội dung văn bản của bình luận.
     * @return {@link ContentCommentDto} đại diện cho bình luận đã được tạo.
     */
    ContentCommentDto commentOnContent(Long sharedContentId, Long userId, String text);

    /**
     * Truy xuất tất cả bình luận cho một nội dung đã chia sẻ cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li>Nhận ID của nội dung được chia sẻ ({@code sharedContentId}).</li>
     *     <li>Truy vấn cơ sở dữ liệu để tìm tất cả các đối tượng {@link com.nutrition.ai.nutritionaibackend.model.shared.ContentComment}
     *         được liên kết với {@code sharedContentId} đã cho.</li>
     *     <li>Chuyển đổi các đối tượng Entity này thành danh sách các {@link ContentCommentDto} và trả về.</li>
     * </ol>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ để lấy các bình luận.
     * @return Danh sách các {@link ContentCommentDto} chứa các bình luận cho nội dung đã chia sẻ.
     */
    List<ContentCommentDto> getCommentsForContent(Long sharedContentId);
}