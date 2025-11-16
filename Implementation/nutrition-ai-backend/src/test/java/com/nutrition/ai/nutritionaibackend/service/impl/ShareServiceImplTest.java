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
 * Unit tests for the {@link ShareServiceImpl} class.
 * Đảm bảo 100% coverage cho tất cả các phương thức.
 */
@ExtendWith(MockitoExtension.class)
class ShareServiceImplTest {

    @Mock
    private SharedContentRepository sharedContentRepository;
    @Mock
    private ContentLikeRepository contentLikeRepository;
    @Mock
    private ContentCommentRepository contentCommentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ShareServiceImpl shareService;

    private User testUser;
    private SharedContent testSharedContent;
    private SharedContentDto testSharedContentDto;
    private ContentComment testContentComment;
    private ContentCommentDto testContentCommentDto;

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
