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
 * Lớp triển khai dịch vụ (Service Implementation) cho {@link ShareService}.
 * Lớp này cung cấp logic nghiệp vụ cụ thể cho các hoạt động liên quan đến tính năng chia sẻ nội dung,
 * bao gồm chia sẻ một nội dung mới, quản lý lượt thích (likes), và quản lý bình luận (comments).
 * Nó tương tác với nhiều Repository khác nhau để truy cập và thao tác dữ liệu liên quan
 * đến nội dung được chia sẻ, lượt thích, bình luận và người dùng.
 * Nguyên tắc Tiêm phụ thuộc (Dependency Injection) được sử dụng thông qua constructor (@RequiredArgsConstructor của Lombok).
 */
@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    /**
     * {@code sharedContentRepository} là giao diện repository để truy cập dữ liệu của {@link SharedContent}.
     * Được tiêm thông qua constructor để quản lý các nội dung đã được chia sẻ.
     */
    private final SharedContentRepository sharedContentRepository;
    /**
     * {@code contentLikeRepository} là giao diện repository để truy cập dữ liệu của {@link ContentLike}.
     * Được tiêm thông qua constructor để quản lý các lượt thích trên nội dung chia sẻ.
     */
    private final ContentLikeRepository contentLikeRepository;
    /**
     * {@code contentCommentRepository} là giao diện repository để truy cập dữ liệu của {@link ContentComment}.
     * Được tiêm thông qua constructor để quản lý các bình luận trên nội dung chia sẻ.
     */
    private final ContentCommentRepository contentCommentRepository;
    /**
     * {@code userRepository} là giao diện repository để truy cập dữ liệu của {@link User}.
     * Được tiêm thông qua constructor để lấy thông tin người dùng liên quan đến các hoạt động chia sẻ.
     */
    private final UserRepository userRepository;
    /**
     * {@code modelMapper} được sử dụng để chuyển đổi giữa các đối tượng Entity (Domain Model)
     * và DTO (Data Transfer Object) một cách tự động và linh hoạt.
     * Được tiêm thông qua constructor.
     */
    private final ModelMapper modelMapper;

    /**
     * Chia sẻ một nội dung mới bởi một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm Người dùng:</b> Truy vấn {@code userRepository} để tìm đối tượng {@link User} dựa trên {@code userId}.
     *         Nếu không tìm thấy người dùng, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Tạo SharedContent:</b> Xây dựng một đối tượng {@link SharedContent} mới sử dụng Builder pattern.
     *         Các thuộc tính như {@code user}, {@code contentId}, {@code contentType}, và {@code visibility} được thiết lập
     *         từ các tham số đầu vào.</li>
     *     <li><b>Lưu SharedContent:</b> Gọi {@code sharedContentRepository.save()} để lưu đối tượng {@code SharedContent} mới
     *         vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi và Trả về:</b> Chuyển đổi đối tượng {@code SharedContent} đã lưu thành {@link SharedContentDto}
     *         sử dụng phương thức {@link #convertToDto(SharedContent)} nội bộ và trả về.</li>
     * </ol>
     *
     * @param userId ID của người dùng thực hiện việc chia sẻ nội dung.
     * @param contentId ID của nội dung được chia sẻ (ví dụ: ID của một công thức hoặc một kế hoạch dinh dưỡng).
     * @param contentType Loại nội dung được chia sẻ (ví dụ: {@code RECIPE}, {@code PLAN}).
     * @param visibility Độ hiển thị của nội dung chia sẻ (ví dụ: {@code PUBLIC}, {@code PRIVATE}).
     * @return {@link SharedContentDto} đại diện cho nội dung đã được chia sẻ thành công.
     * @throws EntityNotFoundException nếu không tìm thấy người dùng với {@code userId} đã cho.
     */
    @Override
    public SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility) {
        // 1. Tìm người dùng chia sẻ nội dung
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 2. Xây dựng đối tượng SharedContent mới
        SharedContent sharedContent = SharedContent.builder()
                .user(user) // Gắn User Entity vào SharedContent
                .contentId(contentId)
                .contentType(contentType)
                .visibility(visibility)
                .build();

        // 3. Lưu SharedContent vào cơ sở dữ liệu
        SharedContent savedContent = sharedContentRepository.save(sharedContent);
        // 4. Chuyển đổi sang DTO và trả về
        return convertToDto(savedContent);
    }

    /**
     * Lấy tất cả nội dung đã chia sẻ có độ hiển thị là {@code PUBLIC}.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Truy vấn tất cả:</b> Lấy tất cả các đối tượng {@link SharedContent} từ {@code sharedContentRepository}.</li>
     *     <li><b>Lọc công khai:</b> Sử dụng Java Stream API để lọc danh sách, chỉ giữ lại những nội dung có {@code visibility} là {@code PUBLIC}.</li>
     *     <li><b>Chuyển đổi DTO:</b> Ánh xạ từng đối tượng {@code SharedContent} đã lọc thành {@link SharedContentDto}
     *         sử dụng phương thức {@link #convertToDto(SharedContent)} nội bộ. Việc này cũng bao gồm việc đếm số lượt thích và bình luận.</li>
     *     <li><b>Thu thập kết quả:</b> Thu thập các DTO đã chuyển đổi vào một {@link List} và trả về.</li>
     * </ol>
     *
     * @return {@link List<SharedContentDto>} chứa tất cả nội dung công khai đã được chia sẻ.
     */
    @Override
    public List<SharedContentDto> getAllPublicContent() {
        return sharedContentRepository.findAll().stream()
                .filter(sc -> sc.getVisibility() == Visibility.PUBLIC) // Lọc theo độ hiển thị PUBLIC
                .map(this::convertToDto) // Chuyển đổi sang DTO, bao gồm việc tính toán lượt thích và bình luận
                .collect(Collectors.toList()); // Thu thập kết quả vào danh sách
    }

    /**
     * Thực hiện thao tác thích (like) một nội dung đã chia sẻ bởi một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm SharedContent:</b> Truy vấn {@code sharedContentRepository} để tìm đối tượng {@link SharedContent}
     *         bằng {@code sharedContentId}. Nếu không tìm thấy, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Tìm Người dùng:</b> Truy vấn {@code userRepository} để tìm đối tượng {@link User} bằng {@code userId}.
     *         Nếu không tìm thấy, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Tạo ContentLike:</b> Xây dựng một đối tượng {@link ContentLike} mới, liên kết với {@code SharedContent}
     *         và {@code User} đã tìm thấy.</li>
     *     <li><b>Lưu ContentLike:</b> Gọi {@code contentLikeRepository.save()} để lưu lượt thích vào cơ sở dữ liệu.
     *         <p><b>Lưu ý:</b> Cần đảm bảo rằng {@code ContentLikeRepository} hoặc logic ở tầng dưới
     *         xử lý việc ngăn chặn các lượt thích trùng lặp từ cùng một người dùng trên cùng một nội dung.</p></li>
     * </ol>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ mà người dùng muốn thích.
     * @param userId ID của người dùng thực hiện thao tác thích.
     * @throws EntityNotFoundException nếu không tìm thấy nội dung chia sẻ hoặc người dùng.
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
        ContentLike like = ContentLike.builder()
                .sharedContent(sharedContent)
                .user(user)
                .build();

        // 4. Lưu ContentLike vào cơ sở dữ liệu
        contentLikeRepository.save(like);
    }

    /**
     * Thêm một bình luận (comment) vào một nội dung đã chia sẻ bởi một người dùng cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Tìm SharedContent:</b> Truy vấn {@code sharedContentRepository} để tìm đối tượng {@link SharedContent}
     *         bằng {@code sharedContentId}. Nếu không tìm thấy, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Tìm Người dùng:</b> Truy vấn {@code userRepository} để tìm đối tượng {@link User} bằng {@code userId}.
     *         Nếu không tìm thấy, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Tạo ContentComment:</b> Xây dựng một đối tượng {@link ContentComment} mới, liên kết với {@code SharedContent},
     *         {@code User} và nội dung văn bản bình luận.</li>
     *     <li><b>Lưu ContentComment:</b> Gọi {@code contentCommentRepository.save()} để lưu bình luận vào cơ sở dữ liệu.</li>
     *     <li><b>Chuyển đổi và Trả về:</b> Chuyển đổi đối tượng {@code ContentComment} đã lưu thành {@link ContentCommentDto}
     *         sử dụng {@code modelMapper} và trả về.</li>
     * </ol>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ mà người dùng muốn bình luận.
     * @param userId ID của người dùng thực hiện bình luận.
     * @param text Nội dung văn bản của bình luận.
     * @return {@link ContentCommentDto} đại diện cho bình luận đã được lưu thành công.
     * @throws EntityNotFoundException nếu không tìm thấy nội dung chia sẻ hoặc người dùng.
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

        // 4. Lưu ContentComment vào cơ sở dữ liệu
        ContentComment savedComment = contentCommentRepository.save(comment);
        // 5. Chuyển đổi sang DTO và trả về
        return modelMapper.map(savedComment, ContentCommentDto.class);
    }

    /**
     * Lấy tất cả bình luận cho một nội dung đã chia sẻ cụ thể.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Kiểm tra sự tồn tại của SharedContent:</b> Trước tiên, kiểm tra xem {@code SharedContent}
     *         với {@code sharedContentId} đã cho có tồn tại hay không. Nếu không, ném {@link EntityNotFoundException}.</li>
     *     <li><b>Truy vấn và Lọc bình luận:</b> Gọi {@code contentCommentRepository.findAll()} để lấy tất cả các bình luận,
     *         sau đó lọc trong bộ nhớ để chỉ giữ lại những bình luận thuộc về {@code sharedContentId} cụ thể.
     *         <p><b>Lưu ý tối ưu hóa:</b> Cách triển khai hiện tại lấy TẤT CẢ bình luận và lọc trong bộ nhớ,
     *         có thể không hiệu quả với số lượng bình luận lớn. Một cách tối ưu hơn là định nghĩa phương thức
     *         {@code findBySharedContentId(Long sharedContentId)} trực tiếp trong {@code ContentCommentRepository}
     *         để cơ sở dữ liệu xử lý việc lọc.</p></li>
     *     <li><b>Chuyển đổi DTO:</b> Ánh xạ từng đối tượng {@link ContentComment} đã lọc thành {@link ContentCommentDto}
     *         sử dụng {@code modelMapper}.</li>
     *     <li><b>Thu thập kết quả:</b> Thu thập các DTO đã chuyển đổi vào một {@link List} và trả về.</li>
     * </ol>
     *
     * @param sharedContentId ID của nội dung đã chia sẻ để lấy các bình luận.
     * @return {@link List<ContentCommentDto>} chứa tất cả bình luận cho nội dung đã chia sẻ.
     * @throws EntityNotFoundException nếu không tìm thấy nội dung chia sẻ với {@code sharedContentId} đã cho.
     */
    @Override
    public List<ContentCommentDto> getCommentsForContent(Long sharedContentId) {
        // 1. Kiểm tra sự tồn tại của SharedContent
        if (!sharedContentRepository.existsById(sharedContentId)) {
            throw new EntityNotFoundException("Shared content not found with id: " + sharedContentId);
        }
        // 2. Lấy và lọc bình luận (cần tối ưu bằng cách dùng findBySharedContentId từ Repository)
        return contentCommentRepository.findAll().stream()
                .filter(comment -> comment.getSharedContent().getId().equals(sharedContentId))
                .map(comment -> modelMapper.map(comment, ContentCommentDto.class)) // 3. Chuyển đổi sang DTO
                .collect(Collectors.toList()); // 4. Thu thập kết quả
    }

    /**
     * Phương thức trợ giúp nội bộ để chuyển đổi đối tượng {@link SharedContent} Entity
     * thành đối tượng {@link SharedContentDto} và điền thêm các thông tin tổng hợp
     * như số lượt thích và số lượt bình luận.
     *
     * <p><b>Luồng hoạt động:</b></p>
     * <ol>
     *     <li><b>Chuyển đổi cơ bản:</b> Sử dụng {@code modelMapper} để thực hiện ánh xạ các trường cơ bản
     *         từ {@code SharedContent} Entity sang {@code SharedContentDto}.</li>
     *     <li><b>Đếm lượt thích:</b> Gọi {@code contentLikeRepository.countBySharedContentId()} để lấy tổng số
     *         lượt thích cho nội dung chia sẻ và thiết lập vào {@code likeCount} của DTO.</li>
     *     <li><b>Đếm bình luận:</b> Gọi {@code contentCommentRepository.countBySharedContentId()} để lấy tổng số
     *         bình luận cho nội dung chia sẻ và thiết lập vào {@code commentCount} của DTO.</li>
     *     <li><b>Trả về DTO:</b> Trả về đối tượng {@link SharedContentDto} đã được điền đầy đủ thông tin.</li>
     * </ol>
     *
     * @param sharedContent Đối tượng SharedContent Entity cần được chuyển đổi.
     * @return {@link SharedContentDto} đã được điền đầy đủ thông tin, bao gồm số lượt thích và bình luận.
     */
    private SharedContentDto convertToDto(SharedContent sharedContent) {
        // Chuyển đổi các trường cơ bản từ Entity sang DTO
        SharedContentDto dto = modelMapper.map(sharedContent, SharedContentDto.class);
        // Đếm và thiết lập số lượt thích bằng cách sử dụng phương thức của Repository
        dto.setLikeCount(contentLikeRepository.countBySharedContentId(sharedContent.getId()));
        // Đếm và thiết lập số lượt bình luận bằng cách sử dụng phương thức của Repository
        dto.setCommentCount(contentCommentRepository.countBySharedContentId(sharedContent.getId()));
        return dto;
    }
}