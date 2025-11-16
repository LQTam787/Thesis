package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.model.shared.*;
import com.nutrition.ai.nutritionaibackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@code ShareServiceImplTest} là một lớp kiểm thử đơn vị cho {@link ShareServiceImpl}.
 * Lớp này sử dụng JUnit 5 và Mockito để kiểm thử các phương thức logic nghiệp vụ của dịch vụ chia sẻ nội dung,
 * cô lập {@link ShareServiceImpl} khỏi các phụ thuộc bên ngoài như cơ sở dữ liệu thông qua các repository mock
 * ({@link SharedContentRepository}, {@link ContentLikeRepository}, {@link ContentCommentRepository}, {@link UserRepository})
 * và ánh xạ đối tượng thông qua {@link ModelMapper}.
 * Mục tiêu chính là đảm bảo rằng các chức năng chia sẻ nội dung, thích nội dung, bình luận nội dung
 * và lấy bình luận hoạt động chính xác và xử lý các trường hợp biên như không tìm thấy tài nguyên.
 */
@ExtendWith(MockitoExtension.class)
class ShareServiceImplTest {

    /**
     * Mô phỏng {@link SharedContentRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link SharedContent}.
     */
    @Mock
    private SharedContentRepository sharedContentRepository;
    /**
     * Mô phỏng {@link ContentLikeRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link ContentLike}.
     */
    @Mock
    private ContentLikeRepository contentLikeRepository;
    /**
     * Mô phỏng {@link ContentCommentRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link ContentComment}.
     */
    @Mock
    private ContentCommentRepository contentCommentRepository;
    /**
     * Mô phỏng {@link UserRepository} để kiểm soát hành vi truy cập dữ liệu của đối tượng {@link User}.
     */
    @Mock
    private UserRepository userRepository;
    /**
     * Mô phỏng {@link ModelMapper} để kiểm soát hành vi ánh xạ giữa các đối tượng DTO và Domain.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Tiêm {@link ShareServiceImpl} và tự động tiêm các đối tượng {@code @Mock} vào các trường tương ứng của nó.
     * Đây là đối tượng thực sẽ được kiểm thử.
     */
    @InjectMocks
    private ShareServiceImpl shareService;

    private User testUser;
    private SharedContent testSharedContent;
    private SharedContentDto testSharedContentDto;
    private ContentComment testContentComment;
    private ContentCommentDto testContentCommentDto;

    /**
     * Phương thức thiết lập chạy trước mỗi bài kiểm thử (phương thức được chú thích bởi {@code @Test}).
     * Phương thức này chịu trách nhiệm khởi tạo các đối tượng dữ liệu giả định như {@link User}, {@link SharedContent},
     * {@link SharedContentDto}, {@link ContentComment} và {@link ContentCommentDto} để sử dụng trong các kiểm thử.
     */
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testSharedContent = SharedContent.builder()
                .id(100L)
                .user(testUser)
                .contentId(1L)
                .contentType(ContentType.RECIPE)
                .visibility(Visibility.PUBLIC)
                .build();

        testSharedContentDto = new SharedContentDto();
        testSharedContentDto.setId(100L);
        testSharedContentDto.setUserId(1L);
        testSharedContentDto.setContentId(1L);
        testSharedContentDto.setContentType(ContentType.RECIPE);
        testSharedContentDto.setVisibility(Visibility.PUBLIC);
        testSharedContentDto.setLikeCount(0);
        testSharedContentDto.setCommentCount(0);

        testContentComment = ContentComment.builder()
                .id(200L)
                .sharedContent(testSharedContent)
                .user(testUser)
                .text("This is a comment.")
                .build();

