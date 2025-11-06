package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.*;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.*;
import com.nutrition.ai.nutritionaibackend.service.ShareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ShareService interface.
 * Handles the business logic for sharing, liking, and commenting on content.
 * Lớp này triển khai giao diện ShareService, xử lý logic nghiệp vụ cho việc chia sẻ, thích (like)
 * và bình luận (comment) về nội dung.
 */
@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    // Inject các Repository cần thiết để tương tác với cơ sở dữ liệu
    private final SharedContentRepository sharedContentRepository;
    private final ContentLikeRepository contentLikeRepository;
    private final ContentCommentRepository contentCommentRepository;
    private final UserRepository userRepository;
    // Inject ModelMapper để chuyển đổi giữa Entity và DTO
    private final ModelMapper modelMapper;

    /**
     * Chia sẻ một nội dung mới.
     * 1. Tìm User bằng userId. Ném EntityNotFoundException nếu không tìm thấy.
     * 2. Tạo đối tượng SharedContent mới với thông tin được cung cấp.
     * 3. Lưu SharedContent vào cơ sở dữ liệu.
     * 4. Chuyển đổi SharedContent đã lưu thành SharedContentDto và trả về.
     *
     * @param userId ID của người dùng chia sẻ.
     * @param contentId ID của nội dung được chia sẻ (ví dụ: ID của công thức, kế hoạch).
     * @param contentType Loại nội dung (ví dụ: RECIPE, PLAN).
     * @param visibility Độ hiển thị (ví dụ: PUBLIC, PRIVATE).
     * @return SharedContentDto của nội dung đã được chia sẻ.
     */
    @Override
    public SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility) {
        // 1. Tìm người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 2. Xây dựng đối tượng SharedContent
        SharedContent sharedContent = SharedContent.builder()
                .user(user) // Gắn User Entity vào
                .contentId(contentId)
                .contentType(contentType)
                .visibility(visibility)
                .build();

        // 3. Lưu vào cơ sở dữ liệu
        SharedContent savedContent = sharedContentRepository.save(sharedContent);
        // 4. Chuyển đổi và trả về DTO
        return convertToDto(savedContent);
    }

    /**
     * Lấy tất cả nội dung đã chia sẻ có độ hiển thị là PUBLIC.
     * 1. Lấy tất cả SharedContent từ cơ sở dữ liệu.
     * 2. Lọc ra những nội dung có Visibility là PUBLIC.
     * 3. Chuyển đổi từng Entity thành SharedContentDto.
     * 4. Thu thập kết quả thành một danh sách.
     *
     * @return List<SharedContentDto> chứa tất cả nội dung công khai.
     */
    @Override
    public List<SharedContentDto> getAllPublicContent() {
        return sharedContentRepository.findAll().stream()
                .filter(sc -> sc.getVisibility() == Visibility.PUBLIC) // 2. Lọc theo PUBLIC
                .map(this::convertToDto) // 3. Chuyển đổi sang DTO (bao gồm cả count likes/comments)
                .collect(Collectors.toList()); // 4. Thu thập kết quả
    }

    /**
     * Thích (like) một nội dung đã chia sẻ.
     * 1. Tìm SharedContent bằng sharedContentId. Ném EntityNotFoundException nếu không tìm thấy.
     * 2. Tìm User bằng userId. Ném EntityNotFoundException nếu không tìm thấy.
     * 3. Tạo đối tượng ContentLike mới, liên kết với SharedContent và User.
     * 4. Lưu ContentLike vào cơ sở dữ liệu.
     *
     * @param sharedContentId ID của nội dung đã chia sẻ.
     * @param userId ID của người dùng thích nội dung đó.
     */
    @Override
    public void likeContent(Long sharedContentId, Long userId) {
        // 1. Tìm SharedContent
        SharedContent sharedContent = sharedContentRepository.findById(sharedContentId)
                .orElseThrow(() -> new EntityNotFoundException("Shared content not found with id: " + sharedContentId));
        // 2. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 3. Xây dựng đối tượng ContentLike
        // Lưu ý: Cần đảm bảo rằng repository (ContentLikeRepository) xử lý việc ngăn chặn duplicate likes
        ContentLike like = ContentLike.builder()
                .sharedContent(sharedContent)
                .user(user)
                .build();

        // 4. Lưu ContentLike
        contentLikeRepository.save(like);
    }

    /**
     * Bình luận (comment) về một nội dung đã chia sẻ.
     * 1. Tìm SharedContent bằng sharedContentId. Ném EntityNotFoundException nếu không tìm thấy.
     * 2. Tìm User bằng userId. Ném EntityNotFoundException nếu không tìm thấy.
     * 3. Tạo đối tượng ContentComment mới với SharedContent, User và text.
     * 4. Lưu ContentComment vào cơ sở dữ liệu.
     * 5. Chuyển đổi ContentComment đã lưu thành ContentCommentDto và trả về.
     *
     * @param sharedContentId ID của nội dung đã chia sẻ.
     * @param userId ID của người dùng bình luận.
     * @param text Nội dung bình luận.
     * @return ContentCommentDto của bình luận đã được lưu.
     */
    @Override
    public ContentCommentDto commentOnContent(Long sharedContentId, Long userId, String text) {
        // 1. Tìm SharedContent
        SharedContent sharedContent = sharedContentRepository.findById(sharedContentId)
                .orElseThrow(() -> new EntityNotFoundException("Shared content not found with id: " + sharedContentId));
        // 2. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 3. Xây dựng đối tượng ContentComment
        ContentComment comment = ContentComment.builder()
                .sharedContent(sharedContent)
                .user(user)
                .text(text)
                .build();

        // 4. Lưu vào cơ sở dữ liệu
        ContentComment savedComment = contentCommentRepository.save(comment);
        // 5. Chuyển đổi và trả về DTO
        return modelMapper.map(savedComment, ContentCommentDto.class);
    }

    /**
     * Lấy tất cả bình luận cho một nội dung đã chia sẻ cụ thể.
     * 1. Kiểm tra xem SharedContent có tồn tại không. Ném EntityNotFoundException nếu không.
     * 2. Lấy tất cả bình luận, lọc ra những bình luận thuộc SharedContentId này.
     * Lưu ý: Cách triển khai tối ưu hơn là sử dụng phương thức `findBySharedContentId` từ ContentCommentRepository.
     * 3. Chuyển đổi từng ContentComment Entity thành ContentCommentDto.
     * 4. Thu thập kết quả thành một danh sách.
     *
     * @param sharedContentId ID của nội dung đã chia sẻ.
     * @return List<ContentCommentDto> chứa các bình luận.
     */
    @Override
    public List<ContentCommentDto> getCommentsForContent(Long sharedContentId) {
        // 1. Kiểm tra sự tồn tại của SharedContent
        if (!sharedContentRepository.existsById(sharedContentId)) {
            throw new EntityNotFoundException("Shared content not found with id: " + sharedContentId);
        }
        // 2. Lấy và lọc bình luận (cần tối ưu bằng findBySharedContentId)
        return contentCommentRepository.findAll().stream()
                .filter(comment -> comment.getSharedContent().getId().equals(sharedContentId))
                .map(comment -> modelMapper.map(comment, ContentCommentDto.class)) // 3. Chuyển đổi sang DTO
                .collect(Collectors.toList()); // 4. Thu thập kết quả
    }

    /**
     * Phương thức nội bộ để chuyển đổi SharedContent Entity thành SharedContentDto
     * và tính toán số lượt thích (like) và bình luận (comment) cho nội dung đó.
     *
     * @param sharedContent Entity SharedContent.
     * @return SharedContentDto đã được điền đầy đủ thông tin.
     */
    private SharedContentDto convertToDto(SharedContent sharedContent) {
        // Chuyển đổi cơ bản từ Entity sang DTO
        SharedContentDto dto = modelMapper.map(sharedContent, SharedContentDto.class);
        // Đếm và thiết lập số lượt thích bằng cách sử dụng phương thức của Repository
        dto.setLikeCount(contentLikeRepository.countBySharedContentId(sharedContent.getId()));
        // Đếm và thiết lập số lượt bình luận
        dto.setCommentCount(contentCommentRepository.countBySharedContentId(sharedContent.getId()));
        return dto;
    }
}