        testContentCommentDto = new ContentCommentDto();
        testContentCommentDto.setId(200L);
        testContentCommentDto.setSharedContentId(100L);
        testContentCommentDto.setUserId(1L);
        testContentCommentDto.setText("This is a comment.");
    }

    /**
     * Kiểm thử phương thức {@code shareContent} của {@link ShareServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng người dùng tồn tại,
     *    lưu nội dung chia sẻ thành công và ánh xạ đối tượng.
     * 2. Gọi phương thức {@code shareContent} thực tế của {@code shareService}.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link SharedContentDto} trả về không null và khớp với đối tượng giả định.
     * 4. Xác minh tương tác:
     *    - Đảm bảo rằng {@code userRepository.findById}, {@code sharedContentRepository.save},
     *    {@code modelMapper.map}, {@code contentLikeRepository.countBySharedContentId}
     *    và {@code contentCommentRepository.countBySharedContentId} đã được gọi đúng 1 lần.
     */
    @DisplayName("Test shareContent - Success")
    @Test
    void shareContent_Success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(sharedContentRepository.save(any(SharedContent.class))).thenReturn(testSharedContent);
        when(modelMapper.map(testSharedContent, SharedContentDto.class)).thenReturn(testSharedContentDto);
        when(contentLikeRepository.countBySharedContentId(testSharedContent.getId())).thenReturn(0l);
        when(contentCommentRepository.countBySharedContentId(testSharedContent.getId())).thenReturn(0l);

        SharedContentDto result = shareService.shareContent(
                testUser.getId(), testSharedContent.getContentId(), testSharedContent.getContentType(), testSharedContent.getVisibility());

        assertNotNull(result);
        assertEquals(testSharedContentDto.getId(), result.getId());
        assertEquals(testSharedContentDto.getContentId(), result.getContentId());
        assertEquals(testSharedContentDto.getContentType(), result.getContentType());
        assertEquals(testSharedContentDto.getVisibility(), result.getVisibility());
        assertEquals(testSharedContentDto.getUserId(), result.getUserId());

        verify(userRepository, times(1)).findById(testUser.getId());
        verify(sharedContentRepository, times(1)).save(any(SharedContent.class));
        verify(modelMapper, times(1)).map(testSharedContent, SharedContentDto.class);
        verify(contentLikeRepository, times(1)).countBySharedContentId(testSharedContent.getId());
        verify(contentCommentRepository, times(1)).countBySharedContentId(testSharedContent.getId());
    }

    /**
     * Kiểm thử phương thức {@code shareContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code userRepository}: khi {@code findById(nonExistentUserId)} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code shareContent} thực tế của {@code shareService} với ID người dùng không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng các phương thức {@code save}, {@code map}, {@code countBySharedContentId} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @DisplayName("Test shareContent - User Not Found")
    @Test
    void shareContent_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 99L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.shareContent(nonExistentUserId, 1L, ContentType.RECIPE, Visibility.PUBLIC);
        });

        assertEquals("User not found with id: " + nonExistentUserId, thrown.getMessage());

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(sharedContentRepository, never()).save(any(SharedContent.class));
        verify(modelMapper, never()).map(any(), any());
        verify(contentLikeRepository, never()).countBySharedContentId(anyLong());
        verify(contentCommentRepository, never()).countBySharedContentId(anyLong());
    }

    /**
     * Kiểm thử phương thức {@code getAllPublicContent} của {@link ShareServiceImpl} trong trường hợp trả về nội dung công khai.
     * Luồng hoạt động:
     * 1. Tạo các đối tượng {@link SharedContent} giả định, bao gồm cả nội dung công khai và nội dung riêng tư.
     * 2. Tạo các đối tượng {@link SharedContentDto} giả định cho nội dung công khai.
     * 3. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng việc tìm tất cả nội dung,
     *    ánh xạ nội dung công khai và đếm lượt thích/bình luận.
     * 4. Gọi phương thức {@code getAllPublicContent} thực tế của {@code shareService}.
     * 5. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 2.
     *    - Kiểm tra xem danh sách có chứa các nội dung công khai và không chứa nội dung riêng tư.
     * 6. Xác minh tương tác:
     *    - Đảm bảo rằng {@code sharedContentRepository.findAll()} đã được gọi đúng 1 lần.
     *    - Đảm bảo rằng {@code modelMapper.map}, {@code contentLikeRepository.countBySharedContentId}
     *    và {@code contentCommentRepository.countBySharedContentId} đã được gọi đúng số lần cho nội dung công khai.
     */
    @DisplayName("Test getAllPublicContent - Returns Public Content")
    @Test
    void getAllPublicContent_ReturnsPublicContent() {
        SharedContent publicContent1 = SharedContent.builder().id(101L).visibility(Visibility.PUBLIC).user(testUser).build();
        SharedContent publicContent2 = SharedContent.builder().id(102L).visibility(Visibility.PUBLIC).user(testUser).build();
        SharedContent privateContent = SharedContent.builder().id(103L).visibility(Visibility.FRIENDS).user(testUser).build();

        SharedContentDto publicDto1 = new SharedContentDto();
        publicDto1.setId(101L);
        publicDto1.setVisibility(Visibility.PUBLIC);
        publicDto1.setUserId(testUser.getId());

        SharedContentDto publicDto2 = new SharedContentDto();
        publicDto2.setId(102L);
        publicDto2.setVisibility(Visibility.PUBLIC);
        publicDto2.setUserId(testUser.getId());

        when(sharedContentRepository.findAll()).thenReturn(Arrays.asList(publicContent1, privateContent, publicContent2));
        when(modelMapper.map(publicContent1, SharedContentDto.class)).thenReturn(publicDto1);
        when(modelMapper.map(publicContent2, SharedContentDto.class)).thenReturn(publicDto2);
        when(contentLikeRepository.countBySharedContentId(publicContent1.getId())).thenReturn(1L);
        when(contentCommentRepository.countBySharedContentId(publicContent1.getId())).thenReturn(2L);
        when(contentLikeRepository.countBySharedContentId(publicContent2.getId())).thenReturn(0L);
        when(contentCommentRepository.countBySharedContentId(publicContent2.getId())).thenReturn(0L);

        List<SharedContentDto> result = shareService.getAllPublicContent();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(101L)));
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(102L)));
        assertFalse(result.stream().anyMatch(dto -> dto.getId().equals(103L))); // Private content should not be included

        verify(sharedContentRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(publicContent1, SharedContentDto.class);
        verify(modelMapper, times(1)).map(publicContent2, SharedContentDto.class);
        verify(contentLikeRepository, times(1)).countBySharedContentId(publicContent1.getId());
        verify(contentCommentRepository, times(1)).countBySharedContentId(publicContent1.getId());
        verify(contentLikeRepository, times(1)).countBySharedContentId(publicContent2.getId());
        verify(contentCommentRepository, times(1)).countBySharedContentId(publicContent2.getId());
    }

    /**
     * Kiểm thử phương thức {@code getAllPublicContent} của {@link ShareServiceImpl} khi không có nội dung công khai.
     * Luồng hoạt động:
     * 1. Tạo các đối tượng {@link SharedContent} giả định chỉ chứa nội dung riêng tư.
     * 2. Thiết lập hành vi giả lập cho {@code sharedContentRepository}: khi {@code findAll()} được gọi,
     *    nó sẽ trả về danh sách các nội dung riêng tư.
     * 3. Gọi phương thức {@code getAllPublicContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và là một danh sách trống.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code map}, {@code countBySharedContentId} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @DisplayName("Test getAllPublicContent - Returns Empty List if No Public Content")
    @Test
    void getAllPublicContent_ReturnsEmptyList_NoPublicContent() {
        SharedContent privateContent1 = SharedContent.builder().id(101L).visibility(Visibility.FRIENDS).user(testUser).build();
        SharedContent privateContent2 = SharedContent.builder().id(102L).visibility(Visibility.FRIENDS).user(testUser).build();

        when(sharedContentRepository.findAll()).thenReturn(Arrays.asList(privateContent1, privateContent2));

        List<SharedContentDto> result = shareService.getAllPublicContent();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(sharedContentRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());
        verify(contentLikeRepository, never()).countBySharedContentId(anyLong());
        verify(contentCommentRepository, never()).countBySharedContentId(anyLong());
    }

    /**
     * Kiểm thử phương thức {@code getAllPublicContent} của {@link ShareServiceImpl} khi không có nội dung nào trong kho lưu trữ.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code sharedContentRepository}: khi {@code findAll()} được gọi,
     *    nó sẽ trả về một danh sách trống.
     * 2. Gọi phương thức {@code getAllPublicContent} thực tế của {@code shareService}.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và là một danh sách trống.
     * 4. Xác minh tương tác: Đảm bảo rằng các phương thức {@code map}, {@code countBySharedContentId} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @DisplayName("Test getAllPublicContent - Returns Empty List if No Content")
    @Test
    void getAllPublicContent_ReturnsEmptyList_NoContent() {
        when(sharedContentRepository.findAll()).thenReturn(Collections.emptyList());

        List<SharedContentDto> result = shareService.getAllPublicContent();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(sharedContentRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());
        verify(contentLikeRepository, never()).countBySharedContentId(anyLong());
        verify(contentCommentRepository, never()).countBySharedContentId(anyLong());
    }

    /**
     * Kiểm thử phương thức {@code likeContent} của {@link ShareServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository để mô phỏng nội dung chia sẻ và người dùng tồn tại.
     * 2. Gọi phương thức {@code likeContent} thực tế của {@code shareService}.
     * 3. Xác minh tương tác:
     *    - Đảm bảo rằng {@code sharedContentRepository.findById}, {@code userRepository.findById}
     *    và {@code contentLikeRepository.save} đã được gọi đúng 1 lần.
     */
    @DisplayName("Test likeContent - Success")
    @Test
    void likeContent_Success() {
        when(sharedContentRepository.findById(testSharedContent.getId())).thenReturn(Optional.of(testSharedContent));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        shareService.likeContent(testSharedContent.getId(), testUser.getId());

        verify(sharedContentRepository, times(1)).findById(testSharedContent.getId());
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(contentLikeRepository, times(1)).save(any(ContentLike.class));
    }

    /**
     * Kiểm thử phương thức {@code likeContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy nội dung chia sẻ.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho {@code sharedContentRepository}: khi {@code findById(nonExistentSharedContentId)} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy nội dung).
     * 2. Gọi phương thức {@code likeContent} thực tế của {@code shareService} với ID nội dung không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findById(user)} và {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @DisplayName("Test likeContent - SharedContent Not Found")
    @Test
    void likeContent_SharedContentNotFound_ThrowsException() {
        Long nonExistentSharedContentId = 999L;
        when(sharedContentRepository.findById(nonExistentSharedContentId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.likeContent(nonExistentSharedContentId, testUser.getId());
        });

        assertEquals("Shared content not found with id: " + nonExistentSharedContentId, thrown.getMessage());

        verify(sharedContentRepository, times(1)).findById(nonExistentSharedContentId);
        verify(userRepository, never()).findById(anyLong());
        verify(contentLikeRepository, never()).save(any(ContentLike.class));
    }

    /**
     * Kiểm thử phương thức {@code likeContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository:
     *    - {@code sharedContentRepository.findById(testSharedContent.getId())} trả về đối tượng {@link SharedContent} giả định.
     *    - {@code userRepository.findById(nonExistentUserId)} trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 2. Gọi phương thức {@code likeContent} thực tế của {@code shareService} với ID người dùng không tồn tại.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 4. Xác minh tương tác: Đảm bảo rằng phương thức {@code save} KHÔNG BAO GIỜ được gọi.
     */
    @DisplayName("Test likeContent - User Not Found")
    @Test
    void likeContent_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 99L;
        when(sharedContentRepository.findById(testSharedContent.getId())).thenReturn(Optional.of(testSharedContent));
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.likeContent(testSharedContent.getId(), nonExistentUserId);
        });

        assertEquals("User not found with id: " + nonExistentUserId, thrown.getMessage());

        verify(sharedContentRepository, times(1)).findById(testSharedContent.getId());
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(contentLikeRepository, never()).save(any(ContentLike.class));
    }

    /**
     * Kiểm thử phương thức {@code commentOnContent} của {@link ShareServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Tạo một chuỗi bình luận.
     * 2. Thiết lập hành vi giả lập cho các repository và mapper để mô phỏng nội dung chia sẻ và người dùng tồn tại,
     *    lưu bình luận thành công và ánh xạ đối tượng.
     * 3. Gọi phương thức {@code commentOnContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem đối tượng {@link ContentCommentDto} trả về không null và khớp với đối tượng giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code sharedContentRepository.findById}, {@code userRepository.findById},
     *    {@code contentCommentRepository.save} và {@code modelMapper.map} đã được gọi đúng 1 lần.
     */
    @DisplayName("Test commentOnContent - Success")
    @Test
    void commentOnContent_Success() {
        String commentText = "Great content!";

        when(sharedContentRepository.findById(testSharedContent.getId())).thenReturn(Optional.of(testSharedContent));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(contentCommentRepository.save(any(ContentComment.class))).thenReturn(testContentComment);
        when(modelMapper.map(testContentComment, ContentCommentDto.class)).thenReturn(testContentCommentDto);

        ContentCommentDto result = shareService.commentOnContent(
                testSharedContent.getId(), testUser.getId(), commentText);

        assertNotNull(result);
        assertEquals(testContentCommentDto.getId(), result.getId());
        assertEquals(testContentCommentDto.getText(), result.getText());
        assertEquals(testContentCommentDto.getSharedContentId(), result.getSharedContentId());
        assertEquals(testContentCommentDto.getUserId(), result.getUserId());

        verify(sharedContentRepository, times(1)).findById(testSharedContent.getId());
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(contentCommentRepository, times(1)).save(any(ContentComment.class));
        verify(modelMapper, times(1)).map(testContentComment, ContentCommentDto.class);
    }

    /**
     * Kiểm thử phương thức {@code commentOnContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy nội dung chia sẻ.
     * Luồng hoạt động:
     * 1. Tạo một chuỗi bình luận và ID nội dung chia sẻ không tồn tại.
     * 2. Thiết lập hành vi giả lập cho {@code sharedContentRepository}: khi {@code findById(nonExistentSharedContentId)} được gọi,
     *    nó sẽ trả về một {@link Optional#empty()} (không tìm thấy nội dung).
     * 3. Gọi phương thức {@code commentOnContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findById(user)}, {@code save} và {@code map} KHÔNG BAO GIỜ
     *    được gọi.
     */
    @DisplayName("Test commentOnContent - SharedContent Not Found")
    @Test
    void commentOnContent_SharedContentNotFound_ThrowsException() {
        Long nonExistentSharedContentId = 999L;
        String commentText = "Test comment.";
        when(sharedContentRepository.findById(nonExistentSharedContentId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.commentOnContent(nonExistentSharedContentId, testUser.getId(), commentText);
        });

        assertEquals("Shared content not found with id: " + nonExistentSharedContentId, thrown.getMessage());

        verify(sharedContentRepository, times(1)).findById(nonExistentSharedContentId);
        verify(userRepository, never()).findById(anyLong());
        verify(contentCommentRepository, never()).save(any(ContentComment.class));
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code commentOnContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy người dùng.
     * Luồng hoạt động:
     * 1. Tạo một chuỗi bình luận và ID người dùng không tồn tại.
     * 2. Thiết lập hành vi giả lập cho các repository:
     *    - {@code sharedContentRepository.findById(testSharedContent.getId())} trả về đối tượng {@link SharedContent} giả định.
     *    - {@code userRepository.findById(nonExistentUserId)} trả về một {@link Optional#empty()} (không tìm thấy người dùng).
     * 3. Gọi phương thức {@code commentOnContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code save} và {@code map} KHÔNG BAO GIỜ được gọi.
     */
    @DisplayName("Test commentOnContent - User Not Found")
    @Test
    void commentOnContent_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 99L;
        String commentText = "Test comment.";
        when(sharedContentRepository.findById(testSharedContent.getId())).thenReturn(Optional.of(testSharedContent));
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.commentOnContent(testSharedContent.getId(), nonExistentUserId, commentText);
        });

        assertEquals("User not found with id: " + nonExistentUserId, thrown.getMessage());

        verify(sharedContentRepository, times(1)).findById(testSharedContent.getId());
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(contentCommentRepository, never()).save(any(ContentComment.class));
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code getCommentsForContent} của {@link ShareServiceImpl} trong trường hợp thành công.
     * Luồng hoạt động:
     * 1. Tạo các đối tượng {@link ContentComment} và {@link ContentCommentDto} giả định.
     * 2. Thiết lập hành vi giả lập cho các repository và mapper:
     *    - {@code sharedContentRepository.existsById(testSharedContent.getId())} trả về {@code true}.
     *    - {@code contentCommentRepository.findAll()} trả về danh sách các bình luận giả định.
     *    - {@code modelMapper.map} ánh xạ các đối tượng {@link ContentComment} thành {@link ContentCommentDto}.
     * 3. Gọi phương thức {@code getCommentsForContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và có kích thước là 2.
     *    - Kiểm tra xem danh sách có chứa các đối tượng {@link ContentCommentDto} giả định.
     * 5. Xác minh tương tác:
     *    - Đảm bảo rằng {@code sharedContentRepository.existsById}, {@code contentCommentRepository.findAll}
     *    và {@code modelMapper.map} đã được gọi đúng số lần.
     */
    @DisplayName("Test getCommentsForContent - Success")
    @Test
    void getCommentsForContent_Success() {
        ContentComment anotherComment = ContentComment.builder()
                .id(201L)
                .sharedContent(testSharedContent)
                .user(testUser)
                .text("Another comment.")
                .build();

        ContentCommentDto anotherCommentDto = new ContentCommentDto();
        anotherCommentDto.setId(201L);
        anotherCommentDto.setSharedContentId(testSharedContent.getId());
        anotherCommentDto.setUserId(testUser.getId());
        anotherCommentDto.setText("Another comment.");

        when(sharedContentRepository.existsById(testSharedContent.getId())).thenReturn(true);
        when(contentCommentRepository.findAll()).thenReturn(Arrays.asList(testContentComment, anotherComment));
        when(modelMapper.map(testContentComment, ContentCommentDto.class)).thenReturn(testContentCommentDto);
        when(modelMapper.map(anotherComment, ContentCommentDto.class)).thenReturn(anotherCommentDto);

        List<ContentCommentDto> result = shareService.getCommentsForContent(testSharedContent.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(testContentCommentDto.getId())));
        assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(anotherCommentDto.getId())));

        verify(sharedContentRepository, times(1)).existsById(testSharedContent.getId());
        verify(contentCommentRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(testContentComment, ContentCommentDto.class);
        verify(modelMapper, times(1)).map(anotherComment, ContentCommentDto.class);
    }

    /**
     * Kiểm thử phương thức {@code getCommentsForContent} của {@link ShareServiceImpl} trong trường hợp không tìm thấy nội dung chia sẻ.
     * Luồng hoạt động:
     * 1. Tạo một ID nội dung chia sẻ không tồn tại.
     * 2. Thiết lập hành vi giả lập cho {@code sharedContentRepository}: khi {@code existsById(nonExistentSharedContentId)} được gọi,
     *    nó sẽ trả về {@code false} (không tìm thấy nội dung).
     * 3. Gọi phương thức {@code getCommentsForContent} thực tế của {@code shareService}.
     * 4. Xác minh kết quả:
     *    - Kiểm tra xem một {@link EntityNotFoundException} có được ném ra hay không.
     *    - Kiểm tra xem thông báo lỗi của ngoại lệ có chính xác hay không.
     * 5. Xác minh tương tác: Đảm bảo rằng các phương thức {@code findAll} và {@code map} KHÔNG BAO GIỜ được gọi.
     */
    @DisplayName("Test getCommentsForContent - SharedContent Not Found")
    @Test
    void getCommentsForContent_SharedContentNotFound_ThrowsException() {
        Long nonExistentSharedContentId = 999L;
        when(sharedContentRepository.existsById(nonExistentSharedContentId)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            shareService.getCommentsForContent(nonExistentSharedContentId);
        });

        assertEquals("Shared content not found with id: " + nonExistentSharedContentId, thrown.getMessage());

        verify(sharedContentRepository, times(1)).existsById(nonExistentSharedContentId);
        verify(contentCommentRepository, never()).findAll();
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Kiểm thử phương thức {@code getCommentsForContent} của {@link ShareServiceImpl} trong trường hợp không có bình luận nào
     * cho nội dung chia sẻ tồn tại.
     * Luồng hoạt động:
     * 1. Thiết lập hành vi giả lập cho các repository:
     *    - {@code sharedContentRepository.existsById(testSharedContent.getId())} trả về {@code true}.
     *    - {@code contentCommentRepository.findAll()} trả về một danh sách trống.
     * 2. Gọi phương thức {@code getCommentsForContent} thực tế của {@code shareService}.
     * 3. Xác minh kết quả:
     *    - Kiểm tra xem danh sách kết quả không null và là một danh sách trống.
     * 4. Xác minh tương tác: Đảm bảo rằng phương thức {@code map} KHÔNG BAO GIỜ được gọi.
     */
    @DisplayName("Test getCommentsForContent - No Comments for Existing SharedContent")
    @Test
    void getCommentsForContent_NoCommentsForExistingSharedContent() {
        when(sharedContentRepository.existsById(testSharedContent.getId())).thenReturn(true);
        when(contentCommentRepository.findAll()).thenReturn(Collections.emptyList());

        List<ContentCommentDto> result = shareService.getCommentsForContent(testSharedContent.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(sharedContentRepository, times(1)).existsById(testSharedContent.getId());
        verify(contentCommentRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());
    }
}
